package com.ccblog.service.chat.Impl;

import cn.hutool.json.JSONObject;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.chat.ai.*;
import com.ccblog.entity.chat.ChatAiSession;
import com.ccblog.enumeration.chat.AiFinishReasonEnum;
import com.ccblog.enumeration.chat.ChatAiSenderEnum;
import com.ccblog.mapper.chat.ChatAiMapper;
import com.ccblog.mapper.chat.ChatAiSessionMapper;
import com.ccblog.redis.chat.ChatAiMsgRedis;
import com.ccblog.redis.chat.ChatAiRedis;
import com.ccblog.redis.chat.ChatAiSessionRedis;
import com.ccblog.service.chat.ChatAiService;
import com.ccblog.utils.TruncateUtil;
import com.ccblog.vo.chat.ChatAiCursorVO;
import com.ccblog.vo.chat.ChatAiSessionCursorVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.ccblog.enumeration.chat.AiSessionFieldEnum.COL_LAST_TIME;
import static com.ccblog.enumeration.chat.AiSessionFieldEnum.COL_SEQ;
import static com.ccblog.utils.JsonUtil.toJson;

/**
 * 用户ai聊天接口实现类
 * @author czc
 * @date 2025/10/28
 */
@Service
@Slf4j
public class ChatAiServiceImpl implements ChatAiService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ChatAiMapper chatAiMapper;

    @Autowired
    private ChatAiSessionMapper chatAiSessionMapper;

    @Autowired
    private ChatAiRedis chatAiRedis;

    @Autowired
    private ChatAiSessionRedis chatAiSessionRedis;

    @Autowired
    private ChatAiMsgRedis chatAiMsgRedis;

    @Value("${ai.xunfei.qwen.baseUrl}")
    private String baseUrl;

    @Value("${ai.xunfei.qwen.apiKey}")
    private String apiKey;

    @Value("${ai.xunfei.qwen.secretKey}")
    private String secretKey;



    // 拼文本 + 缓存token数
    // 为什么不把用户相关上下文存在这里:一种解释是userId只读,并且要保证线程安全,因为content要一直写
    private class ContentBag {
        final StringBuilder content = new StringBuilder();
        int totalTokens;
    }

    /**
     * 获得ai回复
     * @param userRequset
     * @return
     */
    public Flux<ServerSentEvent<JSONObject>> getAnswer(ChatAiRequestDTO userRequset) {
        if(secretKey==null||secretKey.isBlank()){
            log.error("***** 未配置大模型接口信息 *****");
            return null;
        }
        // 使用 WebClient 发送 SSE 请求
        WebClient webClient = webClientBuilder
                .baseUrl(baseUrl) // 设置目标 API 地址(大模型地址)
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer "+ secretKey) // 认证信息
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE) // 期望接收 SSE 格式的响应
                .build();

        // 将前端给出的request封装为调用api接口所需的请求体
        ChatAiApiRequestDTO chatAiApiRequestDTO = new ChatAiApiRequestDTO();
        chatAiApiRequestDTO.setMessages(List.of(
                new ChatAiApiRequestDTO.Message("user", userRequset.getContent())
        ));
        chatAiApiRequestDTO.setStream(true); // 设置流式返回
        chatAiApiRequestDTO.setModel(apiKey); // 后需要根据前端的名称来转id
        BeanUtils.copyProperties(userRequset,chatAiApiRequestDTO);

        // 用户记录准备入库
        Long sessionId = userRequset.getSessionId();
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        LocalDateTime msgTime = userRequset.getCreateTime();
        Long seq;
        if(sessionId==0){ // 新会话
            sessionId = saveNewSession(userId,userRequset.getContent(),msgTime);
            seq = 1L; // 默认1
        }
        else{
            seq = chatAiSessionRedis.incrSeq(sessionId,1);
            chatAiSessionRedis.setSession(sessionId, Map.of(COL_LAST_TIME,msgTime.toString())); // 更新会话时间
        }
        chatAiMsgRedis.removeMessage(sessionId); // 删除缓存
        ChatAiAggDTO chatAiAggDTO = ChatAiAggDTO.builder()
                .seq(seq)
                .sender(ChatAiSenderEnum.USER.getType())
                .createTime(msgTime)
                .content(userRequset.getContent())
                .sessionId(sessionId)
                .userId(userId).build();
        chatAiRedis.aggregate(chatAiAggDTO);

        // 流式返回
        AtomicReference<Subscription> upRef = new AtomicReference<>();
        ContentBag bag = new ContentBag();
        SSEContext ctx = SSEContext.of(userId, sessionId); // 上下文信息
        Long finalSessionId = sessionId; // 捕捉为final局部变量
        Flux<String> upstream = webClient.post()
                .bodyValue(chatAiApiRequestDTO)
                .retrieve()
                .bodyToFlux(String.class);

        // 拼返回帧
        // 生成帧头(带有两个seq信息)
        Long aiSeq = chatAiSessionRedis.incrSeq(sessionId,1); // 预分配ai回的seq
        Mono<ServerSentEvent<JSONObject>> seqFrame = null;
        seqFrame = Mono.fromCallable(() -> ServerSentEvent.<JSONObject>builder()
                .event("seq")
                // 这个data可以改进为只有新会话才传,不过总共也没多少字节而且就一帧
                .data(toJson(Map.of("sessionId", finalSessionId,"userSeq", seq, "aiSeq", aiSeq)))
                .build());
        return Flux.concat(
                seqFrame,                       // 首帧:帧头
                upstream // 解析响应数据为流式字符串
                    .doOnSubscribe(upRef::set)   // 订阅(暂时不是很懂这个操作)
                    .flatMap(data -> processResponse(data, bag,chatAiApiRequestDTO)) // 处理函数
                    .contextWrite(reactorCtx -> reactorCtx.put(SSEContext.class, ctx)) // 挂上下文,注意必须在flatMap下面
                    .doFinally(signal -> onFinally(signal, aiSeq,userId, finalSessionId, bag)) // 监听 SSE 连接终止（可能是正常结束或异常）
                    .onErrorResume(this::handleError) // 发生异常时进行错误处理
                );
    }

    /**
     * 处理 SSE 响应数据
     *
     * @param data          SSE 返回的单条数据
     * @param bag           内容背包,用于累积完整的返回内容+token数
     * @return 处理后的 SSE 事件
     */
    private Mono<ServerSentEvent<JSONObject>> processResponse(
                    String data, ContentBag bag,ChatAiApiRequestDTO request) {
        return Mono.deferContextual(ctx -> { // 拿到上下文并处理(实际上没用到,留着备用)
//            SSEContext c = ctx.get(SSEContext.class);
//            Long userId    = c.getUserId();
//            Long sessionId = c.getSessionId();

            if ("[DONE]".equals(data)) { // 结束帧
                log.info("SSE 消息接收完成，最终内容: {}", bag.content);

                // 返回一个 SSE 事件，表示会话已完成
                return Mono.just(ServerSentEvent.<JSONObject>builder()
                        .event("done")
                        .id(UUID.randomUUID().toString())
                        .data(new JSONObject()) // 发送空数据，仅通知前端结束
                        .build());
            }

            try { // 普通add帧
                // 解析 SSE 返回的 JSON 数据
                ChatAiResponseDTO response = new ObjectMapper().readValue(data, ChatAiResponseDTO.class);
                String content = "";
                if (request.getStream()) {
                    content = response.getChoices().get(0).getDelta().getContent();
                } else {
                    content = response.getChoices().get(0).getMessage().getContent();
                }

                // 累积返回的消息内容
                if (content != null) {
                    bag.content.append(content);
                }
                // 拿到token数
                if (response.getUsage() != null && response.getUsage().getTotal_tokens() > 0) {
                    bag.totalTokens = response.getUsage().getTotal_tokens();
                }

                // 构造 SSE 事件并返回
                return Mono.just(ServerSentEvent.<JSONObject>builder()
                        .event("add") // 事件类型
                        .id(UUID.randomUUID().toString()) // 生成唯一 ID
                        .data(new ObjectMapper().convertValue(response, JSONObject.class)) // 发送解析后的 JSON 数据
                        .build());
            } catch (JsonProcessingException e) {
                log.error("JSON 解析失败: {}", data, e);
                return Mono.just(createErrorEvent("解析失败")); // 解析异常时返回错误事件
            }
        });
    }

    /**
     * 获取聊天历史记录
     * @param sessionId 会话id
     * @param cursor    最前一条时间信息
     * @param cursorSeq 最前一条消息会话消息seq
     * @param limit     数量
     * @return
     */
    public ChatAiCursorVO getChatHistory(Long sessionId, LocalDateTime cursor, Long cursorSeq, int limit) {
        // 新会话
        if(sessionId==0){
            return ChatAiCursorVO.builder()
                    .chatAiItems(List.of())
                    .hasMore(false).build();
        }
        boolean lastestFlag = (cursor == null);
        ChatAiCursorVO chatAiCursorVO;
        if(lastestFlag){ // 最新消息先查缓存
            chatAiCursorVO = chatAiMsgRedis.getMessage(sessionId,limit);
            if(chatAiCursorVO!=null) return chatAiCursorVO;
        }
        // 非新会话/缓存未命中
        if(lastestFlag){
            cursor = LocalDateTime.of(8970, 1, 1, 0, 0);
            cursorSeq = Long.MAX_VALUE;
        }
        List<ChatAiItemDTO> chatAiItems =
                chatAiMapper.listCursorChatHistory(sessionId,cursor,cursorSeq,limit+1);
        int size = chatAiItems.size();
        if(size>limit) chatAiItems.remove(size-1);
        chatAiCursorVO = ChatAiCursorVO.builder()
                .chatAiItems(chatAiItems)
                .hasMore(size>limit).build();
        if(lastestFlag){
            chatAiMsgRedis.setMessage(chatAiCursorVO,sessionId,limit); // 存缓存
        }
        return chatAiCursorVO;
    }

    /**
     * 游标方式获取用户会话
     * @param cursorId  游标sessionId
     * @param limit     数量
     * @return
     */
    public ChatAiSessionCursorVO getChatSession(Long cursorId, int limit) {
        cursorId = (cursorId == null ? Long.MAX_VALUE:cursorId);
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        List<ChatAiSessionItemDTO> chatSessionItems =
                chatAiSessionMapper.listCursorChatSession(userId,cursorId,limit+1);
        for(ChatAiSessionItemDTO dto:chatSessionItems){
            dto.setLastMsgTime(LocalDateTime.parse(chatAiSessionRedis.getSession(dto.getSessionId(),COL_LAST_TIME)));
        }
        int size = chatSessionItems.size();
        if(size>limit) chatSessionItems.remove(size-1);
        return ChatAiSessionCursorVO.builder()
                .chatAiSessionItems(chatSessionItems)
                .hasMore(size>limit).build();
    }

    /**
     * 用户删除会话
     * @param sessionId 会话id
     */
    public void deleteSession(Long sessionId) {
        if(sessionId==0)return; // 新会话不管
        chatAiSessionMapper.deleteSession(sessionId);
    }

    /**
     * 用户重命名会话
     * @param sessionId 会话id
     * @param title     新名称
     */
    public void renameSession(Long sessionId, String title) {
        if(sessionId==0) return; // 新会话不管
        chatAiSessionMapper.renameSession(sessionId,title);
    }

    /**
     * 用户删除消息
     * @param sessionId
     * @param seq       会话内递增序号
     */
    public void deleteMessage(Long sessionId, Long seq) {
        chatAiMapper.deleteMsgBySeq(sessionId,seq);
        chatAiMsgRedis.removeMessage(sessionId); // 清除最新消息缓存(甭管用户有没有删缓存里的)
    }

    /**
     * 创建 SSE 错误事件
     *
     * @param message 错误信息
     * @return SSE 错误事件
     */
    private ServerSentEvent<JSONObject> createErrorEvent(String message) {
        return ServerSentEvent.<JSONObject>builder()
                .event("error") // 事件类型为错误
                .id(UUID.randomUUID().toString()) // 生成唯一 ID
//                .data(ModelMessageUtils.convertModelChatResponse(UUID.randomUUID().toString(), message)) // 发送错误信息
                .build();
    }

    /**
     * 处理 SSE 请求中的异常情况
     *
     * @param e 异常对象
     * @return 错误事件的 Mono
     */
    private Mono<ServerSentEvent<JSONObject>> handleError(Throwable e) {
        log.error("SSE 请求处理异常", e);
        return Mono.just(createErrorEvent("服务异常，请联系管理员"));
    }

    /**
     * 监听 SSE 连接终止（包括正常结束或异常）
     * @param signal      终止信号
     * @param aiSeq       当前AI消息的seq
     * @param userId      用户id
     * @param sessionId   会话id
     * @param bag         消息包
     */
    private void onFinally(SignalType signal, Long aiSeq,Long userId, Long sessionId, ContentBag bag) {
//        log.info("SSE 结束");
        Integer status=AiFinishReasonEnum.NORMAL.getType();
        switch (signal) {
            case ON_COMPLETE:
//                status = AiFinishReasonEnum.NORMAL.getType();
                break;
            case ON_ERROR:
                status = AiFinishReasonEnum.OTHER.getType();
                break;
            case CANCEL:
                status = AiFinishReasonEnum.USER.getType();
                break;
            default:
                ;
        }
        saveAiAnswer(aiSeq,userId,sessionId,bag,status);
    }

    /**
     * 创建新会话
     * @param userId
     * @param content 第一条信息内容,用于生成初始会话
     * @param msgTime
     * @return
     */
    private Long  saveNewSession(Long userId, String content,LocalDateTime msgTime){
        TruncateUtil.Result titleInfo = TruncateUtil.truncate(content,50);
        String title = titleInfo.getText();
        if(titleInfo.isTruncated()){
            title+="...";
        }
        ChatAiSession chatAiSession = ChatAiSession.builder()
                .title(title) // 截取50个byte作为标题
                .lastMsgTime(msgTime)
                .userId(userId).build();
        chatAiSessionMapper.saveSession(chatAiSession);
        Long sessionId = chatAiSession.getId();
        // 节约一次后续的查询
        chatAiSessionRedis.setSession(sessionId, Map.of(
                COL_LAST_TIME,msgTime.toString(),
                COL_SEQ,"1"));
        return sessionId; // sessionId
    }

    /**
     * 添加ai事件到缓存
     * @param bag
     * @param status
     */
    private void saveAiAnswer(Long seq,Long userId,Long sessionId,ContentBag bag, Integer status){
        LocalDateTime msgTime = LocalDateTime.now();
        ChatAiAggDTO chatAiAggDTO = ChatAiAggDTO.builder()
                .seq(seq)
                .sender(ChatAiSenderEnum.AI.getType())
                .createTime(msgTime)
                .content(bag.content.toString())
                .sessionId(sessionId)
                .userId(userId)
                .token(bag.totalTokens)
                .finishReason(status).build();
        chatAiMsgRedis.removeMessage(sessionId); // 删除缓存
        chatAiRedis.aggregate(chatAiAggDTO);
    }

}

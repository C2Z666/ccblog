package com.ccblog.service.notice.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.notice.NoticeDetailDTO;
import com.ccblog.enumeration.notice.NoticeTypeEnum;
import com.ccblog.localCache.user.GlobalViewCache;
import com.ccblog.mapper.article.ArticleMapper;
import com.ccblog.mapper.chat.ChatUserMapper;
import com.ccblog.mapper.notice.NoticeContentMapper;
import com.ccblog.redis.notice.NoticeCountRedis;
import com.ccblog.redis.user.UserInfoRedis;
import com.ccblog.result.PageResult;
import com.ccblog.service.notice.NoticeService;
import com.ccblog.service.notice.helper.NoticeHelper;
import com.ccblog.utils.PageUtil;
import com.ccblog.vo.notice.NoticeVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ccblog.enumeration.user.FieldInfoEnum.COL_PHOTO;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_USER_NAME;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

/**
 * 通知服务实现
 * @author czc
 * @date 2025/10/19
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeContentMapper noticeContentMapper;

    @Autowired
    private NoticeCountRedis noticeCountRedis;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ChatUserMapper chatUserMapper;

    @Autowired
    private UserInfoRedis userInfoRedis;

    @Autowired
    private GlobalViewCache globalViewCache;

    @Autowired
    private NoticeHelper noticeHelper;

    /**
     * 获取消息
     * @param type 消息类型
     * @param currentPage 当前页
     * @param pageSize    页大小
     * @return
     */
    public NoticeVO getNotices(String type, Integer currentPage, Integer pageSize) {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        // 这样写防止分页被插队
        Integer typeCode = Integer.parseInt(String.valueOf(NoticeTypeEnum.typeOf(type).getType()));
        Page<NoticeDetailDTO> page = PageHelper.startPage(currentPage,pageSize)
                .doSelectPage(()->
                         noticeContentMapper.getNoticePageByType(userId,typeCode));

        // 查询redis获得用户信息
        // 先拼接需要的operateUserId
        Set<Long> operatorIds = page.getResult()
                .stream()
                .map(NoticeDetailDTO::getOperateUserId)
                .collect(Collectors.toSet());
        // 查询并补属性
        Map<Long,Map<String, String>> infoMap = userInfoRedis.getInfosBatch(operatorIds,List.of(COL_PHOTO,COL_USER_NAME));
        for(int i=0;i<page.getResult().size();i++){
            NoticeDetailDTO dto = page.getResult().get(i);
            Map<String, String> info = infoMap.get(dto.getOperateUserId());
            dto.setOperateUserPhoto(info.get(COL_PHOTO));
            dto.setOperateUserName(info.get(COL_USER_NAME));
        }
        List<NoticeDetailDTO> noticeDetailDTOS = page.getResult();
        IPage<NoticeDetailDTO> noticeDetailDTOIPage =
                PageUtil.toIPage(new PageResult<>(page.getTotal(),noticeDetailDTOS),currentPage,pageSize);

        // 未读消息计数
        Map<String,Integer> map = noticeCountRedis.getTotalCounts(userId, Arrays.stream(NoticeTypeEnum.COLS).toList());
        if(map.get(type)>0){
            // 清除已读
            map.put(type,0);
            noticeCountRedis.setCount(userId,Map.of(type,"0"));
            noticeCountRedis.aggregate(userId);
            // 失效全局视图缓存
            globalViewCache.evict(userId);
            // 同步数据库
            noticeContentMapper.clearReadStat(userId,typeCode);
        }
        // 下面处理差异化字段
        // 针对主体域为评论的需要增加扩展上下文(文章/专栏)
        if(type.equals(NoticeTypeEnum.COL_COMMENT)||type.equals(NoticeTypeEnum.COL_REPLY)||type.equals(NoticeTypeEnum.COL_LIKE)){
            noticeHelper.addContext(noticeDetailDTOS);
        }

        NoticeVO noticeVO = NoticeVO.builder()
                .unreadCountMap(map)
                .list(noticeDetailDTOIPage)
                .build();
        return noticeVO;
    }

    /**
     * 清除所有已读状态
     */
    public void clearAllUnread() {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        noticeCountRedis.setCount(userId, Arrays.stream(NoticeTypeEnum.COLS)
                                        .collect(toMap(identity(), k -> "0")));
        noticeContentMapper.clearReadStat(userId,null);
    }
}
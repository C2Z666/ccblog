package com.ccblog.config;

import com.ccblog.constant.RabbitMQConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static com.ccblog.constant.RabbitMQConstant.*;

/**
 * RabbitMQ的相关初始化
 * @author czc
 * @date 2025/10/9
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "rabbitmq.switchFlag", havingValue = "true") // 条件配置,只有开关打开才运行
public class RabbitMQConfig {
    /**
     * 发送端的配置序列化方式(有区别于redis的序列化器,防止互相干扰)
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("初始化RabbitMQ");
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 构造时直接注入 ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        template.setMessageConverter(converter); // 发布消息的消息转换器
        return template;
    }

    /**
     * 接收端的序列化,不能去掉!!!
     */
    @Bean
    public Jackson2JsonMessageConverter consumerJacksonConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }


    /* ============= 交换机 ============= */
    @Bean
    public TopicExchange userEventExchange() {
        return ExchangeBuilder.topicExchange(RabbitMQConstant.USER_EVENT_EX)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange articleEventExchange() {
        return ExchangeBuilder.topicExchange(RabbitMQConstant.ARTICLE_EVENT_EX)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange commentEventExchange() {
        return ExchangeBuilder.topicExchange(RabbitMQConstant.COMMENT_EVENT_EX)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange noticeEventExchange() {
        return ExchangeBuilder.topicExchange(RabbitMQConstant.NOTICE_EVENT_EX)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange chatEventExchange() {
        return ExchangeBuilder.topicExchange(RabbitMQConstant.CHAT_EVENT_EX)
                .durable(true)
                .build();
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EX).durable(true).build();
    }

    /* ============= 队列 ============= */
    /**
     * 通用队列模板
     * @param queueName
     * @return
     */
    private Queue buildQueue(String queueName,String deadExchangeName,String deadQueueName) {
        if(deadExchangeName==null)deadQueueName="";
        if(deadQueueName==null)deadQueueName="";
        return QueueBuilder.durable(queueName)
                .withArgument("x-max-retry", 3)          // 最多 3 次
//                .withArgument("x-message-ttl", 600_000)   // 不设置过期时间
                .withArgument("x-dead-letter-exchange", deadExchangeName)
                .withArgument("x-dead-letter-routing-key", deadQueueName)
                .build();
    }

    @Bean
    public Queue userCountQueue() { // 设置队列和配置死信队列
        return buildQueue(USER_COUNT_Q,DEAD_LETTER_EX,USER_DLQ);
    }

    @Bean
    public Queue userFollowQueue() { // 设置队列和配置死信队列
        return buildQueue(USER_FOLLOW_Q,DEAD_LETTER_EX,USER_DLQ);
    }

    @Bean
    public Queue articleCountQueue() { // 设置队列和配置死信队列
        return buildQueue(ARTICLE_COUNT_Q,DEAD_LETTER_EX,ARTICLE_DLQ);
    }

    @Bean
    public Queue articleInteractionQueue() { // 设置队列和配置死信队列
        return buildQueue(ARTICLE_INTERACTION_Q,DEAD_LETTER_EX,ARTICLE_DLQ);
    }

    @Bean
    public Queue commentCountQueue() { // 设置队列和配置死信队列
        return buildQueue(COMMENT_COUNT_Q,DEAD_LETTER_EX,COMMENT_DLQ);
    }

    @Bean
    public Queue commentInteractionQueue() { // 设置队列和配置死信队列
        return buildQueue(COMMENT_INTERACTION_Q,DEAD_LETTER_EX,COMMENT_DLQ);
    }

    @Bean
    public Queue noticeCountQueue() { // 设置队列和配置死信队列
        return buildQueue(NOTICE_COUNT_Q,DEAD_LETTER_EX,NOTICE_DLQ);
    }

    @Bean
    public Queue noticeContentQueue() { // 设置队列和配置死信队列
        return buildQueue(NOTICE_CONTENT_Q,DEAD_LETTER_EX,NOTICE_DLQ);
    }

    @Bean
    public Queue chatUserQueue() { // 设置队列和配置死信队列
        return buildQueue(CHAT_USER_Q,DEAD_LETTER_EX,CHAT_DLQ);
    }

    @Bean
    public Queue chatUserSessionQueue() { // 设置队列和配置死信队列
        return buildQueue(CHAT_USER_SESSION_Q,DEAD_LETTER_EX,CHAT_DLQ);
    }

    @Bean
    public Queue chatAIQueue() { // 设置队列和配置死信队列
        return buildQueue(CHAT_AI_Q,DEAD_LETTER_EX,CHAT_DLQ);
    }


    /* ============= 死信队列 ============= */
    @Bean
    public Queue userDlqQueue() {
        return QueueBuilder.durable(USER_DLQ).build();
    }

    @Bean
    public Queue artcileDlqQueue() {
        return QueueBuilder.durable(ARTICLE_DLQ).build();
    }

    @Bean
    public Queue commentDlqQueue() {
        return QueueBuilder.durable(COMMENT_DLQ).build();
    }

    @Bean
    public Queue noticeDlqQueue() {
        return QueueBuilder.durable(NOTICE_DLQ).build();
    }

    @Bean
    public Queue chatDlqQueue() {
        return QueueBuilder.durable(CHAT_DLQ).build();
    }

    /* ============= 绑定 ============= */

    /**
     * 通用绑定模板
     * @param queue   队列
     * @param exchang 交换机
     * @param key     route-key
     * @return
     */
    private Binding commentBinding(Queue queue, TopicExchange exchang,String key) {
        return BindingBuilder
                .bind(queue)
                .to(exchang)
                .with(key);
    }

    // 绑定普通队列和交换机
    @Bean
    public Binding userCountBinding(Queue userCountQueue, TopicExchange userEventExchange) {
        return BindingBuilder
                .bind(userCountQueue)
                .to(userEventExchange)
                .with(USER_COUNT_KEY);
    }

    @Bean
    public Binding userFollowBinding(Queue userFollowQueue, TopicExchange userEventExchange) {
        return BindingBuilder
                .bind(userFollowQueue)
                .to(userEventExchange)
                .with(USER_FOLLOW_KEY);
    }

    @Bean
    public Binding articleCountBinding(Queue articleCountQueue, TopicExchange articleEventExchange) {
        return BindingBuilder
                .bind(articleCountQueue)
                .to(articleEventExchange)
                .with(ARTICLE_COUNT_KEY);
    }

    @Bean
    public Binding articleInteractionBinding(Queue articleInteractionQueue, TopicExchange articleEventExchange) {
        return BindingBuilder
                .bind(articleInteractionQueue)
                .to(articleEventExchange)
                .with(ARTICLE_INTERACTION_KEY);
    }

    @Bean
    public Binding commentCountBinding(Queue commentCountQueue, TopicExchange commentEventExchange) {
        return BindingBuilder
                .bind(commentCountQueue)
                .to(commentEventExchange)
                .with(COMMENT_COUNT_KEY);
    }

    @Bean
    public Binding commentInteractionBinding(Queue commentInteractionQueue, TopicExchange commentEventExchange) {
        return BindingBuilder
                .bind(commentInteractionQueue)
                .to(commentEventExchange)
                .with(COMMENT_INTERACTION_KEY);
    }

    @Bean
    public Binding noticeCountBinding(Queue noticeCountQueue, TopicExchange noticeEventExchange) {
        return BindingBuilder
                .bind(noticeCountQueue)
                .to(noticeEventExchange)
                .with(NOTICE_COUNT_KEY);
    }

    @Bean
    public Binding noticeContentBinding(Queue noticeContentQueue, TopicExchange noticeEventExchange) {
        return BindingBuilder
                .bind(noticeContentQueue)
                .to(noticeEventExchange)
                .with(NOTICE_CONTENT_KEY);
    }

    @Bean
    public Binding chatUserBinding(Queue chatUserQueue, TopicExchange chatEventExchange) {
        return BindingBuilder
                .bind(chatUserQueue)
                .to(chatEventExchange)
                .with(CHAT_USER_KEY);
    }

    @Bean
    public Binding chatUserSessionBinding(Queue chatUserSessionQueue, TopicExchange chatEventExchange) {
        return BindingBuilder
                .bind(chatUserSessionQueue)
                .to(chatEventExchange)
                .with(CHAT_USER_SESSION_KEY);
    }

    @Bean
    public Binding chatAIBinding(Queue chatAIQueue, TopicExchange chatEventExchange) {
        return BindingBuilder
                .bind(chatAIQueue)
                .to(chatEventExchange)
                .with(CHAT_AI_KEY);
    }

    // 绑定死信队列和死信交换机
    @Bean
    public Binding userDlqBinding() {
        return BindingBuilder
                .bind(userDlqQueue())
                .to(deadLetterExchange())
                .with(USER_DLQ);
    }

    @Bean
    public Binding articleDlqBinding() {
        return BindingBuilder
                .bind(artcileDlqQueue())
                .to(deadLetterExchange())
                .with(ARTICLE_DLQ);
    }

    @Bean
    public Binding commentDlqBinding() {
        return BindingBuilder
                .bind(commentDlqQueue())
                .to(deadLetterExchange())
                .with(COMMENT_DLQ);
    }

    @Bean
    public Binding noticeDlqBinding() {
        return BindingBuilder
                .bind(noticeDlqQueue())
                .to(deadLetterExchange())
                .with(NOTICE_DLQ);
    }

    @Bean
    public Binding chatDlqBinding() {
        return BindingBuilder
                .bind(chatDlqQueue())
                .to(deadLetterExchange())
                .with(CHAT_DLQ);
    }
}
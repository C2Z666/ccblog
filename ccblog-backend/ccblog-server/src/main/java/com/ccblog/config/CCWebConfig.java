package com.ccblog.config;

import com.ccblog.interceptor.GlobalViewInterceptor;
import com.ccblog.utils.JsonUtil; //
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 注册xml解析器
 */
@Slf4j
@Configuration
public class CCWebConfig implements WebMvcConfigurer {
    @Value("${server-ip}")
    private String serverIp;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("配置swagger接口文档");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath*:/META-INF/resources/webjars/");
        /** 配置knife4j 显示文档 */
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        /**
         * 配置swagger-ui显示文档
         */
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        /** 公共部分内容 */
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 网络请求配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://"+serverIp) // 与前端一致
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)      // 记忆cookie?
                .maxAge(3600);
    }

//    配置swagger信息
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CC-blog")
                        .contact(new Contact())
                        .description("CC博客API文档")
                        .version("v1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("外部文档")
                        .url("https://springshop.wiki.github.org/docs"));
    }

    @Resource
    private GlobalViewInterceptor globalViewInterceptor;
    @Resource
    private AsyncHandlerInterceptor onlineUserStatisticInterceptor; // 关于人数统计,默认构造方式是使用caffeine缓存来统计在线人数

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...未完成");
        registry.addInterceptor(globalViewInterceptor).addPathPatterns("/**");
        registry.addInterceptor(onlineUserStatisticInterceptor).addPathPatterns("/**").excludePathPatterns("/test/**").excludePathPatterns("/subscribe");
    }


    /**
     * 配置序列化方式
     *
     * @param converters
     */
    @Override
//    org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport  在这个里面可以看到会根据配置加入很多初始的消息转换器
//    在里面如果jackson2XmlPresent=True,也会加入MappingJackson2XmlHttpMessageConverter
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2XmlHttpMessageConverter()); // 这一句报错
        converters.forEach(s -> {
            if (s instanceof MappingJackson2HttpMessageConverter) {
                // 长整型序列化返回时，更新为string，避免前端js精度丢失
                // 注意这个仅适用于json数据格式的返回，对于Thymeleaf的模板渲染依然会出现精度问题
                ((MappingJackson2HttpMessageConverter) s).getObjectMapper().registerModule(JsonUtil.bigIntToStrsimpleModule());
            }
        });
    }

    /**
     * 配置异步信息,方便SSE异步写入
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(mvcTaskExecutor());
    }

    public ThreadPoolTaskExecutor mvcTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);                // 核心线程数
        executor.setMaxPoolSize(64);                // 最大线程数
        executor.setQueueCapacity(2048);            // 队列长度
        executor.setThreadNamePrefix("mvc-async-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * fixme 返回数据类型的配置
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true)
                .defaultContentType(MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_OCTET_STREAM, MediaType.MULTIPART_FORM_DATA, MediaType.MULTIPART_MIXED, MediaType.MULTIPART_RELATED, MediaType.TEXT_HTML)
                // 当下面的配置为false（默认值）时，通过浏览器访问后端接口，会根据acceptHeader协商进行返回，返回结果都是xml格式；与我们日常习惯不太匹配
                // 因此禁用请求头的AcceptHeader，在需要进行xml交互的接口上，手动加上 consumer, produces 属性； 因为本项目中，只有微信的交互是采用的xml进行传参、返回，其他的是通过json进行交互，所以只在微信的 WxRestController 中需要特殊处理；其他的默认即可
                .ignoreAcceptHeader(true)
                .parameterName("mediaType")
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("text", MediaType.TEXT_PLAIN)
                .mediaType("text/event-stream", MediaType.TEXT_EVENT_STREAM)
                .mediaType("application/octet-stream", MediaType.APPLICATION_OCTET_STREAM)
                .mediaType("multipart/form-data", MediaType.MULTIPART_FORM_DATA)
        ;
    }
}

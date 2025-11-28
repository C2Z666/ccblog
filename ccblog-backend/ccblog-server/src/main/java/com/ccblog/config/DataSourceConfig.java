package com.ccblog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 表初始化，只有首次启动时，才会执行
 * 使用 Flyway
 *
 * @author czc
 * @date 2025-09-24
 */
@Slf4j
@Configuration
@Profile("dev")
public class DataSourceConfig {
    // 把yml配置的值直接给数据,'.'的意思是层级
    @Value("${database.name}")
    private String database;

    @Value("${spring.flyway.enabled:true}")
    private Boolean flywayEnable;

    @Value("${spring.flyway.locations}")
    private String locations; // sql文件地址

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        log.info("数据库初始化准备...");
        // 这个dataSource是Spring自动找到@Bean里面的dataSource然后调用
        final org.springframework.jdbc.datasource.init.DataSourceInitializer initializer = new org.springframework.jdbc.datasource.init.DataSourceInitializer();
        // 设置数据源
        initializer.setDataSource(dataSource);
        boolean enable = needInit(dataSource); // 不存在数据库那么初始化
        initializer.setEnabled(enable);
        return initializer;
    }

    /**
     * 检测一下数据库中表是否存在，若存在则不初始化；否则基于 schema-all.sql 进行初始化表
     *
     * @param dataSource
     * @return true 表示需要初始化； false 表示无需初始化
     */
    private boolean needInit(DataSource dataSource) {
//        if (autoInitDatabase()) {
//            return true;
//        }
        // 根据是否存在表来判断是否需要执行sql操作
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        if (!flywayEnable) {
            // 根据用户来判断是否有初始化
            List list = jdbcTemplate.queryForList("SELECT table_name FROM information_schema.TABLES where table_name = 'user_info' and table_schema = '" + database + "';");
            return CollectionUtils.isEmpty(list);
        }

        List<Map<String, Object>> record = jdbcTemplate.queryForList("select * from flyway_schema_history limit 1;");
        if (CollectionUtils.isEmpty(record)) {
            // 首次启动，需要初始化库表，直接返回
            return true;
        }
        return false;
    }

}

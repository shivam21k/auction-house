package com.auctionhouse.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SchemaHotfixConfig {

    private static final Logger log = LoggerFactory.getLogger(SchemaHotfixConfig.class);

    @Bean
    public CommandLineRunner enumColumnHotfixRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            apply(jdbcTemplate, "ALTER TABLE auctions MODIFY COLUMN status VARCHAR(64) NOT NULL");
            apply(jdbcTemplate, "ALTER TABLE products MODIFY COLUMN status VARCHAR(64) NOT NULL");
        };
    }

    private void apply(JdbcTemplate jdbcTemplate, String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception ex) {
            log.debug("Schema hotfix skipped for SQL [{}]: {}", sql, ex.getMessage());
        }
    }
}

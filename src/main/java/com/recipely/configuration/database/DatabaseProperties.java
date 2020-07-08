package com.recipely.configuration.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jdbc")
@EnableConfigurationProperties
@NoArgsConstructor
public class DatabaseProperties {
    private String driver;
    private String url;
    private String username;
    private String password;
    private HibernateConfig hibernate;

    @Data
    static class HibernateConfig {
        private String dialect;
        private Boolean showSQL;
        private Boolean formatSQL;
        private String packagesToScan;
    }
}

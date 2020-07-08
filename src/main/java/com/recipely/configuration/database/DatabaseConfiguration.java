package com.recipely.configuration.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:properties/persistence-mysql-prod.properties")
public class DatabaseConfiguration {

    private final DatabaseProperties databaseProperty;

    @Autowired
    public DatabaseConfiguration(DatabaseProperties databaseProperty) {
        this.databaseProperty = databaseProperty;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(databaseProperty.getDriver())
                .url(databaseProperty.getUrl())
                .username(databaseProperty.getUsername())
                .password(databaseProperty.getPassword())
                .build();
    }

    private Properties setJpaProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", databaseProperty.getHibernate().getDialect());
        props.setProperty("hibernate.show_sql", databaseProperty.getHibernate().getShowSQL().toString());
        props.setProperty("hibernate.format_sql", databaseProperty.getHibernate().getFormatSQL().toString());
        return props;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan(databaseProperty.getHibernate().getPackagesToScan());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(setJpaProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}

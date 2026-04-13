package com.jsp.AutomationEngine.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
        public DataSource dataSource() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setUsername(System.getenv("DB_USERNAME"));
            hikariConfig.setPassword(System.getenv("DB_PASSWORD"));
            hikariConfig.setJdbcUrl(System.getenv("DB_URL"));
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            return new HikariDataSource(hikariConfig);

        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

            LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

            emf.setDataSource(dataSource());
            emf.setPackagesToScan("com.jsp.AutomationEngine");
            emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            Map<String, Object> props = new HashMap<>();
            props.put("hibernate.hbm2ddl.auto", "update");
            props.put("hibernate.show_sql", true);

            emf.setJpaPropertyMap(props);
            return emf;
        }

        @Bean
        public JpaTransactionManager transactionManager() {
            JpaTransactionManager jtm = new JpaTransactionManager();
            jtm.setEntityManagerFactory(entityManagerFactory().getObject());

            return jtm;

        }



    }




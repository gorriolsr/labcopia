package com.tecnocampus.outlaws.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {


    @Bean
    public JdbcClient jdbcClient(DataSource ds) {
        return JdbcClient.create(ds);
    }

}

package com.example.batch.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AplicationConfig {

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //dataSource.setUrl("jdbc:mysql://localhost:3306/batch_database");// Utilizar sin Docker
        dataSource.setUrl("jdbc:mysql://host.docker.internal:3306/proceduresDB");//Utilizar con dokcer
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;
    }
}

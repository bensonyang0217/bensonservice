package com.benson.bensonservice;

import com.benson.bensonservice.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class BensonserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BensonserviceApplication.class, args);
    }

}

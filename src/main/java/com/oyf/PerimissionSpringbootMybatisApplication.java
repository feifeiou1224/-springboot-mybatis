package com.oyf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.oyf.*"})
public class PerimissionSpringbootMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerimissionSpringbootMybatisApplication.class, args);
    }

}

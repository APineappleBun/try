package com.example.orienteering;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.orienteering.mapper")
public class OrienteeringApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrienteeringApplication.class, args);
    }

}

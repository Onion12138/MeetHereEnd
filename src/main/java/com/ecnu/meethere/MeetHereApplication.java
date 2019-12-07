package com.ecnu.meethere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MeetHereApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetHereApplication.class, args);
    }

}

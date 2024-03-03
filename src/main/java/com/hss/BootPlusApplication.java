package com.hss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  //开启定时任务注解功能
public class BootPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootPlusApplication.class, args);
    }

}

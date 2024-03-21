package com.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.data.mapper")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("数据工厂启动！\n" +
                "    .___       __                     _____               __                       \n" +
                "  __| _/____ _/  |______            _/ ____\\____    _____/  |_  ___________ ___.__.\n" +
                " / __ |\\__  \\\\   __\\__  \\    ______ \\   __\\\\__  \\ _/ ___\\   __\\/  _ \\_  __ <   |  |\n" +
                "/ /_/ | / __ \\|  |  / __ \\_ /_____/  |  |   / __ \\\\  \\___|  | (  <_> )  | \\/\\___  |\n" +
                "\\____ |(____  /__| (____  /          |__|  (____  /\\___  >__|  \\____/|__|   / ____|\n" +
                "     \\/     \\/          \\/                      \\/     \\/                   \\/     ");
    }

}

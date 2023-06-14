package com.bitarrded.youtube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class YoutubeApplication {
    public static void main(String[] args) {
        SpringApplication.run(YoutubeApplication.class, args);


    }
}



package com.translation.translationservicemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TranslationServiceMgmtApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranslationServiceMgmtApplication.class, args);
    }

}

package com.iscool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.naming.NamingException;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class FileSharerApplication {


    public static void main(String[] args) throws NamingException {
		SpringApplication.run(FileSharerApplication.class, args);
    }




}

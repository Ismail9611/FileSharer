package com.iscool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileSharerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileSharerApplication.class, args);
	}

}

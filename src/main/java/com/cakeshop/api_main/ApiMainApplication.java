package com.cakeshop.api_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMainApplication.class, args);
    }

//	@Bean
//	public CommandLineRunner initData(INationRepository nationRepository) {
//		return args -> {
//
//			System.out.println("Nation data for TP Hồ Chí Minh initialized successfully!");
//		};
//	}
}
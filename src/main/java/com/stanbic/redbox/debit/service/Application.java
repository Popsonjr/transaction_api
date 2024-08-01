package com.stanbic.redbox.debit.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class Application {

//	@SpringBootApplication
//	@EnableTransactionManagement
	public static void main(String[] args) {
//		System.out.println("dhjagh");
		SpringApplication.run(Application.class, args);
	}
}

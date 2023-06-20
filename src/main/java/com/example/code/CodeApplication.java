package com.example.code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.example.code.DAO")
public class CodeApplication {
	public static void main(String[] args) {
		SpringApplication.run(CodeApplication.class, args);
	}
}
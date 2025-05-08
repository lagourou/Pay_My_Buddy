package com.pay_my_buddy.OC_P6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "com.pay_my_buddy.OC_P6")
public class OcP6Application {

	public static void main(String[] args) {
		SpringApplication.run(OcP6Application.class, args);
	}

	@Autowired
	private ApplicationContext context;

	@PostConstruct
	public void checkBeans() {
		System.out.println("UserMapper bean exists: " + context.containsBean("userMapper"));
	}

}

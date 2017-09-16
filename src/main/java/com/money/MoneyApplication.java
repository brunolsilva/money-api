package com.money;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.money.config.property.MoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(MoneyApiProperty.class)
public class MoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyApplication.class, args);
	}
}

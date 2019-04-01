package com.hquery.hrpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(
        scanBasePackages = {"com.hquery.hrpc"}
)
public class HrpcApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(HrpcApplication.class, args);
		run.registerShutdownHook();
	}
}

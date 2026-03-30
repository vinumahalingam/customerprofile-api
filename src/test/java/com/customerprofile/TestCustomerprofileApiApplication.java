package com.customerprofile;

import org.springframework.boot.SpringApplication;

public class TestCustomerprofileApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(CustomerprofileApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

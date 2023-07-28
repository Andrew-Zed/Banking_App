package com.andrew.peoplesBank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Peoples Bank",
				description = "Backend Rest APIs for The Peoples Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Andrew Awadike",
						email = "andrew.awadyke@gmail.com",
						url = "https://github.com/Andrew-Zed/Banking_App"
				),
				license = @License(
						name = "The Peoples Bank",
						url = "https://github.com/Andrew-Zed/Banking_App"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The Peoples Bank Application Documentation",
				url = "https://github.com/Andrew-Zed/Banking_App"
		)
)
public class PeoplesBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeoplesBankApplication.class, args);
	}

}

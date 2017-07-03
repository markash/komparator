package io.threesixty.kt.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.threesixty.ui.annotation.EnableThreeSixtyComponents;

@EnableThreeSixtyComponents
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
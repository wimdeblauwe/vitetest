package com.example.vitetest;

import com.example.vitetest.infrastructure.vite.ViteServerConfigurationPropertiesContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ViteTestApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ViteTestApplication.class)
				.initializers(new ViteServerConfigurationPropertiesContextInitializer())
				.run(args);
	}

}

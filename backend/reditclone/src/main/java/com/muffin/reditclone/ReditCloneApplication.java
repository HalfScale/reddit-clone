package com.muffin.reditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.muffin.reditclone.config.SwaggerConfiguration;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class ReditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReditCloneApplication.class, args);
	}

}

package com.home.csconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class CsConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsConfigServerApplication.class, args);
	}

}

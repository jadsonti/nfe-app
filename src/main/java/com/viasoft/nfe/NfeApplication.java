package com.viasoft.nfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("com.viasoft.nfe.model")
public class NfeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NfeApplication.class, args);
	}

}

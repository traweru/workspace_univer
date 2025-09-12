package dev.vortsu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VortsuApplication implements CommandLineRunner {

	@Autowired
	private Initializer initializer;

	public static void main(String[] args) {
		SpringApplication.run(VortsuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initializer.initial();
	}
}
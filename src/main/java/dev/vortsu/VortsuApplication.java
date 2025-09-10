package dev.vortsu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VortsuApplication {

	private static Initializer initiator;
	@Autowired
	public void setInitiatorLoader(Initializer initiator){
		VortsuApplication.initiator = initiator;
	}
	public static void main(String[] args) {
		SpringApplication.run(VortsuApplication.class, args);
		initiator.initial();
	}

}

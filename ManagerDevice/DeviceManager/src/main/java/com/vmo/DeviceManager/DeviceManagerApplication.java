package com.vmo.DeviceManager;

import com.vmo.DeviceManager.models.Apartment;
import com.vmo.DeviceManager.models.Erole;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DeviceManagerApplication implements CommandLineRunner {
	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(DeviceManagerApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByRole(Erole.ROLE_ADMIN);
		if(adminAccount == null){
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstName("admin");
			user.setLastName("admin");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setRole(Erole.ROLE_ADMIN);
			 userRepository.save(user);

		}
	}
}

package com.vmo.DeviceManager;

import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
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
	@Autowired
	DepartmentRepository departmentRepository;

	public static void main(String[] args) {
		SpringApplication.run(DeviceManagerApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByRole(Erole.ADMIN);
		if(adminAccount == null){
			Department department = new Department();
			department.setDepartmentName("DU18");
			department.setAddress("abc");
			departmentRepository.save(department);

			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstName("admin");
			user.setLastName("admin");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setDepartment(department);
			user.setRole(Erole.ADMIN);
			 userRepository.save(user);

		}
	}
}

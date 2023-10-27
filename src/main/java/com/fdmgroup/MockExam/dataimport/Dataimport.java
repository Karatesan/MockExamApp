package com.fdmgroup.MockExam.dataimport;


import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fdmgroup.MockExam.model.MailInformation;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.UserRepository;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class Dataimport implements ApplicationRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		List<User> users = userRepository.findAll();
		
		if(users.isEmpty()) {
			MailInformation info = new MailInformation();
			User admin = User.builder().firstName("Admin").lastName("Adminovsky")
					.email(info.getEmail()).password(passwordEncoder.encode("Admin.123")).role(Role.ADMIN)
					.build();
			admin.setVerified(true);
			userRepository.save(admin);
		}
}
}



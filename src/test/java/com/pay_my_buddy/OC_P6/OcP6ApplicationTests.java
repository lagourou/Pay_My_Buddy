package com.pay_my_buddy.OC_P6;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pay_my_buddy.OC_P6.configuration.SecurityConfig;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

@SpringBootTest(classes = { OcP6Application.class,
		SecurityConfig.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OcP6ApplicationTests {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void checkBeans() {
		System.out.println("Beans chargés : " + context.getBeanDefinitionCount());
		System.out.println("UserMapper présent ? " + context.containsBean("userMapper"));
	}

	@Test
	void testUserRegistration() {
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword(passwordEncoder.encode("password123"));
		user.setUsername("TestUser");
		user.setAccountBalance(BigDecimal.valueOf(0.00));

		userRepository.deleteAll();
		userRepository.save(user);

		assertTrue(userRepository.findByEmail("test@example.com").isPresent());
	}

}

package com.hj.spring.config;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hj.spring.accounts.Account;
import com.hj.spring.accounts.AccountRole;
import com.hj.spring.accounts.AccountService;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			
			@Autowired
			AccountService accountService;
			
			@Override
			public void run(ApplicationArguments args) throws Exception {
				Account admin = Account.builder()
					.email("khj@naver.com")
					.password("admin")
					.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
					.build();
				
				accountService.saveAccount(admin);
			}
		};
		
	}
}
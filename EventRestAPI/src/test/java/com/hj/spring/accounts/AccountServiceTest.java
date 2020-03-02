package com.hj.spring.accounts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AccountServiceTest {

	@Autowired
	AccountService accountService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Test
	public void findByUsername() {
		
		// Given
		String password = "1234";
		String username = "khj@naver.com";
		Account account = Account.builder()
				.email(username)
				.password(password)
				.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
				.build();
		
		accountRepository.save(account);
		
		// When
		UserDetailsService userDetailsService = accountService;
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		// Then
		assertThat(userDetails.getPassword()).isEqualTo(password);
	}
	
	@Test
	public void findByUsernameFail() {
		String username = "random@naver.com";
		try {
			accountService.loadUserByUsername(username);
			//test 실패
			fail("supposed to be fail");
		} catch (UsernameNotFoundException e) {
			assertThat(e.getMessage()).containsSequence(username);
		}
	}
}
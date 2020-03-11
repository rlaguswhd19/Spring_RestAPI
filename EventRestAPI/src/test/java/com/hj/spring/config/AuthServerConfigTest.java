package com.hj.spring.config;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hj.spring.accounts.Account;
import com.hj.spring.accounts.AccountRole;
import com.hj.spring.accounts.AccountService;
import com.hj.spring.common.BaseControllerTest;
import com.hj.spring.common.TestDescription;

public class AuthServerConfigTest extends BaseControllerTest{
	
	@Autowired
	AccountService accountService;
	
	@Test
	@TestDescription("인증 토큰을 발급 받는 테스트")
	public void getAuthToken() throws Exception {
		// Given
		String username = "rlaguswhd19@naver.com";
		String password = "1234";
		
		Account khj = Account.builder()
				.email(username)
				.password(password)
				.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
				.build();
		
		this.accountService.saveAccount(khj);
		
		String clientId = "myApp";
		String clientSecret = "pass";
		
		this.mockMvc.perform(post("/oauth/token")
			.with(httpBasic(clientId, clientSecret))	
			.param("username", username)
			.param("password", password)
			.param("grant_type", "password")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("access_token").exists())
			;
	}	
}

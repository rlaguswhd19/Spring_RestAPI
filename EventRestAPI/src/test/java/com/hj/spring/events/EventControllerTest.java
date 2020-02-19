package com.hj.spring.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {
	
	@Autowired
	MockMvc mocMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void createEvent() throws Exception {
		Event event = Event.builder()
				.name("Srping")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 02, 19, 14, 19))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 02, 20, 14, 19))
				.beginEventDateTime(LocalDateTime.of(2020, 02, 21, 14, 19))
				.endEventDateTime(LocalDateTime.of(2020, 02, 22, 14, 19))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("대전 현지의 러브하우스")
				.build();
				
		mocMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event))
				) 
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists());
	}
}
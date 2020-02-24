package com.hj.spring.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

import org.junit.jupiter.api.Test;

public class EventTest {
	
	@Test
	public void builder() {
		Event event = Event.builder()
				.name("KHJ")
				.description("REST API")
				.build();
		assertThat(event).isNotNull();
	}
	
	@Test
	public void javaBean() {
		String name = "Event";
		String description = "Spring";
		
		Event event = new Event();
		event.setName("Event");
		event.setDescription("Spring");
		
		assertThat(event.getName()).isEqualTo(name);
		assertThat(event.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void testFree() {
		//Given
		Event event = Event.builder()
				.basePrice(0)
				.maxPrice(0)
				.build();
		
		//When
		event.update();
		
		//Then
		assertThat(event.isFree()).isTrue();
		
		//Given
		event = Event.builder()
				.basePrice(100)
				.maxPrice(0)
				.build();
		//When
		event.update();
				
		//Then
		assertThat(event.isFree()).isFalse();
		
		//Given
		event = Event.builder()
				.basePrice(0)
				.maxPrice(100)
				.build();
		//When
		event.update();
				
		//Then
		assertThat(event.isFree()).isFalse();
	}
	
	@Test
	public void testOffline() {
		//Given
		Event event = Event.builder()
				.location("대전의 현지 러브 하우스")
				.build();
		
		//When
		event.update();
		
		//Then
		assertThat(event.isOffline()).isTrue();
		
		//Given
		event = Event.builder()
				.build();
		
		//When
		event.update();
		
		//Then
		assertThat(event.isOffline()).isFalse();
	}
}

package com.hj.spring.events;

import static org.assertj.core.api.Assertions.assertThat;

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
}

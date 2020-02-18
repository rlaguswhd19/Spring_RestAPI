package com.hj.spring.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EventTest {
	
	@Test
	public void builder() {
		Event event = Event.builder().build();
		assertThat(event).isNotNull();
	}
}

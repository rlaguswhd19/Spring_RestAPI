package com.hj.spring.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
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
	@Parameters(method = "parametersForTestFree")
	public void testFree(int basePrice, int maxPrice, boolean isFree) {
		//Given
		Event event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		
		//When
		event.update();
		
		//Then
		assertThat(event.isFree()).isEqualTo(isFree);
	}
	
	private Object[] parametersForTestFree() {
		return new Object[] {
				new Object[] {0, 0, true},
				new Object[] {100, 0 ,false},
				new Object[] {0, 100 ,false},
				new Object[] {100, 200 ,false},
		};
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

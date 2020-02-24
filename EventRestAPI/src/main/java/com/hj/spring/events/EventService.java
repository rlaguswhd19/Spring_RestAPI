package com.hj.spring.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ResponseEntity createEvent(Event event) {
		
		// free update
		event.update();
		
		// db
		Event newEvent = this.eventRepository.save(event);
		URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
		return ResponseEntity.created(createUri).body(newEvent);
	}
}

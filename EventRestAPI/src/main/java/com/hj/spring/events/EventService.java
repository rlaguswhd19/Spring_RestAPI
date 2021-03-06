package com.hj.spring.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.hj.spring.accounts.Account;
import com.hj.spring.accounts.AccountAdapter;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ResponseEntity createEvent(Event event) {
		
		// event update
		event.update();
		
		// db
		Event newEvent = eventRepository.save(event);
		
		WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
		URI createdUri = selfLinkBuilder.toUri();
		EventResource eventResource = new EventResource(newEvent);
		
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
		return ResponseEntity.created(createdUri).body(eventResource);
	}

	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler, Account account) {
		Page<Event> page = this.eventRepository.findAll(pageable);
		var pageResource = assembler.toModel(page, e -> new EventResource(e));
		pageResource.add(new Link("/docs/index.html#resources-events-querys").withRel("profile"));
		
 		if(account != null) {
			pageResource.add(linkTo(EventController.class).withRel("create-event"));
		}
		return ResponseEntity.ok(pageResource);
	}

	public ResponseEntity getEvent(Integer id, Account account) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
		
		if(event.getManager().equals(account)) {
			eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
		}
		
		eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
		
		return ResponseEntity.ok(eventResource);
	}

	public ResponseEntity updateEvent(Integer id, EventDto eventDto, Account account) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Event event = optionalEvent.get();
		
		if(!event.getManager().equals(account)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		
		// 덮어 씌우기
		this.modelMapper.map(eventDto, event);
		Event saveEvent = this.eventRepository.save(event);
		
		EventResource eventResource = new EventResource(saveEvent);
		eventResource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));
		
		return ResponseEntity.ok(eventResource);
	}
}

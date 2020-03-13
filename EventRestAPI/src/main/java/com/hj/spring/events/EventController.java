package com.hj.spring.events;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hj.spring.accounts.Account;
import com.hj.spring.accounts.CurrentUser;
import com.hj.spring.common.ErrorsResource;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	@Autowired
	private EventService eventSerivce;

	@Autowired
	private EventValidator eventValidator;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/")
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors, @CurrentUser Account account) {
		
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		eventValidator.validate(eventDto, errors);

		if (errors.hasErrors()) {
			return badRequest(errors);
		}
		if(account == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		
		Event event = modelMapper.map(eventDto, Event.class);
		event.setManager(account);

		return eventSerivce.createEvent(event);
	}

	@GetMapping("/")
	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler, @CurrentUser Account
			account) {
		return this.eventSerivce.queryEvents(pageable, assembler, account);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id, @CurrentUser Account account) {
		return this.eventSerivce.getEvent(id, account);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors, @CurrentUser Account account) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		return this.eventSerivce.updateEvent(id, eventDto, account);
	}
	
	public ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
}

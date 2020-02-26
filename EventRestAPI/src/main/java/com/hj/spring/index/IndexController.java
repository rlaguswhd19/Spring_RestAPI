package com.hj.spring.index;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hj.spring.events.EventController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@RestController
public class IndexController {

	@GetMapping("/api")
	public RepresentationModel index() {
		var index = new RepresentationModel();
		index.add(linkTo(EventController.class).withRel("events"));
		return index;
	}
}

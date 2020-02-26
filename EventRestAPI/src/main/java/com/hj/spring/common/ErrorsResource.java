package com.hj.spring.common;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import com.hj.spring.index.IndexController;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.*;
public class ErrorsResource extends EntityModel<Errors> {
	
	public ErrorsResource(Errors errors, Link... links) {
		super(errors, links);
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
	}
}

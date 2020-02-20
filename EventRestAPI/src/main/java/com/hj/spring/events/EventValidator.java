package com.hj.spring.events;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

	public void validate(EventDto eventDto, Errors errors) {
		if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
			errors.rejectValue("basePrice", "Wrong Value", "BasePrice is wrong");
			errors.rejectValue("maxPrice", "Wrong Value", "MaxPrice is wrong");
		}
		
		LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
		if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
			endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
			endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
			
			errors.rejectValue("endEventDateTime", "Wrong Value", "endEventDateTime is wrong");
		}
		
		//TODO BeginEventDateTime
		//TODO CloseEnrollmentDateTime
	}
}

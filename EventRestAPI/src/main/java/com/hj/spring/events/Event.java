package com.hj.spring.events;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hj.spring.accounts.Account;
import com.hj.spring.accounts.AccountSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id") //Equals랑 Hashcode를 비교할때  id만 사용한다.
@Entity
public class Event {
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private String description;
	private LocalDateTime beginEnrollmentDateTime;
	private LocalDateTime closeEnrollmentDateTime;
	private LocalDateTime beginEventDateTime;
	private LocalDateTime endEventDateTime;
	private String location; // (optional) 이게 없으면 온라인 모임
	private int basePrice; // (optional)
	private int maxPrice; // (optional)
	private int limitOfEnrollment;
	private boolean offline;
	private boolean free;
	@Enumerated(EnumType.STRING) //숫자값으로 저장되는데 이넘 순서를 바꾸면 데이터가 꼬이기 때문에 string으로 쓴다.
	private EventStatus eventStatus = EventStatus.DRAFT;
	@ManyToOne
	@JsonSerialize(using = AccountSerializer.class)
	private Account manager;
	
	public void update() {
		// Update Free
		if(this.basePrice == 0 && this.maxPrice == 0) {
			this.free = true;
		} else {
			this.free = false;
		}
		
		//Update Offline
		if(this.location == null || this.location.isBlank()) {
			this.offline = false;
		}else {
			this.offline = true;
		}
	}

}

package com.hj.spring.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import com.hj.spring.accounts.Account;
import com.hj.spring.accounts.AccountRepository;
import com.hj.spring.accounts.AccountRole;
import com.hj.spring.accounts.AccountService;
import com.hj.spring.common.AppProperties;
import com.hj.spring.common.BaseControllerTest;
import com.hj.spring.common.TestDescription;

public class EventControllerTest  extends BaseControllerTest{
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AccountRepository accountRepository;
		
	@Autowired
	AppProperties appProperties;
	
	@Before
	public void setUp() {
		accountRepository.deleteAll();
		eventRepository.deleteAll();
	}
	
	@Test
	@TestDescription("정상적으로 이벤트를 생성하는 테스트")
	public void createEvent() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Srping")
				.description("Spring API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 02, 19, 14, 19))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 02, 20, 14, 19))
				.beginEventDateTime(LocalDateTime.of(2020, 02, 21, 14, 19))
				.endEventDateTime(LocalDateTime.of(2020, 02, 22, 14, 19))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("Daejeon")
				.build();
		
		mockMvc.perform(post("/api/events/")
				.header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				) 
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists())
			.andExpect(header().exists(HttpHeaders.LOCATION))
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
			.andExpect(jsonPath("free").value(false))
			.andExpect(jsonPath("offline").value(true))
			.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.query-events").exists())
			.andExpect(jsonPath("_links.update-event").exists())
			.andDo(document("create-event", 
					links(
							linkWithRel("self").description("link to self"),
							linkWithRel("query-events").description("link to query-events"),
							linkWithRel("update-event").description("link to update an existing"),
							linkWithRel("profile").description("link to profile")
					),
					requestHeaders(
							headerWithName(HttpHeaders.ACCEPT).description("accept header"),
							headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
					),
					requestFields(
							fieldWithPath("name").description("Name of new event"),
							fieldWithPath("description").description("Description of new event"),
							fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
							fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
							fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
							fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
							fieldWithPath("location").description("Location of new event"),
							fieldWithPath("basePrice").description("BasePrice of new event"),
							fieldWithPath("maxPrice").description("MaxPrice of new event"),
							fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event")
					),
					responseHeaders(
							headerWithName(HttpHeaders.LOCATION).description("location header"),
							headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
					),
					//requestFields를 사용하면 links가 걸린다. 문서화하지 않았기 때문이다. 하지만 relaxed를 사용하면 모든것을 문서화하지 않아도됀다.
					//단점은 정확한 문서를 만들지 못한다는거다.
					//api가 바꼇을때 모든것을 다 하지않으면 api문서화가 제대로 되지 않는다.
					relaxedResponseFields(
							fieldWithPath("id").description("Id of new event"),
							fieldWithPath("name").description("Name of new event"),
							fieldWithPath("description").description("Description of new event"),
							fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
							fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
							fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
							fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
							fieldWithPath("location").description("Location of new event"),
							fieldWithPath("basePrice").description("BasePrice of new event"),
							fieldWithPath("maxPrice").description("MaxPrice of new event"),
							fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event"),
							fieldWithPath("free").description("Free of new event"),
							fieldWithPath("offline").description("Offline of new event"),
							fieldWithPath("eventStatus").description("EventStatus of new event"),
							fieldWithPath("_links.self.href").description("link to self"),
							fieldWithPath("_links.query-events.href").description("link to query-event list"),
							fieldWithPath("_links.update-event.href").description("link to update existing event"),
							fieldWithPath("_links.profile.href").description("link to profile")
							
					)
			))
			;
	}
	
	@Test
	@TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Unknown_Input() throws Exception {
		Event event = Event.builder()
				.id(100)
				.name("Srping")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 02, 19, 14, 19))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 02, 20, 14, 19))
				.beginEventDateTime(LocalDateTime.of(2020, 02, 21, 14, 19))
				.endEventDateTime(LocalDateTime.of(2020, 02, 22, 14, 19))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("대전 현지의 러브하우스")
				.eventStatus(EventStatus.PUBLISHED)
				.free(true)
				.offline(false)
				.build();
		
		mockMvc.perform(post("/api/events/")
				.header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event))
				) 
			.andDo(print())
			.andExpect(status().isBadRequest())
			;
			
	}
	
	@Test
	@TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Empty_Input() throws Exception {
		EventDto eventDto = EventDto.builder().build();
		
		mockMvc.perform(post("/api/events/")
				.header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				)
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andExpect(jsonPath("_links.index").exists())
				;
	}
	
	@Test
	@TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Wrong_Input() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Srping")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 02, 22, 14, 19))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 02, 21, 14, 19))
				.beginEventDateTime(LocalDateTime.of(2020, 02, 20, 14, 19))
				.endEventDateTime(LocalDateTime.of(2020, 02, 19, 14, 19))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("대전 현지의 러브하우스")
				.build();
		
		mockMvc.perform(post("/api/events/")
				.header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("content.[0].objectName").exists())
				.andExpect(jsonPath("content.[0].defaultMessage").exists())
				.andExpect(jsonPath("content.[0].code").exists())
				.andExpect(jsonPath("_links.index").exists())
				;
	}
	
	@Test
	@TestDescription("30개의 이벤트를 10개씩 조회하는데 11~20 조회하기")
	public void queryEvent() throws Exception{
		// Given
		IntStream.range(1, 31).forEach(i -> {
			this.generatedEvent(i);
		});
		
		this.mockMvc.perform(get("/api/events/")
				.param("page", "1")
				.param("size", "10")
				.param("sort", "id,DESC")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("page").exists())
			.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.profile").exists())
			.andDo(document("query-events"))
			;
	}
	
	@Test
	@TestDescription("기존의 이벤트 하나 조회하기")
	public void getEvent() throws Exception {
		// Given
		Event event = this.generatedEvent(100);
		
		// When
		this.mockMvc.perform(get("/api/events/"+event.getId()))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("id").exists())
			.andExpect(jsonPath("name").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.profile").exists())
			.andDo(document("get-an-event"))
			;
	}
	
	@Test
	@TestDescription("없는 이벤트를 조회했을때 404응답받기")
	public void getEvent404() throws Exception {
		// When
		this.mockMvc.perform(get("/api/events/123"))
			.andExpect(status().isNotFound())
			;
	}
	
	@Test
	@TestDescription("이벤트를 정상적으로 수정하기")
	public void updateEvent() throws Exception {
		// Given
		Event event = this.generatedEvent(200);
		
		EventDto eventDto = modelMapper.map(event, EventDto.class);
		String eventName = "Update event";
		eventDto.setName(eventName);
		
		// When
		this.mockMvc.perform(put("/api/events/{id}",event.getId())
					.header(HttpHeaders.AUTHORIZATION, getBearerToken())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objectMapper.writeValueAsString(eventDto))
					.accept(MediaTypes.HAL_JSON)
				)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(eventName))
			.andExpect(jsonPath("_links.self").exists())
			.andDo(document("update-event"))
			;
	}
	
	@Test
	@TestDescription("입력값이 비어있는 경우 이벤트 수정 실패")
	public void updateEvent_400_Empty() throws Exception {
		//Given
		Event event = this.generatedEvent(200);
		EventDto eventDto = new EventDto();
		
		this.mockMvc.perform(put("/api/events/{id}",event.getId())
				.header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(eventDto))
				.accept(MediaTypes.HAL_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	@TestDescription("입력값이 잘못된 경우 이벤트 수정 실패")
	public void updateEvent_400_Wrong() throws Exception {
		//Given
		Event event = this.generatedEvent(200);
		EventDto eventDto = modelMapper.map(event, EventDto.class);
		eventDto.setBasePrice(20000);
		eventDto.setMaxPrice(10000);
		
		this.mockMvc.perform(put("/api/events/{id}",event.getId())
				.header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(eventDto))
				.accept(MediaTypes.HAL_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	@TestDescription("존재하지 않는 이벤트 수정")
	public void updateEvent_404() throws Exception {
		//Given
		Event event = this.generatedEvent(200);
		EventDto eventDto = modelMapper.map(event, EventDto.class);
		
		this.mockMvc.perform(put("/api/events/12341234")
				.header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(eventDto))
				.accept(MediaTypes.HAL_JSON)
			)
			.andDo(print())
			.andExpect(status().isNotFound())
		;
	}
	
	private Event generatedEvent(int i) {
		Event event = Event.builder()
				.name("Srping")
				.description("Spring API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 02, 19, 14, 19))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 02, 20, 14, 19))
				.beginEventDateTime(LocalDateTime.of(2020, 02, 21, 14, 19))
				.endEventDateTime(LocalDateTime.of(2020, 02, 22, 14, 19))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("Daejeon")
				.free(false)
				.offline(true)
				.eventStatus(EventStatus.DRAFT)
				.build();
		return this.eventRepository.save(event);
	}
	private String getBearerToken() throws Exception {
		return "Bearer " + getAcessToken();
	}
	
	private String getAcessToken() throws Exception {
		// Given
		Account khj = Account.builder()
			.email(appProperties.getAdminUsername())
			.password(appProperties.getAdminPassword())
			.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
			.build();
				
		this.accountService.saveAccount(khj);
				
		ResultActions perform = this.mockMvc.perform(post("/oauth/token")
			.with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))	
			.param("username", appProperties.getAdminUsername())
			.param("password", appProperties.getAdminPassword())
			.param("grant_type", "password")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("access_token").exists())
		;
		
		var responseBody =  perform.andReturn().getResponse().getContentAsString();
		Jackson2JsonParser parser = new Jackson2JsonParser();
		return parser.parseMap(responseBody).get("access_token").toString();
		
	}
}

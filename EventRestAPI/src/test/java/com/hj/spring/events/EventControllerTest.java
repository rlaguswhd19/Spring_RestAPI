package com.hj.spring.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.spring.common.RestDocsConfiguration;
import com.hj.spring.common.TestDescription;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
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
					responseFields(
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
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event))
				) 
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Empty_Input() throws Exception {
		EventDto eventDto = EventDto.builder().build();
		
		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				)
				.andExpect(status().isBadRequest());
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
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$[0].objectName").exists())
				.andExpect(jsonPath("$[0].defaultMessage").exists())
				.andExpect(jsonPath("$[0].code").exists())
				;
	}
}

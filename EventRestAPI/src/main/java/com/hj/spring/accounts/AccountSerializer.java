package com.hj.spring.accounts;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

// @JsonComponent 모든 Account가 id만 나가게 된다. Account정보를 조회하고싶을때는 안됀다. 다른 용도로 사용할때는 사용해야한다.

public class AccountSerializer extends JsonSerializer<Account>{

	@Override
	public void serialize(Account account, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", account.getId());
		gen.writeEndObject();
	}

}

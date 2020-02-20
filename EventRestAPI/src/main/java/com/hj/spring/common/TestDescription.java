package com.hj.spring.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //target
@Retention(RetentionPolicy.SOURCE) //얼마나 오래 가져갈 것이냐 이 애노테이션을
public @interface TestDescription {
	
	String value();
}

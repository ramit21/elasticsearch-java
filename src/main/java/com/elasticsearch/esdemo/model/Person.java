package com.elasticsearch.esdemo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "people-idx")
public class Person {

	@Id
	private String name;
	private Integer age;
	private String dtCreated;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDtCreated() {
		return dtCreated;
	}

	public void setDtCreated(String dtCreated) {
		this.dtCreated = dtCreated;
	}

	@Override
	public String toString() {
		//return "{Name=" + name + ", age=" + age + ", dtCreated=" + dtCreated + "}";
		return "{Name=" + name + ", age=" + age + "}";
	}

}

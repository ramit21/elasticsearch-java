package com.elasticsearch.esdemo.model;

import java.util.Date;

public class Person {

	private String name;
	private Integer age;
	private Date dtCreated;

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

	public Date getDtCreated() {
		return dtCreated;
	}

	public void setDtCreated(Date dtCreated) {
		this.dtCreated = dtCreated;
	}

	@Override
	public String toString() {
		//return "{Name=" + name + ", age=" + age + ", dtCreated=" + dtCreated + "}";
		return "{Name=" + name + ", age=" + age + "}";
	}

}

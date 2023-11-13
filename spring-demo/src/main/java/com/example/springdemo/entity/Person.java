package com.example.springdemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
	
	private long id;
	private String name;
	private String email;
	
	@JsonProperty("data_insert")
	private String dataInsert;
	
	

}

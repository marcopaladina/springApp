package com.example.springdemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
	
	private long id;
	private String name;
	private String email;
	
	@JsonProperty("data_insert")
	private String dataInsert;

	@JsonProperty("data_update")
	private String dataUpdate;

}

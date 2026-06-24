package com.example.springdemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "person")
public class Person {
	
	private long id;
	private String name;
	private String email;
	
	@JsonProperty("data_insert")
	private String dataInsert;

	@JsonProperty("data_update")
	private String dataUpdate;

}

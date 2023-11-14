package com.example.springdemo.bean;

import java.util.UUID;

import com.example.springdemo.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonBean {
	
	
	private UUID id;
	
	private Person person;


}

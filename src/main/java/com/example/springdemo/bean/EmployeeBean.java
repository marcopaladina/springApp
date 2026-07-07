package com.example.springdemo.bean;

import com.example.springdemo.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;


@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBean {
	
	
	private UUID id;
	
	private Employee employee;
}

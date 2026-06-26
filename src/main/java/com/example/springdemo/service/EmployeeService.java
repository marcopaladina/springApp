package com.example.springdemo.service;


import com.example.springdemo.entity.Employee;
import com.example.springdemo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public List<Employee> getEmployee() {
        return repo.findAll();
    }
}

package com.example.springdemo.service;


import com.example.springdemo.entity.Employee;
import com.example.springdemo.entity.Person;
import com.example.springdemo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
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

    public List<Person> getPersons() {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Person getPerson(Long id) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void addPerson(Person person) {
    }

    public void updatePerson(Long id, Person person) {
    }

    public void deletePerson(Long id) {
    }

}

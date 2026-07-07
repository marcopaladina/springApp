package com.example.springdemo.service;


import com.example.springdemo.entity.Employee;
import com.example.springdemo.entity.Person;
import com.example.springdemo.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }



    public Page<Employee> getEmployee(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public List<Person> getPersons() {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Optional<Employee> getEmployee(Long id) {

        return repo.findById(id);
    }

    public void addPerson(Person person) {
    }

    public void updatePerson(Long id, Person person) {
    }

    public void deletePerson(Long id) {
    }


    public Employee addEmployee(Employee employee) {
        return repo.save(employee);
    }
}

package com.example.springdemo.service;


import com.example.springdemo.entity.Employee;
import com.example.springdemo.entity.Person;
import com.example.springdemo.exception.ResourceNotFoundException;
import com.example.springdemo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
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



    public void updatePerson(Long id, Person person) {
    }

    public void deletePerson(long id) {
    }

    public void addEmployee(Employee employee) {
        repo.save(employee);
    }

    public Employee getEmployee(long id) {
        log.info("Ricerca persona id= {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Persona non trovata id= {}", id);
                    return new ResourceNotFoundException("Person con id " + id + " non trovata");
                });
    }
}

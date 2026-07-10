package com.example.springdemo.service;


import com.example.springdemo.entity.Employee;
import com.example.springdemo.exception.ResourceNotFoundException;
import com.example.springdemo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    private final EmployeeRepository repo;


    public Page<Employee> getEmployee(Pageable pageable) {

        return repo.findAll(pageable);
    }



    public Employee getEmployee(Long id) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void updateEmployee(Long id, Employee employee) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void deleteEmployee(long id) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void addEmployee(Employee employee) {

        repo.save(employee);
    }

    public Employee getEmployeeById(long id) {
        log.info("Ricerca persona id= {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Persona non trovata id= {}", id);
                    return new ResourceNotFoundException("Person con id " + id + " non trovata");
                });
    }
}

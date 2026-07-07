package com.example.springdemo.controller;

import com.example.springdemo.bean.EmployeeBean;
import com.example.springdemo.entity.Employee;
import com.example.springdemo.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v2")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/employee")
    public Page<Employee> findEmployees(Pageable pageable) {
        log.info("Find employees with page: {}", pageable);
        log.info("findEmployees -> Vuoto?: {}", service.getEmployee(pageable).isEmpty());

        return service.getEmployee(pageable);
    }



//    @GetMapping("/listOfEmployee")
//    public ResponseEntity<Page<EmployeeBean>> findListOfEmployee(Pageable pageable)
//            throws JsonProcessingException {
//
//        Page<EmployeeBean> responsePage = service.getEmployee(pageable);
//
//        for (EmployeeBean employee : responsePage.getContent()) {
//            employee.setId(UUID.randomUUID());
//            employee.setEmployee(employee);
//        }
//                .map(employee -> {
//                    EmployeeBean employeeBean = new EmployeeBean();
//                    employeeBean.setId(UUID.randomUUID());
//                    employeeBean.setEmployee(employee);
//                    return employeeBean;
//                });

//        log.info("findListOfEmployee(): {}",
//                new ObjectMapper().writeValueAsString(responsePage));
//
//        return ResponseEntity.ok(responsePage);
//    }



    @GetMapping("/employee/{id}")
    public Optional<Employee> findEmployee(@PathVariable Long id) {

        log.info("findEmployee(@PathVariable int id)= {}", id);

        return service.getEmployee(id);
    }


    @PostMapping("/employee")
    public void addEmployee(@RequestBody Employee employee) {

        log.info("addEmployee(@RequestBody Employee employee)= {}", employee);

        service.addEmployee(employee);
    }


//    @PutMapping("/person/{id}")
//    public void updatePerson(@PathVariable Long id, @RequestBody Person person) throws JsonProcessingException {
//        log.info("updatePerson(@RequestParam(\"id\") int id, @RequestBody Person person), {}, {}", id, person);
//
//        service.updatePerson(id, person);
//    }


//    @DeleteMapping("/person/{id}")
//    public void deletePerson(@PathVariable Long id) {
//
//        log.info("deletePerson(@PathVariable int id)= {}", id);
//
//        service.deletePerson(id);
//    }

}

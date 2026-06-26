package com.example.springdemo.controller;

import com.example.springdemo.entity.Employee;
import com.example.springdemo.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<List<Employee>> findEmployees() {
        log.info("findEmployees -> Vuoto?: {}", service.getEmployee(). isEmpty());

        return new ResponseEntity<>(service.getEmployee(), HttpStatus.OK);
    }


//    @GetMapping("/listOfPerson")
//    public ResponseEntity<ArrayList<PersonBean>> findListOfPerson() throws JsonProcessingException {
//
//        ObjectMapper obj = new ObjectMapper();
//        String risultato = obj.writeValueAsString(service.getPersons());
//        log.info("findListOfPerson(): {}", risultato);
//
//        PersonBean personBean = null;
//        ArrayList<PersonBean> responseListOfPerson = new ArrayList<>();
//        List<Person> lista = service.getPersons();
//        for(Person elemento:lista) {
//            personBean = new PersonBean();
//            personBean.setId(UUID.randomUUID());
//            personBean.setPerson(elemento);
//
//            responseListOfPerson.add(personBean);
//        }
//        return new ResponseEntity<>(responseListOfPerson, HttpStatus.OK);
//    }


//    @GetMapping("/person/{id}")
//    public Person findPerson(@PathVariable Long id) {
//
//        log.info("findPerson(@PathVariable int id)= {}", id);
//
//        return service.getPerson(id);
//    }


//    @PostMapping("/person")
//    public void addPerson(@RequestBody Person person) {
//
//        log.info("addPerson(@RequestBody Person person)= {}", person);
//
//        service.addPerson(person);
//    }


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

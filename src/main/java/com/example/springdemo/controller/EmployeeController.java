package com.example.springdemo.controller;

import com.example.springdemo.entity.Employee;
import com.example.springdemo.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService service;

    @GetMapping("/")
    public ResponseEntity<Page<Employee>> findEmployees(Pageable pageable) {
        log.info("Find employees with page: {}", pageable);
        log.info("findEmployees -> Vuoto?: {}", service.getEmployee(pageable).isEmpty());

        return new ResponseEntity<>(service.getEmployee(pageable), HttpStatus.OK);
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


    @GetMapping("/employee/{id}")
    public Employee findEmployee(@PathVariable Long id) {

        log.info("findEmployee(@PathVariable int id)= {}", id);

        return service.getEmployeeById(id);
    }


    @PostMapping("/")
    public void addEmployee(@RequestBody Employee employee) {
        log.info("addEmployee(@RequestBody Employee employee)= {}", employee);
        service.addEmployee(employee);
    }


    @PutMapping("/person/{id}")
    public void updateEmployee(@PathVariable Long id, @RequestBody Employee employee) throws JsonProcessingException {
        log.info("updateEmployee(@RequestParam(\"id\") int id, @RequestBody Employee employee), {}, {}", id, employee);

        service.updateEmployee(id, employee);
    }


    @DeleteMapping("/person/{id}")
    public void deleteEmployee(@PathVariable Long id) {

        log.info("deleteEmployee(@PathVariable int id)= {}", id);

        service.deleteEmployee(id);
    }

}

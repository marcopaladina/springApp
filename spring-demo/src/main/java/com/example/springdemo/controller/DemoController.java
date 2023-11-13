package com.example.springdemo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.bean.PersonBean;
import com.example.springdemo.entity.Person;
import com.example.springdemo.service.DemoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/api/v1")
public class DemoController {
	
	private static final String WELCOME = "Welcome";
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);
	private static final String HOME = "Home ";
	private final AtomicLong counter = new AtomicLong();
	
	@Autowired
	private DemoService service;

	@Value("${business.service.element}")
	String element;

    @Value("${spring.application.name}")
    String appName;


	@GetMapping("")
	public String defaultMethod() {
		
		final String template = "[%d], Ciao ";
		
		log.info("counter: " + counter.get());
		String result = String.format(template, counter.incrementAndGet()).concat("Ciccio");
		
		return result;
	}


	@GetMapping("/hello")
	public List<String> secondMethod() {

    	// setto il valore %1 con la stringa testo("a tutti")
    	String testo = "for professional Developer";
    	
    	testo = appName.replace("%1", testo);
		
		log.info("testo: " + testo);

		return Arrays.asList(WELCOME, "Hello", testo, "Arrivederci Roma", HOME + "page", element);
	}


	@GetMapping("/persons")
	public ResponseEntity<List<Person>> findPersons() throws JsonProcessingException {
		
		ObjectMapper obj = new ObjectMapper();
		String risultato = obj.writeValueAsString(service.getPersons());
		log.info("findPersons(): " + risultato);

		return new ResponseEntity<>(service.getPersons(), HttpStatus.OK);
	}


	@GetMapping("/listOfPerson")
	public ResponseEntity<List<PersonBean>> findListOfPerson() throws JsonProcessingException {
		
		ObjectMapper obj = new ObjectMapper();
		String risultato = obj.writeValueAsString(service.getPersons());
		log.info("findPersons(): " + risultato);
		
		PersonBean personBean = null;
		ArrayList<PersonBean> responseListOfPerson = new ArrayList<>();
		List<Person> lista = service.getPersons();
		for(Person elemento:lista) {
			personBean = new PersonBean();
			personBean.setId(UUID.randomUUID());
			personBean.setPerson(elemento);
			
			responseListOfPerson.add(personBean);
		}
		return new ResponseEntity<>(responseListOfPerson, HttpStatus.OK);
	}
	
	
	@GetMapping("/person/{id}")
	public Person findPerson(@PathVariable int id) {
		
		log.info("findPerson(@PathVariable int id), {}", id );

		return service.getPerson(id);
	}


	@GetMapping("/scan-file")
	public List<String> scannerFile() throws IOException {
		
		log.info("scannerFile()");

		return service.scanFile();
	}


	@PostMapping("/person")
	public void addPerson(@RequestBody Person person) {
		
		log.info("addPerson(@RequestBody Person person), {}", person);

		service.addPerson(person);
	}


	@PutMapping("/person{id}")
	public void updatePerson(@RequestParam("id") int id, @RequestBody Person person) throws JsonProcessingException {
		
		log.info("updatePerson(@RequestParam(\"id\") int id, @RequestBody Person person), {}, {}", id, person);

		service.updatePerson(id, person);
	}


	@DeleteMapping("/person/{id}")
	public void deletePerson(@PathVariable int id) {
		
		log.info("deletePerson(@PathVariable int id), {}", id);

		service.deletePerson(id);
	}

	
	@GetMapping("/test{name}")
	@ResponseBody
	String hello(@RequestParam String name, @RequestHeader int number) {
		
		log.trace("Hello {}", name, number);
		log.debug("Hello {}", name, number);
		log.info("Hello {}", name, number);
		log.warn("Hello {}", name, number);
		log.error("Hello {}", name, number);
		
		return "Hello: " + name + " - " + number;
	}

}

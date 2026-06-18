package com.example.springdemo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicLong;

import com.example.springdemo.exception.ResourceNotFoundException;
import com.example.springdemo.repository.PersonRepository;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.example.springdemo.utility.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springdemo.entity.Person;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DemoService {
	
	private static final Logger log = LoggerFactory.getLogger(DemoService.class);
	private final PersonRepository personRepository;
//	private final AtomicLong counter = new AtomicLong();

    public DemoService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

	public List<Person> getPersons() {
		return personRepository.findAll();
	}

	public Person getPerson(Long id) {
		return personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Person con id " + id + " non trovata"));
	}

	public Person addPerson(Person person) {
		person.setDataInsert(Util.getTodayFormatted());
		return personRepository.save(person);
	}

	public Person updatePerson(Long id, Person person)
			throws JsonProcessingException {

		ObjectMapper obj = new ObjectMapper();
		String risultato = obj.writeValueAsString(person);
		log.info("Mappa Oggetto: {}", risultato);

		Person existingPerson = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Person con id " + id + " non trovata"));
		existingPerson.setName(person.getName());
		existingPerson.setEmail(person.getEmail());
		existingPerson.setDataModify(Util.getTodayFormatted());

		return personRepository.save(existingPerson);
	}

	public void deletePerson(Long id) {

		if (!personRepository.existsById(id)) {
			throw new ResourceNotFoundException(
					"Person con id " + id + " non trovata");
		}

		personRepository.deleteById(id);
	}

	public List<String> scanFile() throws IOException {

		List<String> results = new ArrayList<>();
		String filePath = "test.txt";

		Resource res = new ClassPathResource(filePath);

		log.info(res.getFilename());

		results.add(res.getDescription());
		results.add(res.getFilename());
		results.add(res.contentLength() + " Kb");

		try (BufferedReader buffer =
					 new BufferedReader(new FileReader(res.getFile()))) {

			results.add("Start File ---> [");

			while (buffer.ready()) {
				results.add(buffer.readLine());
			}

			results.add("]");
		}

		return results;
	}

//	public List<Person> addPersons(List<Person> persons) {
//		Person person = new Person();
//		for (Person p : persons) {
//			person.setName(p.getName());
//			person.setEmail(p.getEmail());
//			person.setDataInsert(Util.getTodayFormatted());
//			persons.add(person);
//		}
//		return personRepository.saveAll(persons);
//	}

//	public List<Person> addPersons(List<Person> persons) {
//		persons.forEach(person -> {
//			person.setDataInsert(Util.getTodayFormatted());
//		});
//		return personRepository.saveAll(persons);
//	}
	/*
	 * Variante più efficiente per grandi volumi. Se devo inserire centinaia
	 * o migliaia di record, si può usare una transazione.
	 */
	@Transactional
	public List<Person> addPersons(List<Person> persons) {
		String today = Util.getTodayFormatted();

		persons.forEach(person -> {
			person.setDataInsert(today);
		});
		return personRepository.saveAll(persons);
	}

//	private List<Person> persons = new ArrayList<>(Arrays.asList(
//		new Person(1, "John Wick", "jowick@gmail.com", Util.buildSystemDate(Util.DATE_FORMAT_LONG), null),
//		new Person(2, "Bill Muller", "bill.mu@gmail.com", Util.buildSystemDate(Util.DATE_FORMAT_LONG), null),
//		new Person(3, "John Smith", "john.sm@gmail.com", Util.buildSystemDate(Util.DATE_FORMAT_LONG), null),
//		new Person(4, "Bob Geldof", "bobge@email.com", Util.buildSystemDate(Util.DATE_FORMAT_LONG), null)
//	));

//    public DemoService(PersonRepository personRepository) {
//        this.personRepository = personRepository;
//    }

//    private Long findLastId() {
//		return persons.get(persons.size() - 1).getId();
//	}


	
//	public List<Person> getPersons() {
//		return persons;
//	}
	
	
//	public Person getPerson(int id) {
//		return persons.stream()
//				.filter(p -> p.getId() == id)
//				.findFirst()
//				.get();
//	}
	
	
//	public void addPerson(Person person) {
//		Person newPers = Person.builder()
//				.id(findLastId() + 1)
//				.name(person.getName())
//				.email(person.getEmail())
//				.dataInsert(Util.getTodayFormatted())
//				.build();
//		persons.add(newPers);
//	}
	
//	public void updatePerson(int id, Person person) throws JsonProcessingException {
//				ObjectMapper obj = new ObjectMapper();
//		String risultato = obj.writeValueAsString(person);
//		log.info("Mappa Oggetto: " + risultato);
//
//		for(int i=0; i<persons.size(); i++) {
//			Person p = persons.get(i);
//			if(p.getId() == id) {
//				person.setId(id);
//				person.setDataModify(Util.getTodayFormatted());
//				persons.set(i, person);
//			}
//		}
//	}
	
//	public void deletePerson(int id) {
//		persons.remove(id);
//	}
	
	
//	public List<String> scanFile() throws IOException {
//		List<String> results = new ArrayList<>();
//		String filePath = "test.txt";
//		Resource res = new ClassPathResource(filePath);
//		log.info(res.getFilename());
//		results.add(res.getDescription());
//		results.add(res.getFilename());
//		results.add(res.contentLength() + " Kb");
//
//		FileReader fileIn = new FileReader(res.getFile());
//		BufferedReader buffer = new BufferedReader(fileIn);
//
//		results.add("Start File ---> [");
//		while(buffer.ready()) {
//			results.add(buffer.readLine());
//		}
//		results.add("]");
//
//		return results;
//	}

}

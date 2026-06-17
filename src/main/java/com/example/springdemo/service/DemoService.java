package com.example.springdemo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.example.springdemo.utility.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.springdemo.entity.Person;



@Service
public class DemoService {
	
	private static final Logger log = LoggerFactory.getLogger(DemoService.class);

	private List<Person> persons = new ArrayList<>(Arrays.asList(
		new Person(1, "John Wick", "jowick@gmail.com", Util.defaultStartDate(Util.DATE_FORMAT_LONG), null),
		new Person(2, "Bill Muller", "bill.mu@gmail.com", Util.defaultStartDate(Util.DATE_FORMAT_LONG), null),
		new Person(3, "John Smith", "john.sm@gmail.com", Util.defaultStartDate(Util.DATE_FORMAT_LONG), null),
		new Person(4, "Bob Geldof", "bobge@email.com", Util.defaultStartDate(Util.DATE_FORMAT_LONG), null)
	));
	
	public List<Person> getPersons() {
		
		return persons;
	}
	
	
	public Person getPerson(int id) {
		
		return persons.stream()
				.filter(p -> p.getId() == id)
				.findFirst()
				.get();
	}
	
	
	public void addPerson(Person person) {
		Person newPerson = Person.builder()
				.id(findLastId() + 1)
				.name(person.getName())
				.email(person.getEmail())
				.dataInsert(Util.getTodayFormatted())
				.dataUpdate(null)
				.build();
		persons.add(newPerson);
	}

    private long findLastId() {
        return persons.get(persons.size() - 1).getId();
    }

//	@Query("SELECT COALESCE(MAX(u.id), 0) FROM User u")
//	Long findLastId();

	public void updatePerson(int id, Person person) throws JsonProcessingException {

		for (int i = 0; i < persons.size(); i++) {
			Person p = persons.get(i);
			if (p.getId() == id) {

				p.setId(id);
				p.setName(person.getName());
				p.setEmail(person.getEmail());
				p.setDataUpdate(Util.getTodayFormatted());
				// aggiorna solo i campi necessari
				return;
			}
		}
	}


//	public void updatePerson(int id, Person person) throws JsonProcessingException {
//
//		ObjectMapper obj = new ObjectMapper();
//		String risultato = obj.writeValueAsString(person);
//		log.info("Mappa Oggetto: " + risultato);
//
//		for (int i = 0; i < persons.size(); i++) {
//			Person p = persons.get(i);
//			if (p.getId() == id) {
//
//				// Mantieni l'id originale
//				person.setId(id);
//
//				persons.set(i, person);
//				return;
//			}
//		}
//	}


	public void deletePerson(int id) {

		persons.remove(getPerson(id));
	}
	
	
	public List<String> scanFile() throws IOException {
		
		List<String> results = new ArrayList<>();
		String filePath = "test.txt";
		Resource res = new ClassPathResource(filePath);
		log.info(res.getFilename());
		results.add(res.getDescription());
		results.add(res.getFilename());
		results.add(res.contentLength() + " Kb");
		
		FileReader fileIn = new FileReader(res.getFile());
		BufferedReader buffer = new BufferedReader(fileIn);
		
		results.add("Start File ---> [");
		while(buffer.ready()) {
			results.add(buffer.readLine());
		}
		results.add("]");
		
		return results;
	}

}

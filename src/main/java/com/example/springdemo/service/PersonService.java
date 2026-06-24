package com.example.springdemo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

import com.example.springdemo.exception.ResourceNotFoundException;
import com.example.springdemo.repository.PersonRepository;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.hibernate.cfg.NotYetImplementedException;
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
public class PersonService {
	
	private static final Logger log = LoggerFactory.getLogger(PersonService.class);
	private final PersonRepository personRepository;
//	private final AtomicLong counter = new AtomicLong();

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

	public List<Person> getPersons() {
		return personRepository.findAll();
	}

	public Person getPerson(Long id) {
		log.info("Ricerca persona id= {}", id);
		return personRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("Persona non trovata id= {}", id);
					return new ResourceNotFoundException("Person con id " + id + " non trovata");
				});
	}

	public Person addPerson(Person person) {
		log.info("Crea persona person= {}", person);
		person.setDataInsert(Util.getTodayFormatted());
		return personRepository.save(person);
	}

	public Person updatePerson(Long id, Person person)
			throws JsonProcessingException {

		ObjectMapper obj = new ObjectMapper();
		String risultato = obj.writeValueAsString(person);
		log.info("Mappa Oggetto= {}", risultato);

		Person existingPerson = personRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("Persona non trovata id= {}", id);
					return new ResourceNotFoundException("Person con id " + id + " non trovata");
				});
		existingPerson.setName(person.getName());
		existingPerson.setEmail(person.getEmail());
		existingPerson.setDataUpdate(Util.getTodayFormatted());

		return personRepository.save(existingPerson);
	}

	public void deletePerson(Long id) {
		log.warn("Persona trovata id= {}", id);
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

	public static String say() {
		return "Working...";
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

	public long findLastId() {
		log.info("Finding last id from DB: {}", personRepository.count());
		return personRepository.findLastId();
	}

	public byte[] createPdf() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Document document = new Document(PageSize.A4, 40, 40, 60, 60);
			PdfWriter writer = PdfWriter.getInstance(document, baos);

			document.open();

			PdfContentByte canvas = writer.getDirectContent();

			// -----------------------------
			// HEADER
			// -----------------------------
			canvas.beginText();
			canvas.setFontAndSize(BaseFont.createFont(), 14);
			canvas.setTextMatrix(40, 820);
			canvas.showText("Report PDF - Header");
			canvas.endText();

			// Linea sotto l’header
			canvas.moveTo(40, 810);
			canvas.lineTo(555, 810);
			canvas.stroke();

			// -----------------------------
			// FOOTER
			// -----------------------------
			canvas.beginText();
			canvas.setFontAndSize(BaseFont.createFont(), 10);
			canvas.setTextMatrix(40, 30);
			canvas.showText("Footer - Pagina 1");
			canvas.endText();

			// Linea sopra il footer
			canvas.moveTo(40, 50);
			canvas.lineTo(555, 50);
			canvas.stroke();

			// -----------------------------
			// TABELLA CENTRALE (4 colonne)
			// -----------------------------
			PdfPTable table = new PdfPTable(4); // <-- ora 4 colonne
			table.setWidthPercentage(100);
			table.setSpacingBefore(80);

			// Header tabella
			table.addCell("ID");
			table.addCell("Nome");
			table.addCell("Valore");
			table.addCell("Descrizione");

			// Righe dati
			table.addCell("1");
			table.addCell("Marco");
			table.addCell("100");
			table.addCell("Prima riga");

			table.addCell("2");
			table.addCell("Spring Boot");
			table.addCell("200");
			table.addCell("Framework Java");

			table.addCell("3");
			table.addCell("PDF Report");
			table.addCell("300");
			table.addCell("Generazione PDF");

			document.add(table);

			// -----------------------------
			// PARAGRAFO FINALE
			// -----------------------------
			document.add(new Paragraph("\n\nDocumento generato con header, footer e tabella a 4 colonne."));

			document.close();
			return baos.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException("Errore durante la creazione del PDF", e);
		}
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

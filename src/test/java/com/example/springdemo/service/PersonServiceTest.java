package com.example.springdemo.service;

import com.example.springdemo.entity.Person;
import com.example.springdemo.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService service;

    @Test
    void shouldReturnAllPersons() {
        List<Person> persons = List.of(
                Person.builder().id(1L).name("Mario").build(),
                Person.builder().id(2L).name("Luigi").build()
        );

        when(personRepository.findAll())
                .thenReturn(persons);
        List<Person> result = service.getPersons();

        assertEquals(2, result.size());
        verify(personRepository).findAll();
    }

    @Test
    void shouldSaveManyPersons() {

        List<Person> persons = List.of(
                Person.builder()
                        .name("Mario")
                        .email("mario@test.it")
                        .build(),

                Person.builder()
                        .name("Luigi")
                        .email("luigi@test.it")
                        .build()
        );

        when(personRepository.saveAll(anyList()))
                .thenReturn(persons);

        List<Person> result =
                service.addPersons(persons);

        assertEquals(2, result.size());

        verify(personRepository).saveAll(anyList());
    }

    @Test
    void shouldDeletePerson() {
        when(personRepository.existsById(1L))
                .thenReturn(true);
        service.deletePerson(1L);
        verify(personRepository)
                .deleteById(1L);
    }
}
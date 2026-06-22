package com.example.springdemo.controller;

import com.example.springdemo.entity.Person;
import com.example.springdemo.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService PersonService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllPersons() throws Exception {

        List<Person> persons = List.of(
                Person.builder()
                        .id(1L)
                        .name("Mario")
                        .email("mario@test.it")
                        .build(),
                Person.builder()
                        .id(2L)
                        .name("Luigi")
                        .email("luigi@test.it")
                        .build()
        );

        when(PersonService.getPersons()).thenReturn(persons);

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mario"))
                .andExpect(jsonPath("$[1].name").value("Luigi"));
        verify(PersonService, times(2)).getPersons();
    }

    @Test
    void shouldReturnSinglePerson() throws Exception {

        Person person = Person.builder()
                .id(1L)
                .name("Mario")
                .email("mario@test.it")
                .build();
        when(PersonService.getPerson(1L))
                .thenReturn(person);
        mockMvc.perform(get("/api/v1/person/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mario"));
    }

    @Test
    void shouldCreatePerson() throws Exception {

        Person person = Person.builder()
                .name("Mario")
                .email("mario@test.it")
                .build();
        mockMvc.perform(post("/api/v1/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());
        verify(PersonService).addPerson(any(Person.class));
    }

    @Test
    void shouldCreateManyPersons() throws Exception {

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

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persons)))
                .andExpect(status().isCreated());
        verify(PersonService).addPersons(anyList());
    }

    @Test
    void shouldDeletePerson() throws Exception {

        mockMvc.perform(delete("/api/v1/person/1"))
                .andExpect(status().isOk());
        verify(PersonService).deletePerson(1L);
    }
}
package com.example.springdemo.repository;

import com.example.springdemo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT COALESCE(MAX(per.id), 0) FROM Person per")
	Long findLastId();
}

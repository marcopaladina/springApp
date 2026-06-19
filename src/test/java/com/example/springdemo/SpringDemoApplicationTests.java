package com.example.springdemo;

import com.example.springdemo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SpringDemoApplicationTests {

    @Autowired
    private PersonService service;

    @Test
    void contextLoads() {
        assertNotNull(service, "Il contesto Spring non ha caricato service");
    }

    @Test
    void testServiceLogic() {
        String result = service.say();
        assertEquals("Working...", result);
    }
}

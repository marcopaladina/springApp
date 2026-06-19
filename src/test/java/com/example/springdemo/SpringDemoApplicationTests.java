package com.example.springdemo;

import com.example.springdemo.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SpringDemoApplicationTests {

    @Autowired
    private DemoService service;

    @Test
    void contextLoads() {
        assertNotNull(service, "Il contesto Spring non ha caricato MyService");
    }

    @Test
    void testServiceLogic() {
        String result = service.say();
        assertEquals("Working...", result);
    }
}

package com.github.bcm.integration.controllers;

import com.github.bcm.integration.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetHelloWorld() {
        String body = this.restTemplate.getForObject("/helloworld", String.class);
        assertThat(body).isEqualTo("Hello World");
    }

}

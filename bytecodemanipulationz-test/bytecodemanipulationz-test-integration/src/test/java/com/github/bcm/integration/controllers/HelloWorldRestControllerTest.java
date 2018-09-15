package com.github.bcm.integration.controllers;

import com.github.bcm.integration.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldRestControllerTest {

    private static final Logger LOG  = LoggerFactory.getLogger(HelloWorldRestControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetHelloWorld() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/helloworld", String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        String body = response.getBody();
        assertThat(body).isEqualTo("Hello World");

        HttpHeaders headers = response.getHeaders();

        LOG.debug("Http Headers={}", headers);

        assertThat(headers.containsKey("X-SJ-UUID")).isTrue();
        assertThat(headers.get("X-SJ-UUID")).isNotNull();
        assertThat(headers.get("X-SJ-UUID")).hasSize(1);
        assertThat(headers.get("X-SJ-UUID").get(0)).satisfies(value -> Objects.requireNonNull(UUID.fromString(value)));
    }

}

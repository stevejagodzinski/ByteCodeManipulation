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
import org.springframework.http.HttpMethod;
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

    private static final String GET_URL = "/helloworld";
    private static final String POST_URL = "/helloworld/test-post";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetHelloWorld() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(GET_URL, String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        String body = response.getBody();
        assertThat(body).isEqualTo("Hello World");

        validateHeaders(response.getHeaders());
    }

    @Test
    public void testPutHelloWorld() {
        ResponseEntity<Void> response = this.restTemplate.exchange("/helloworld/test-put/123", HttpMethod.PUT, null, Void.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        validateHeaders(response.getHeaders());
    }

    @Test
    public void testPostHelloWorld() {
        ResponseEntity<Void> response = this.restTemplate.postForEntity(POST_URL, "postbody", Void.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        validateHeaders(response.getHeaders());
    }

    @Test
    public void testDeleteHelloWorld() {
        ResponseEntity<Void> response = this.restTemplate.exchange("/helloworld/test-delete/123", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        validateHeaders(response.getHeaders());
    }

    @Test
    //@Ignore("Failing")
    public void testHeaderIsAddedWhenRequestIsBad() {
        ResponseEntity<Void> response = this.restTemplate.postForEntity(POST_URL, null, Void.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);

        validateHeaders(response.getHeaders());
    }

    @Test
    public void testSeparateRequestsCreateUniqueIds() {
        ResponseEntity<String> response1 = this.restTemplate.getForEntity(GET_URL, String.class);
        ResponseEntity<String> response2 = this.restTemplate.getForEntity(GET_URL, String.class);

        String uuid1 = validateHeaders(response1.getHeaders());
        String uuid2 = validateHeaders(response2.getHeaders());

        assertThat(uuid1).isNotEqualTo(uuid2);
    }

    private String validateHeaders(HttpHeaders headers) {
        LOG.debug("Http Headers={}", headers);

        assertThat(headers.containsKey("X-SJ-UUID")).isTrue();
        assertThat(headers.get("X-SJ-UUID")).isNotNull();
        assertThat(headers.get("X-SJ-UUID")).hasSize(1);

        @SuppressWarnings("ConstantConditions") // headers.get("X-SJ-UUID") can not be null... null check performed above
        String uuid = headers.get("X-SJ-UUID").get(0);

        assertThat(uuid).satisfies(value -> Objects.requireNonNull(UUID.fromString(value)));

        return uuid;
    }

}

package com.github.bcm.integration.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test/time")
    public long getCurrentTimeMillis() {
        LOG.debug("getCurrentTimeMillis executed");
        return System.currentTimeMillis();
    }

    @PutMapping("/test/test-put/{id}")
    public void testPut(@PathVariable String id) {
        LOG.debug("testPut executed with id={}", id);
    }

    @PostMapping("/test/test-post")
    public void testPost(@RequestBody String body) {
        LOG.debug("testPost executed with body={}", body);
    }

    @DeleteMapping("/test/test-delete/{id}")
    public void testDelete(@PathVariable String id) {
        LOG.debug("testDelete executed with id={}", id);
    }
}

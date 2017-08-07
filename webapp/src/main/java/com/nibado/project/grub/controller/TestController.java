package com.nibado.project.grub.controller;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Response test() {
        log.info("Test!");

        return new Response("Boop!");
    }

    @Value
    public static class Response {
        private String message;
    }
}

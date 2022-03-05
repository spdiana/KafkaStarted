package com.example.controller;

import com.example.model.PersonDto;
import com.example.service.ReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
@RequestMapping(value = ReadController.BASE_PATH, produces = "application/hal+json")
public class ReadController {

    protected static final String BASE_PATH = "/read";
    private final ReadService readService;

    public ReadController(ReadService readService) {
        this.readService = readService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<String>> create(@Validated @RequestBody PersonDto person) {
        return readService.sendToQueue(person);
    }

}

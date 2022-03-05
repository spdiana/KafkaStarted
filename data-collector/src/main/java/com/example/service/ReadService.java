package com.example.service;

import com.example.model.PersonDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface ReadService {

    DeferredResult<ResponseEntity<String>> sendToQueue(PersonDto personDto);
}

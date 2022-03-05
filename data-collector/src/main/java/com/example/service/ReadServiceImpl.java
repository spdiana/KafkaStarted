package com.example.service;

import com.example.PersonEvent;
import com.example.model.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Service
public class ReadServiceImpl implements ReadService {

    private KafkaTemplate<String, PersonEvent> kafkaTemplate;

    @Value("${spring.kafka.demo-topic}")
    private String avroTopic;

    @Value("${event_seed.value}")
    private int seedValue;

    @Autowired
    public ReadServiceImpl(KafkaTemplate<String, PersonEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public DeferredResult<ResponseEntity<String>> sendToQueue(PersonDto personDto) {

        PersonEvent personEvent = PersonEvent
                .newBuilder()
                .setFirstName(personDto.getFirstName())
                .setLastName(personDto.getLastName())
                .setAge(personDto.getAge())
                .setSeedValue(seedValue).build();

        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();

        ListenableFuture<SendResult<String, PersonEvent>> future = kafkaTemplate.send(avroTopic, personEvent);
        future.addCallback(new ListenableFutureCallback<SendResult<String, PersonEvent>>() {

            @Override
            public void onSuccess(final SendResult<String, PersonEvent> message) {
                deferredResult.setResult(new ResponseEntity<>("Message sent", HttpStatus.CREATED));
                log.info("sent message= " + message + " with offset= " + message.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(final Throwable throwable) {
                deferredResult.setResult(new ResponseEntity<>("Message not sent", HttpStatus.BAD_REQUEST));
                log.error("unable to send message= " + throwable.getMessage());
            }
        });
        return deferredResult;
    }
}

package com.example.kafkajsonproducer.controllers;

import com.example.kafkajsonproducer.Service.ProducerService;
import com.example.kafkajsonproducer.dto.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producer")
@Slf4j
public class ProducerController {
    private final ProducerService messageProducer;

    @PostMapping("/customer")
    public CompletableFuture<ResponseEntity<Customer>> sendCustomer(@RequestBody Customer customer){
        log.info(
                "Received customer publish request. customerId={}, email={}",
                customer.getId(),
                customer.getEmail()
        );

        return messageProducer.sendCustomerEvent(customer)
                .thenApply(result -> {
                    var metadata = result.getRecordMetadata();
                    log.info(
                            "Customer event published. customerId={}, topic={}, partition={}, offset={}",
                            customer.getId(),
                            result.getProducerRecord().topic(),
                            metadata.partition(),
                            metadata.offset()
                    );
                    return ResponseEntity.ok(customer);
                })
                .exceptionally(throwable -> {
                    log.error(
                            "Customer publish failed. customerId={}, error={}",
                            customer.getId(),
                            extractErrorMessage(throwable),
                            throwable
                    );
                    return ResponseEntity.internalServerError().build();
                });
    }

    private static String extractErrorMessage(Throwable throwable) {
        Throwable rootCause = throwable.getCause() != null ? throwable.getCause() : throwable;
        return rootCause.getMessage() != null ? rootCause.getMessage() : rootCause.toString();
    }
}

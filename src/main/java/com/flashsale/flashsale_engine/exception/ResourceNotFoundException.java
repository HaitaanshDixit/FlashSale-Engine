package com.flashsale.flashsale_engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
1. findById(999) runs
        ↓
2. Returns Optional<Sneaker> — either contains a Sneaker or is empty
        ↓
3. orElseThrow() checks — is the Optional empty?
        ↓
   YES → executes the lambda: new ResourceNotFoundException("Sneaker not found with id: 999")
              ↓
         ResourceNotFoundException constructor runs
              ↓
         super("Sneaker not found with id: 999") called
              ↓
         message travels up: RuntimeException → Exception → Throwable
              ↓
         Throwable stores message in detailMessage field
              ↓
         Exception is thrown, travels up the call stack
              ↓
         GlobalExceptionHandler catches it
              ↓
         ex.getMessage() retrieves "Sneaker not found with id: 999" from Throwable
              ↓
         Returns clean 404 JSON response
        ↓
   NO → returns the Sneaker object, continues normally
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
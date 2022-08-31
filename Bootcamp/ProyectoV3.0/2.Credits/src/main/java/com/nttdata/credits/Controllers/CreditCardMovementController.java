package com.nttdata.credits.Controllers;

import com.nttdata.credits.Application.Services.CreditCardMovement.CreditCardMovementService;
import com.nttdata.credits.Domain.CreditCardMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/creditCardMovements")

public class CreditCardMovementController {
    @Autowired
    private CreditCardMovementService CreditCardMovementService;

    @PostMapping
    public Mono<ResponseEntity<CreditCardMovement>> addMovement(@RequestBody CreditCardMovement creditCardMovement) {

        return CreditCardMovementService.register(creditCardMovement)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c));
    }
}

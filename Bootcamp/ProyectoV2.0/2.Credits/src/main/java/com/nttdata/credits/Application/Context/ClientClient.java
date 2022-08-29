package com.nttdata.credits.Application.Context;

import com.nttdata.credits.Application.Models.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(url = "http://localhost:3000/api/v1/clients/", name = "clients")
public interface ClientClient {
  @GetMapping("{ruc_dni}")
  public ResponseEntity<Client> getById(@PathVariable Long ruc_dni);
}
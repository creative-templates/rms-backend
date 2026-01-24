package com.restaurant.ms.outlet.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.ms.core.payloads.GeneralResponse;
import com.restaurant.ms.core.repositories.OutletRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/outlet")
@RequiredArgsConstructor
public class OutletController {
  private final OutletRepository outletRepository;

  @GetMapping("info")
  public ResponseEntity<GeneralResponse> getInfo(@RequestParam("id") UUID id) {
    return ResponseEntity.ok().body(new GeneralResponse(outletRepository.findById(id)));
  }

}

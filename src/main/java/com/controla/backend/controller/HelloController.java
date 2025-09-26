package com.controla.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {  // <- isto é a classe

    @GetMapping("/public/hello")
    public String hello() {      // <- este é um método da classe
        return "Olá do Controller!";
    }
}

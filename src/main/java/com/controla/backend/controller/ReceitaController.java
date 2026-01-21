package com.controla.backend.controller;


import com.controla.backend.dto.ReceitaRequestDTO;
import com.controla.backend.dto.ReceitaResponseDTO;
import com.controla.backend.entity.Receita;
import com.controla.backend.service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {
    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;

    }

    @PostMapping
    public ResponseEntity<ReceitaResponseDTO> cadastrar(@RequestBody @Valid ReceitaRequestDTO dto)  {

        Receita receita = new Receita();

        receita.setValor(dto.getValor());
        receita.setData(dto.getData());
        receita.setObservacao(dto.getObservacao());
        receita.setCategoria(dto.getCategoria());
        receita.setDescricao(dto.getDescricao());

        Receita receitaSalva = receitaService.cadastrarReceita(receita);


        ReceitaResponseDTO response = new ReceitaResponseDTO(
                receitaSalva.getId(),
                receitaSalva.getValor(),
                receitaSalva.getData(),
                receitaSalva.getCategoria(),
                receitaSalva.getDescricao(),
                receitaSalva.getObservacao()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }
}
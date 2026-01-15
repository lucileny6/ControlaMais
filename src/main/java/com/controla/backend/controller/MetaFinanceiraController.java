package com.controla.backend.controller;

import com.controla.backend.dto.MetaFinanceiraRequestDTO;
import com.controla.backend.dto.MetaFinanceiraResponseDTO;
import com.controla.backend.entity.MetaFinanceira;
import com.controla.backend.service.MetaFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;




@RestController
@RequestMapping ("/meta")

public class MetaFinanceiraController{
    private final MetaFinanceiraService metaFinanceiraService;


    public MetaFinanceiraController(MetaFinanceiraService metaFinanceiraService) {
        this.metaFinanceiraService = metaFinanceiraService;

    }
    @PostMapping
    public ResponseEntity<MetaFinanceiraResponseDTO>CriarMeta(@RequestBody @Valid MetaFinanceiraRequestDTO dto) {

        MetaFinanceira metaSalva =
                metaFinanceiraService.criarMeta(dto.getNome(), dto.getValorMeta());

        MetaFinanceiraResponseDTO response = new MetaFinanceiraResponseDTO(
                metaSalva.getId(),
                metaSalva.getValorMeta(),
                metaSalva.getNome(),
                metaSalva.isAtiva());


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}

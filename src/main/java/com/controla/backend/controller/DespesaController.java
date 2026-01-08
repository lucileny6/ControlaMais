package com.controla.backend.controller;

import com.controla.backend.dto.DespesaRequestDTO;
import com.controla.backend.dto.DespesaResponseDTO;
import com.controla.backend.entity.Despesa;
import com.controla.backend.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/despesa")

public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController( DespesaService despesaService) {

        this.despesaService = despesaService;
    }
    @PostMapping
    public ResponseEntity<DespesaResponseDTO> cadastrar(@RequestBody @Valid DespesaRequestDTO dto) {
        Despesa despesa = new Despesa();

        despesa.setValor(dto.getValor());
        despesa.setCategoria(dto.getCategoria());
        despesa.setData(dto.getData());
        despesa.setObservacao(dto.getObservacao());
        despesa.setDescricao(dto.getDescricao());

        Despesa despesaSalva = despesaService.cadastrarDespesa(despesa);

        DespesaResponseDTO response = new DespesaResponseDTO(
                despesaSalva.getId(),
                despesaSalva.getValor(),
                despesaSalva.getData(),
                despesaSalva.getCategoria(),
                despesaSalva.getDescricao(),
                despesaSalva.getObservacao()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);



    }


}

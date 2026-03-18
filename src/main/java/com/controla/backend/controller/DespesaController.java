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
@RequestMapping("/api/despesas")

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
        despesa.setRecorrente(dto.getRecorrente());

        Despesa despesaSalva = despesaService.cadastrarDespesa(despesa);

        DespesaResponseDTO response = new DespesaResponseDTO(
                despesaSalva.getId(),
                despesaSalva.getValor(),
                despesaSalva.getData(),
                despesaSalva.getCategoria(),
                despesaSalva.getDescricao(),
                despesaSalva.getObservacao(),
                despesaSalva.getRecorrente()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);



    }
    @PutMapping("/{id}")
    public ResponseEntity<DespesaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DespesaRequestDTO dto) {

        Despesa despesaAtualizada = despesaService.atualizarDespesa(id, dto);

        DespesaResponseDTO response = new DespesaResponseDTO(
                despesaAtualizada.getId(),
                despesaAtualizada.getValor(),
                despesaAtualizada.getData(),
                despesaAtualizada.getCategoria(),
                despesaAtualizada.getDescricao(),
                despesaAtualizada.getObservacao(),
                despesaAtualizada.getRecorrente()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        despesaService.deletarDespesa(id);
        return ResponseEntity.noContent().build();
    }


}



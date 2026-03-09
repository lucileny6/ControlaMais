package com.controla.backend.controller;

import com.controla.backend.dto.MetaFinanceiraRequestDTO;
import com.controla.backend.dto.MetaFinanceiraResponseDTO;
import com.controla.backend.entity.MetaFinanceira;
import com.controla.backend.service.MetaFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
public class MetaFinanceiraController {
    private final MetaFinanceiraService metaFinanceiraService;

    public MetaFinanceiraController(MetaFinanceiraService metaFinanceiraService) {
        this.metaFinanceiraService = metaFinanceiraService;
    }

    @PostMapping
    public ResponseEntity<MetaFinanceiraResponseDTO> criarMeta(@RequestBody @Valid MetaFinanceiraRequestDTO dto) {

        MetaFinanceira metaSalva =
                metaFinanceiraService.criarMeta(dto.getNome(), dto.getValorMeta(), dto.getDescricao(), dto.getPrazo());

        MetaFinanceiraResponseDTO response = new MetaFinanceiraResponseDTO(
                metaSalva.getId(),
                metaSalva.getValorMeta(),
                metaSalva.getNome(),
                metaSalva.getDescricao(),
                metaSalva.getPrazo(),
                metaSalva.isAtiva()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaFinanceiraResponseDTO> atualizarMeta(
            @PathVariable Long id,
            @RequestBody @Valid MetaFinanceiraRequestDTO dto
    ) {
        MetaFinanceira metaAtualizada =
                metaFinanceiraService.atualizarMeta(id, dto.getNome(), dto.getValorMeta(), dto.getDescricao(), dto.getPrazo());

        MetaFinanceiraResponseDTO response = new MetaFinanceiraResponseDTO(
                metaAtualizada.getId(),
                metaAtualizada.getValorMeta(),
                metaAtualizada.getNome(),
                metaAtualizada.getDescricao(),
                metaAtualizada.getPrazo(),
                metaAtualizada.isAtiva()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMeta(@PathVariable Long id) {
        metaFinanceiraService.excluirMeta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MetaFinanceiraResponseDTO>> listarMetas() {
        List<MetaFinanceiraResponseDTO> metas = metaFinanceiraService.listarMetasUsuario();
        return ResponseEntity.ok(metas);
    }
}

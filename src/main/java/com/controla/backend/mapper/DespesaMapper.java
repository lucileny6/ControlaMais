package com.controla.backend.mapper;

import com.controla.backend.dto.DespesaRequestDTO;
import com.controla.backend.dto.DespesaResponseDTO;
import com.controla.backend.entity.Despesa;

public class DespesaMapper {
    public static Despesa toEntity(DespesaRequestDTO dto) {

        Despesa d = new Despesa();

        d.setValor(dto.getValor());
        d.setCategoria(dto.getCategoria());
        d.setData(dto.getData());
        d.setDescricao(dto.getDescricao());
        d.setObservacao(dto.getObservacao());
        d.setRecorrente(dto.getRecorrente());

        return d;


    }
    public static DespesaResponseDTO toDTO(Despesa despesa){
        return new DespesaResponseDTO(
                despesa.getId(),
                despesa.getValor(),
                despesa.getData(),
                despesa.getCategoria(),
                despesa.getDescricao(),
                despesa.getObservacao(),
                despesa.getRecorrente()
        );
    }
}

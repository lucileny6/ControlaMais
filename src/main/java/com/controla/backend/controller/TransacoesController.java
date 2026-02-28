package com.controla.backend.controller;

import com.controla.backend.dto.DashboardTransactionDTO;
import com.controla.backend.service.TransacoesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value={"/api/transaction", "/api/transacoes"})
public class TransacoesController {

    private final TransacoesService transacoesService;

    public TransacoesController(TransacoesService transacoesService) {
        this.transacoesService = transacoesService;
    }
   @GetMapping
    public List<DashboardTransactionDTO> listar() {
       return transacoesService.listarTodasTransacoes();
   }
}

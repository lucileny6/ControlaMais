package com.controla.backend.service;

import com.controla.backend.dto.DashboardDTO;
import com.controla.backend.repository.DespesaRepository;
import com.controla.backend.repository.ReceitaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
public class DashboardService {

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;

    public DashboardService(ReceitaRepository receitaRepository,
                            DespesaRepository despesaRepository) {
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
    }

        public DashboardDTO getDashboard() {

            // 1. Descobrir quem é o usuário logado
            String emailUsuarioLogado =
                    SecurityContextHolder.getContext().getAuthentication().getName();

            // 2. Buscar a soma das despesas no banco
            BigDecimal totalReceitas = receitaRepository.somarReceitasPorUsuario(emailUsuarioLogado);

            //3. Buscar a soma das despesas no banco
            BigDecimal totalDespesas = despesaRepository.somarDespesasPorUsuario(emailUsuarioLogado);

            // 4. Tratar null (quando não existe nenhuma receita ou despesa)
            if (totalReceitas == null) {
                totalReceitas = BigDecimal.ZERO;
            }
            if (totalDespesas == null) {
                totalDespesas = BigDecimal.ZERO;
            }
            // 5. Calcular o saldo
            BigDecimal saldo = totalReceitas.subtract(totalDespesas);

            // 6. Montar o DTO do Dashboard
            return new DashboardDTO(
                    saldo,
                    totalReceitas,
                    totalDespesas,
                    BigDecimal.ZERO,          // meta (por enquanto)
                    BigDecimal.ZERO,          // poupanca (por enquanto)
                    Collections.emptyList(),  // transações recentes (depois)
                    Collections.emptyList()   // ações rápidas (depois)
            );
        }
}






package com.controla.backend.service;

import com.controla.backend.dto.AcaoFinanceiraDTO;
import com.controla.backend.dto.ChatIARequestDTO;
import com.controla.backend.dto.ChatIAResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ChatIAService {

    public ChatIAResponseDTO processarMensagem(ChatIARequestDTO request) {

        String mensagem = request.getMensagem().toLowerCase();

        // Caso: despesa identificada
        if (mensagem.contains("gastei") || mensagem.contains("paguei")) {

            // Monta a ação financeira
            AcaoFinanceiraDTO acao = new AcaoFinanceiraDTO();
            acao.setTipo(AcaoFinanceiraDTO.TipoAcaoFinanceira.DESPESA);
            acao.setValor(new BigDecimal("50.00"));
            acao.setCategoria("Alimentação");
            acao.setDescricao("Despesa identificada pelo Chat IA");
            acao.setObservacao(null);

            // Monta a resposta
            ChatIAResponseDTO response = new ChatIAResponseDTO();
            response.setTipo(ChatIAResponseDTO.ChatIAResponseType.CONFIRMACAO);
            response.setMensagem(
                    "Entendi que você deseja cadastrar uma despesa no valor de R$ 50,00. Confirma?"
            );

            // (no próximo passo vamos anexar a ação ao response)
            return response;
        }

        // Caso padrão: apenas texto
        ChatIAResponseDTO response = new ChatIAResponseDTO();
        response.setTipo(ChatIAResponseDTO.ChatIAResponseType.TEXTO);
        response.setMensagem(
                "Posso te ajudar a registrar receitas, despesas ou analisar seus gastos."
        );

        return response;
    }
}

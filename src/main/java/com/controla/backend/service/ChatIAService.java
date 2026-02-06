package com.controla.backend.service;

import com.controla.backend.dto.AcaoFinanceiraDTO;
import com.controla.backend.dto.ChatIARequestDTO;
import com.controla.backend.dto.ChatIAResponseDTO;
import com.controla.backend.entity.AcaoFinanceira;
import com.controla.backend.entity.TipoAcaoFinanceira;
import com.controla.backend.repository.AcaoFinanceiraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatIAService {

    private final AcaoFinanceiraRepository acaoFinanceiraRepository;

    // ação pendente (memória temporária do chat)
    private AcaoFinanceiraDTO acaoPendente;

    public ChatIAService(AcaoFinanceiraRepository acaoFinanceiraRepository) {
        this.acaoFinanceiraRepository = acaoFinanceiraRepository;
    }

    public ChatIAResponseDTO processarMensagem(ChatIARequestDTO request) {

        String mensagem = request.getMensagem().toLowerCase();

        // 1️⃣ Se existe ação pendente → tratar confirmação
        if (acaoPendente != null) {
            return processarConfirmacao(mensagem);
        }

        // 2️⃣ DESPESA
        if (ehDespesa(mensagem)) {
            return iniciarAcao(TipoAcaoFinanceira.DESPESA, mensagem);
        }

        // 3️⃣ RECEITA
        if (ehReceita(mensagem)) {
            return iniciarAcao(TipoAcaoFinanceira.RECEITA, mensagem);
        }

        return respostaTexto(
                "Posso te ajudar a registrar receitas ou despesas 😊"
        );
    }

    // =========================
    // INICIAR AÇÃO
    // =========================
    private ChatIAResponseDTO iniciarAcao(
            TipoAcaoFinanceira tipo,
            String mensagem
    ) {
        BigDecimal valor = extrairValor(mensagem);

        if (valor == null) {
            return respostaTexto(
                    "Entendi a intenção, mas não identifiquei o valor. Pode informar?"
            );
        }

        acaoPendente = new AcaoFinanceiraDTO();
        acaoPendente.setTipo(tipo);
        acaoPendente.setValor(valor);
        acaoPendente.setDescricao("Registrado via chat");
        acaoPendente.setCategoria(
                tipo == TipoAcaoFinanceira.DESPESA ? "Alimentação" : "Renda"
        );

        return respostaConfirmacao(tipo, valor);
    }

    // =========================
    // CONFIRMAÇÃO
    // =========================
    private ChatIAResponseDTO processarConfirmacao(String mensagem) {

        if (mensagem.contains("sim") || mensagem.contains("confirmar")) {

            AcaoFinanceira entity = new AcaoFinanceira();
            entity.setTipo(acaoPendente.getTipo());
            entity.setValor(acaoPendente.getValor());
            entity.setCategoria(acaoPendente.getCategoria());
            entity.setDescricao(acaoPendente.getDescricao());
            entity.setData(LocalDate.now());

            acaoFinanceiraRepository.save(entity);

            acaoPendente = null;

            return respostaTexto("Registro salvo com sucesso ✅");
        }

        if (mensagem.contains("não") || mensagem.contains("cancelar")) {
            acaoPendente = null;
            return respostaTexto("Tudo bem, não registrei nada 😊");
        }

        return respostaTexto("Você confirma ou cancela?");
    }

    // =========================
    // REGRAS
    // =========================
    private boolean ehDespesa(String mensagem) {
        return mensagem.contains("gastei")
                || mensagem.contains("paguei")
                || mensagem.contains("comprei");
    }

    private boolean ehReceita(String mensagem) {
        return mensagem.contains("recebi")
                || mensagem.contains("ganhei")
                || mensagem.contains("entrou");
    }

    private BigDecimal extrairValor(String mensagem) {
        Pattern pattern = Pattern.compile("(\\d+(?:[\\.,]\\d{1,2})?)");
        Matcher matcher = pattern.matcher(mensagem);

        if (matcher.find()) {
            return new BigDecimal(matcher.group(1).replace(",", "."));
        }
        return null;
    }

    // =========================
    // RESPOSTAS
    // =========================
    private ChatIAResponseDTO respostaTexto(String mensagem) {
        ChatIAResponseDTO response = new ChatIAResponseDTO();
        response.setTipo(ChatIAResponseDTO.ChatIAResponseType.TEXTO);
        response.setMensagem(mensagem);
        return response;
    }

    private ChatIAResponseDTO respostaConfirmacao(
            TipoAcaoFinanceira tipo,
            BigDecimal valor
    ) {
        ChatIAResponseDTO response = new ChatIAResponseDTO();
        response.setTipo(ChatIAResponseDTO.ChatIAResponseType.CONFIRMACAO);
        response.setMensagem(
                "Deseja confirmar esta "
                        + tipo.name().toLowerCase()
                        + " no valor de R$ "
                        + valor.setScale(2)
                        + "?"
        );
        return response;
    }
}

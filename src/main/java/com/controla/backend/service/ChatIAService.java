package com.controla.backend.service;

import com.controla.backend.dto.AcaoFinanceiraDTO;
import com.controla.backend.dto.ChatIARequestDTO;
import com.controla.backend.dto.ChatIAResponseDTO;
import com.controla.backend.entity.AcaoFinanceira;
import com.controla.backend.entity.TipoAcaoFinanceira;
import com.controla.backend.entity.User;
import com.controla.backend.repository.AcaoFinanceiraRepository;
import com.controla.backend.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatIAService {

    private final AcaoFinanceiraRepository acaoFinanceiraRepository;
    private final UserRepository userRepository;

    //  memória temporária do chat
    private AcaoFinanceiraDTO acaoPendente;

    public ChatIAService(
            AcaoFinanceiraRepository acaoFinanceiraRepository,
            UserRepository userRepository
    ) {
        this.acaoFinanceiraRepository = acaoFinanceiraRepository;
        this.userRepository = userRepository;
    }

    public ChatIAResponseDTO processarMensagem(ChatIARequestDTO request) {

        String mensagem = request.getMensagem().toLowerCase();


        if (acaoPendente != null) {
            return processarConfirmacao(mensagem);
        }


        if (ehDespesa(mensagem)) {
            return iniciarAcao(TipoAcaoFinanceira.DESPESA, mensagem);
        }


        if (ehReceita(mensagem)) {
            return iniciarAcao(TipoAcaoFinanceira.RECEITA, mensagem);
        }

        return respostaTexto("Posso te ajudar a registrar receitas ou despesas 😊");
    }


    private ChatIAResponseDTO iniciarAcao(
            TipoAcaoFinanceira tipo,
            String mensagem
    ) {
        BigDecimal valor = extrairValor(mensagem);

        if (valor == null) {
            return respostaTexto("Não identifiquei o valor. Pode informar?");
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


    private ChatIAResponseDTO processarConfirmacao(String mensagem) {


        if (mensagem.contains("sim") || mensagem.contains("confirmar")) {

            // 🔐 Usuário logado via JWT
            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            String email = auth.getName();

            User usuario = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            AcaoFinanceira entity = new AcaoFinanceira();
            entity.setTipo(acaoPendente.getTipo());
            entity.setValor(acaoPendente.getValor());
            entity.setCategoria(acaoPendente.getCategoria());
            entity.setDescricao(acaoPendente.getDescricao());
            entity.setData(LocalDate.now());

            // ⭐ ESSENCIAL
            entity.setUsuario(usuario);

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
            return new BigDecimal(
                    matcher.group(1).replace(",", ".")
            );
        }

        return null;
    }

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

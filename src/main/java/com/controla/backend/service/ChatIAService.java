package com.controla.backend.service;

import com.controla.backend.dto.AcaoFinanceiraDTO;
import com.controla.backend.dto.ChatIARequestDTO;
import com.controla.backend.dto.ChatIAResponseDTO;
import com.controla.backend.entity.Despesa;
import com.controla.backend.entity.Receita;
import com.controla.backend.entity.TipoAcaoFinanceira;
import com.controla.backend.entity.User;
import com.controla.backend.repository.DespesaRepository;
import com.controla.backend.repository.ReceitaRepository;
import com.controla.backend.repository.UserRepository;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatIAService {

    private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");
    private static final DateTimeFormatter MONTH_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM/yyyy", LOCALE_PT_BR);

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;
    private final UserRepository userRepository;
    private final OpenAIService openAIService;
    private final DashboardService dashboardService;


    private final Map<String, AcaoFinanceiraDTO> memoria = new ConcurrentHashMap<>();

    public ChatIAService(
            ReceitaRepository receitaRepository,
            DespesaRepository despesaRepository,
            UserRepository userRepository,
            OpenAIService openAIService,
            DashboardService dashboardservice
    ) {
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
        this.userRepository = userRepository;
        this.openAIService = openAIService;
        this.dashboardService = dashboardservice;
    }

    public ChatIAResponseDTO processarMensagem(ChatIARequestDTO request) {

        String mensagem = request != null && request.getMensagem() != null
                ? request.getMensagem().trim()
                : "";

        String email = getUsuarioLogado();

        if (mensagem.isBlank()) {
            return respostaTexto("Me diga uma receita, despesa ou consulta.");
        }

        // fluxo de confirmação
        if (memoria.containsKey(email)) {
            return processarConfirmacao(mensagem, email);
        }

        // saudação
        if (isSaudacao(mensagem)) {
            return respostaTexto("Olá! Posso registrar receitas, despesas ou consultar seu saldo 😊");
        }

        // IA
        AcaoFinanceiraDTO acao = openAIService.interpretarMensagem(mensagem);

        if (acao == null || acao.getAcao() == null) {
            return respostaTexto("Não consegui entender. Pode tentar novamente?");
        }

        String tipoAcao = normalizeTag(acao.getAcao());

        // 🔥 CONSULTA
        if ("CONSULTAR_SALDO".equals(tipoAcao)) {
            return consultarSaldo(email);
        }

        // 🔥 CRIAÇÃO
        TipoAcaoFinanceira tipo = resolveTipoAcao(tipoAcao);

        if (tipo == null
                || acao.getValor() == null
                || acao.getValor().compareTo(BigDecimal.ZERO) <= 0) {

            return respostaTexto("Informe um valor válido 😊");
        }

        acao.setTipo(tipo);

        if (acao.getDescricao() == null || acao.getDescricao().isBlank()) {
            acao.setDescricao("Registro via chat");
        }

        if (acao.getCategoria() == null || acao.getCategoria().isBlank()) {
            acao.setCategoria(tipo == TipoAcaoFinanceira.DESPESA ? "Outros" : "Renda");
        }

        if (acao.getData() == null) {
            acao.setData(LocalDate.now());
        }

        memoria.put(email, acao);

        return respostaConfirmacao(acao);
    }

    // =========================
    // CONSULTA DE SALDO
    // =========================
    private ChatIAResponseDTO consultarSaldo(String email) {

        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();

        BigDecimal receitas = receitaRepository
                .somarReceitasPorUsuarioNoPeriodo(email, inicio, fim);

        BigDecimal despesas = despesaRepository
                .somarDespesasPorUsuarioNoPeriodo(email, inicio, fim);

        // 🔥 tratamento de null (ESSENCIAL)
        if (receitas == null) receitas = BigDecimal.ZERO;
        if (despesas == null) despesas = BigDecimal.ZERO;

        BigDecimal saldo = receitas.subtract(despesas);

        String mes = capitalize(mesAtual.format(MONTH_FORMATTER));

        return respostaTexto(
                "Resumo de " + mes + ":\n\n"
                        + "Receitas: " + formatCurrency(receitas) + "\n"
                        + "Despesas: " + formatCurrency(despesas) + "\n"
                        + "Saldo: " + formatCurrency(saldo)
        );
    }

    // =========================
    // CONFIRMAÇÃO
    // =========================
    private ChatIAResponseDTO processarConfirmacao(String mensagem, String email) {

        AcaoFinanceiraDTO acao = memoria.get(email);

        if (acao == null) {
            return respostaTexto("Nenhuma ação pendente.");
        }

        String msg = normalizeText(mensagem);

        if (msg.contains("sim") || msg.contains("confirmar")) {

            User usuario = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            salvar(acao, usuario);
            memoria.remove(email);

            return respostaTexto("Registro salvo com sucesso ✅");
        }

        if (msg.contains("nao") || msg.contains("cancelar")) {
            memoria.remove(email);
            return respostaTexto("Tudo bem, não registrei 😊");
        }

        return respostaTexto("Você confirma ou cancela?");
    }

    private void salvar(AcaoFinanceiraDTO acao, User usuario) {

        if (acao.getTipo() == TipoAcaoFinanceira.RECEITA) {

            Receita r = new Receita();
            r.setUser(usuario);
            r.setValor(acao.getValor());
            r.setCategoria(acao.getCategoria());
            r.setDescricao(acao.getDescricao());
            r.setData(acao.getData());

            receitaRepository.save(r);
            return;
        }

        Despesa d = new Despesa();
        d.setUser(usuario);
        d.setValor(acao.getValor());
        d.setCategoria(acao.getCategoria());
        d.setDescricao(acao.getDescricao());
        d.setData(acao.getData());

        despesaRepository.save(d);
    }

    // =========================
    // HELPERS
    // =========================
    private String getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private TipoAcaoFinanceira resolveTipoAcao(String acao) {
        if ("CRIAR_DESPESA".equals(acao)) return TipoAcaoFinanceira.DESPESA;
        if ("CRIAR_RECEITA".equals(acao)) return TipoAcaoFinanceira.RECEITA;
        return null;
    }

    private ChatIAResponseDTO respostaTexto(String msg) {
        ChatIAResponseDTO r = new ChatIAResponseDTO();
        r.setTipo(ChatIAResponseDTO.ChatIAResponseType.TEXTO);
        r.setMensagem(msg);
        return r;
    }

    private ChatIAResponseDTO respostaConfirmacao(AcaoFinanceiraDTO acao) {
        ChatIAResponseDTO r = new ChatIAResponseDTO();
        r.setTipo(ChatIAResponseDTO.ChatIAResponseType.CONFIRMACAO);

        r.setMensagem(
                "Deseja confirmar esta "
                        + acao.getTipo().name().toLowerCase()
                        + " de "
                        + formatCurrency(acao.getValor())
                        + "?"
        );

        return r;
    }

    private boolean isSaudacao(String msg) {
        String t = normalizeText(msg);
        return t.equals("oi") || t.equals("ola") || t.equals("bom dia");
    }

    private String normalizeTag(String v) {
        return v == null ? "" : normalizeText(v).replace(" ", "_").toUpperCase();
    }

    private String normalizeText(String v) {
        String t = v == null ? "" : v.toLowerCase();
        return Normalizer.normalize(t, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim();
    }

    private String formatCurrency(BigDecimal v) {
        return NumberFormat.getCurrencyInstance(LOCALE_PT_BR).format(v);
    }

    private String capitalize(String t) {
        return t.substring(0, 1).toUpperCase() + t.substring(1);
    }
}

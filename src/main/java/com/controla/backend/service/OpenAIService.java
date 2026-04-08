package com.controla.backend.service;

import com.controla.backend.dto.AcaoFinanceiraDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public AcaoFinanceiraDTO interpretarMensagem(String mensagem) {
        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);

            String prompt = """
Interprete a mensagem do usuario e devolva somente um JSON valido, sem markdown.

Use exatamente uma destas acoes:
- CRIAR_DESPESA
- CRIAR_RECEITA
- CONSULTAR_SALDO
- NENHUMA

Regras:
- Para registrar despesa, use acao CRIAR_DESPESA e tipo DESPESA.
- Para registrar receita, use acao CRIAR_RECEITA e tipo RECEITA.
- Para consultar o total gasto no mes, use acao CONSULTAR_SALDO e tipo DESPESA.
- Para consultar o total recebido no mes, use acao CONSULTAR_SALDO e tipo RECEITA.
- Para consultar saldo total do mes, use acao CONSULTAR_SALDO e tipo null.
- Se nao houver intencao financeira clara, use acao NENHUMA.
- Se o usuario nao informar valor numa criacao, use valor null.
- Se o usuario nao informar data, use data null.
- Descricao deve ser curta e objetiva.
- Categoria deve ser curta e objetiva.

Formato esperado:
{
  "acao": "CRIAR_DESPESA | CRIAR_RECEITA | CONSULTAR_SALDO | NENHUMA",
  "tipo": "DESPESA | RECEITA | null",
  "data": "YYYY-MM-DD ou null",
  "valor": 0,
  "descricao": "texto ou null",
  "observacao": "texto ou null",
  "categoria": "texto ou null"
}

Mensagem do usuario:
%s
""".formatted(mensagem);

            String body = objectMapper.writeValueAsString(
                    java.util.Map.of(
                            "model", "gpt-4o-mini",
                            "messages", java.util.List.of(
                                    java.util.Map.of(
                                            "role", "system",
                                            "content", "Voce extrai intencoes financeiras e responde somente JSON valido."
                                    ),
                                    java.util.Map.of(
                                            "role", "user",
                                            "content", prompt
                                    )
                            ),
                            "temperature", 0
                    )
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            InputStream responseStream = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
            if (responseStream == null) {
                return null;
            }

            String response = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);
            if (status >= 400) {
                System.err.println("Erro OpenAI: " + response);
                return null;
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.isEmpty()) {
                return null;
            }

            String content = choices
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            String cleanJson = sanitizeJson(content);
            return objectMapper.readValue(cleanJson, AcaoFinanceiraDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String sanitizeJson(String content) {
        if (content == null) {
            return "{}";
        }

        String sanitized = content.trim();
        if (sanitized.startsWith("```")) {
            sanitized = sanitized.replaceFirst("^```json\\s*", "");
            sanitized = sanitized.replaceFirst("^```\\s*", "");
            sanitized = sanitized.replaceFirst("\\s*```$", "");
        }

        int firstBrace = sanitized.indexOf('{');
        int lastBrace = sanitized.lastIndexOf('}');
        if (firstBrace >= 0 && lastBrace > firstBrace) {
            sanitized = sanitized.substring(firstBrace, lastBrace + 1);
        }

        return sanitized;
    }
}

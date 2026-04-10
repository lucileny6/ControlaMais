package com.controla.backend.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAIConfig {
    @Value("${openai.api.key:}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public boolean isConfigured() {
        return apiKey != null &&  !apiKey.trim().isEmpty();
    }



}

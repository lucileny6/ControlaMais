package com.controla.backend.controller;

import com.controla.backend.dto.ChatIARequestDTO;
import com.controla.backend.dto.ChatIAResponseDTO;
import com.controla.backend.service.ChatIAService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/chat-ia")
    public class ChatIAController {

        private final ChatIAService chatIAService;

        public ChatIAController(ChatIAService chatIAService) {
            this.chatIAService = chatIAService;
        }

        @PostMapping
        public ResponseEntity<ChatIAResponseDTO> conversar(
                @RequestBody ChatIARequestDTO request
        ) {
            ChatIAResponseDTO response = chatIAService.processarMensagem(request);
            return ResponseEntity.ok(response);
        }
    }



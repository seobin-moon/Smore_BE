package com.meossamos.smore.domain.chat.message.controller;

import com.meossamos.smore.domain.chat.message.dto.ChatMessageRequestDto;
import com.meossamos.smore.domain.chat.message.dto.ChatMessageResponseDto;
import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(ChatMessageRequestDto messageDto, Principal principal) {
        String senderId = null;

        if (principal != null && !"anonymousUser".equals(principal.getName())) {
            senderId = principal.getName();
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    !"anonymousUser".equals(authentication.getName())) {
                senderId = authentication.getName();
            }
        }

        if (senderId == null) {
            log.warn("⚠️ 인증 정보를 찾을 수 없음! 기본 senderId 설정");
            senderId = "anonymous";
        }

        messageDto.setSenderId(senderId);

        ChatMessage savedMessage = chatMessageService.saveChatMessage(
                messageDto.getRoomId(),
                messageDto.getChatType(),
                senderId,
                messageDto.getMessage(),
                messageDto.getAttachment()
        );

        ChatMessageResponseDto response = ChatMessageResponseDto.builder()
                .messageId(savedMessage.getId())
                .roomId(savedMessage.getRoomId())
                .senderId(savedMessage.getSenderId())
                .message(savedMessage.getMessage())
                .attachment(savedMessage.getAttachment())
                .timestamp(savedMessage.getCreatedDate() != null ? savedMessage.getCreatedDate() : LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/chatroom/" + messageDto.getRoomId(), response);
        log.debug("📤 메시지 전송 완료: senderId={}, 대상={}", response.getSenderId(), "/topic/chatroom/" + messageDto.getRoomId());
    }
}

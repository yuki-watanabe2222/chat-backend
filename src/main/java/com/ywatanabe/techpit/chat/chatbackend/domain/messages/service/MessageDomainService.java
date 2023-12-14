package com.ywatanabe.techpit.chat.chatbackend.domain.messages.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ywatanabe.techpit.chat.chatbackend.domain.messages.model.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageDomainService {

  private final MessageRepository messageRepository;

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddhhmm");

  public Message post(Message message) {
    var uuid = UUID.randomUUID();
    var now = LocalDateTime.now();
    message.setId(now.format(FORMATTER) + "-" + uuid.toString());
    message.setTimestamp(now);
    messageRepository.insert(message);
    return message;
  }

  public List<Message> find(int channelId, Optional<String> searchWord) {
    return messageRepository.find(channelId, searchWord);
  }
}

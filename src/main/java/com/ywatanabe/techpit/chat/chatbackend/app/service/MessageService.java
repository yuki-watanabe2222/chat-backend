package com.ywatanabe.techpit.chat.chatbackend.app.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ywatanabe.techpit.chat.chatbackend.app.exception.TooManyRequestsException;
import com.ywatanabe.techpit.chat.chatbackend.domain.messages.model.Message;
import com.ywatanabe.techpit.chat.chatbackend.domain.messages.service.MessageDomainService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

  private final MessageDomainService messageDomainService;

  private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();

  @Retryable(value = Exception.class, maxAttempts = 6, backoff = @Backoff(delay = 100, maxDelay = 1000, multiplier = 2))
  public Message post(Message message) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();

    var bucket = resolvBucket(username);
    if (!bucket.tryConsume(1)) {
      throw new TooManyRequestsException();
    }

    message.setUsername(username);
    return messageDomainService.post(message);
  }

  public List<Message> find(int channelId, Optional<String> searchWord) {
    return messageDomainService.find(channelId, searchWord);
  }

  private Bucket resolvBucket(String username) {
    return bucketMap.computeIfAbsent(username, key -> {
      var refill = Refill.intervally(2, Duration.ofMinutes(1));
      var limit = Bandwidth.classic(2, refill);
      return Bucket.builder()
          .addLimit(limit)
          .build();
    });
  }
}

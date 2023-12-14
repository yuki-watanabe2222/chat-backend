package com.ywatanabe.techpit.chat.chatbackend.infrastructure.messages;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ywatanabe.techpit.chat.chatbackend.domain.messages.model.Message;

@Mapper
public interface MessageMapper {
  void insert(Message message);

  List<Message> find(@Param("channelId") int channelId, @Param("searchWord") Optional<String> searchWord);
}

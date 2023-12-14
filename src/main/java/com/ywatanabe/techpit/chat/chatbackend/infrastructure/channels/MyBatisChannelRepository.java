package com.ywatanabe.techpit.chat.chatbackend.infrastructure.channels;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ywatanabe.techpit.chat.chatbackend.domain.channels.model.Channel;
import com.ywatanabe.techpit.chat.chatbackend.domain.channels.service.ChannelRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisChannelRepository implements ChannelRepository {

  private final ChannelMapper channelMapper;

  @Override
  public void insert(Channel channel) {
    channelMapper.insert(channel);
  }

  @Override
  public List<Channel> findAll() {
    return channelMapper.findAll();
  }

  @Override
  public Optional<Integer> getMaxId() {
    return channelMapper.getMaxId();
  }

  @Override
  public int update(Channel channel) {
    return channelMapper.update(channel);
  }

  @Override
  public int delete(int id) {
    return channelMapper.delete(id);
  }
}

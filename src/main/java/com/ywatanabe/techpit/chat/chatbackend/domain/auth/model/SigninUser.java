package com.ywatanabe.techpit.chat.chatbackend.domain.auth.model;

import lombok.Data;

@Data
public class SigninUser {
  private String username;
  private String password;
}

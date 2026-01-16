package com.restaurant.ms.core.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender eMailSender;

  public void sendText(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("noreply@rms.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    eMailSender.send(message);
  }
}

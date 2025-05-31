package br.com.shdo.email_service.core;

public interface EmailSenderUseCase {
  void sendEmail(String to, String subject, String body);  
}

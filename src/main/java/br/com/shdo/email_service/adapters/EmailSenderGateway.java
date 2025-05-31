package br.com.shdo.email_service.adapters;

// Contract for sending emails
public interface EmailSenderGateway {
  void sendEmail(String to, String subject, String body);
}

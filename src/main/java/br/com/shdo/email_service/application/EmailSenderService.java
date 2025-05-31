package br.com.shdo.email_service.application;

import br.com.shdo.email_service.core.EmailSenderUseCase;
import br.com.shdo.email_service.adapters.EmailSenderGateway;

public class EmailSenderService implements EmailSenderUseCase {

  private final EmailSenderGateway emailSenderGateway;
 
  public EmailSenderService(EmailSenderGateway emailSenderGateway) {
    this.emailSenderGateway = emailSenderGateway;
  }

  @Override
  public void sendEmail(String to, String subject, String body) {
    this.emailSenderGateway.sendEmail(to, subject, body);
  }

}

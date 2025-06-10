package br.com.shdo.email_service.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.com.shdo.email_service.adapters.EmailSenderGateway;
import br.com.shdo.email_service.core.exceptions.EmailServiceException;
import br.com.shdo.email_service.infra.aws_ses.AwsSesEmailSender;
import br.com.shdo.email_service.infra.mailgun.MailgunEmailSender;

@Primary
@Component
public class FallbackEmailSenderGateway implements EmailSenderGateway {
  private final AwsSesEmailSender awsSesEmailSender;
  private final MailgunEmailSender emailSenderMailgun;

  @Autowired
  public FallbackEmailSenderGateway(AwsSesEmailSender awsSesEmailSender, MailgunEmailSender emailSenderMailgun) {
    this.awsSesEmailSender = awsSesEmailSender;
    this.emailSenderMailgun = emailSenderMailgun;
  }

  @Override
  public void sendEmail(String to, String subject, String body) {
    try {
      // Chama o método sendEmail do AwsSesEmailSender
      awsSesEmailSender.sendEmail(to, subject, body);
    } catch (EmailServiceException e) {
      // Caso falhe, tenta enviar usando MailGun
      emailSenderMailgun.sendEmail(to, subject, body);
    }
  }
}

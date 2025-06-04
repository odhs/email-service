package br.com.shdo.email_service.infra.aws_ses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import br.com.shdo.email_service.adapters.EmailSenderGateway;
import br.com.shdo.email_service.core.exceptions.EmailServiceException;

@Service
public class AwsSesEmailSender implements EmailSenderGateway {

  private final AmazonSimpleEmailService amazonSimpleEmailService;

  @Value("${email.source}")
  private String email;

  @Autowired
  public AwsSesEmailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
    this.amazonSimpleEmailService = amazonSimpleEmailService;
  }

  @Override
  public void sendEmail(String to, String subject, String body) {
    SendEmailRequest request = new SendEmailRequest()
        .withSource(email)
        .withDestination(new Destination().withToAddresses(to))
        .withMessage(new com.amazonaws.services.simpleemail.model.Message()
            .withSubject(new Content(subject))
            .withBody(new Body()
                .withText(new Content(body))));
    try {
      this.amazonSimpleEmailService.sendEmail(request);
    } catch (AmazonServiceException e) {
      throw new EmailServiceException("Failed to send email: " + e.getMessage(), e);
    }
  }

}

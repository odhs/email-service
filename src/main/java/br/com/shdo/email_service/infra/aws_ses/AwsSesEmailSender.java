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

/**
 * Service implementation for sending emails using AWS Simple Email Service (SES).
 * This class integrates with AWS SES to send emails with specified recipients, subjects, and bodies.
 * 
 * <p>Dependencies:
 * <ul>
 *   <li>AmazonSimpleEmailService: AWS SDK client for interacting with SES.</li>
 *   <li>Spring @Value annotation for injecting the email source from application properties.</li>
 * </ul>
 * 
 * <p>Usage:
 * <pre>
 * {@code
 * AwsSesEmailSender emailSender = new AwsSesEmailSender(amazonSimpleEmailService);
 * emailSender.sendEmail("recipient@example.com", "Subject", "Email body");
 * }
 * </pre>
 * 
 * <p>Throws:
 * <ul>
 *   <li>EmailServiceException: If the email fails to send due to an AWS service error.</li>
 * </ul>
 * 
 * <p>Configuration:
 * Ensure the following property is set in your application configuration:
 * <ul>
 *   <li><code>email.source</code>: The email address used as the sender.</li>
 * </ul>
 * 
 * @author Sérgio Oliveira
 * @since 1.0.0
 */
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

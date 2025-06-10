package br.com.shdo.email_service.infra.mailgun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//import com.mashape.unirest.http.HttpResponse; // unirest v1.4.9
//import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import br.com.shdo.email_service.adapters.EmailSenderGateway;
import br.com.shdo.email_service.core.exceptions.EmailServiceException;

/**
 * Sends a simple email message using Mailgun API.
 * 
 * <p>
 * This method constructs an HTTP POST request to the Mailgun API to send an
 * email.
 * It uses basic authentication with the API key and sets the necessary
 * parameters
 * such as sender, recipient, subject, and body of the email.
 * 
 */
@Service
public class MailgunEmailSender implements EmailSenderGateway {

  @Value("${mailgun.secretKey}")
  private String apiKey;

  @Override
  public void sendEmail(String to, String subject, String body) {
    try {
      //HttpResponse<JsonNode> response = Unirest
      Unirest
          .post("https://api.mailgun.net/v3/sandbox71f1695b8569494c9f1520fd69a8feb4.mailgun.org/messages")
          .basicAuth("api", apiKey)
          .queryString("from", "Mailgun Sandbox <postmaster@sandbox71f1695b8569494c9f1520fd69a8feb4.mailgun.org>")
          .queryString("to", to)
          .queryString("subject", subject)
          .queryString("text", "body")
          .asJson();

      //response.getBody();

    } catch (UnirestException e) {
      throw new EmailServiceException("Failed to send email by Mailgun: " + e.getMessage(), e);
    }
  }
}
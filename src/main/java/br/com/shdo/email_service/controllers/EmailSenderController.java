package br.com.shdo.email_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.shdo.email_service.application.EmailSenderService;
import br.com.shdo.email_service.core.EmailRequest;
import br.com.shdo.email_service.core.exceptions.EmailServiceException;

@RestController
@RequestMapping("/api/v1/email")
public class EmailSenderController{
  
  private final EmailSenderService emailSenderService;
  
  @Autowired
  public EmailSenderController(EmailSenderService emailSenderService) {
    this.emailSenderService = emailSenderService;
  }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request){
      try {
        emailSenderService.sendEmail(request.to(), request.subject(), request.body());
        return ResponseEntity.ok("Email sent successfully");
      } catch (EmailServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sent email: " + e.getMessage());
      } 
    }


  
}

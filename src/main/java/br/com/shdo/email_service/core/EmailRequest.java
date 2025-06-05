package br.com.shdo.email_service.core;

public record EmailRequest(String to, String subject, String body) {

    public EmailRequest {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject cannot be null or empty");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Email body cannot be null or empty");
        }
    }
}

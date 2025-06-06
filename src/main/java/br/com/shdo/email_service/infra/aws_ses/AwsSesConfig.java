package br.com.shdo.email_service.infra.aws_ses;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

/**
 * Configuration class for AWS SES (Simple Email Service).
 * 
 * This class sets up the necessary AWS SES client using credentials and region
 * specified in the application's properties file.
 * 
 * Properties:
 * - aws.region: The AWS region where the SES service is hosted.
 * - aws.accessKeyId: The AWS access key ID for authentication.
 * - aws.secretKey: The AWS secret key for authentication.
 * 
 * Beans:
 * - AmazonSimpleEmailService: Configures and provides an instance of 
 *   AmazonSimpleEmailService for sending emails using AWS SES.
 * 
 * Dependencies:
 * - AWS SDK for Java.
 * - Spring Framework for dependency injection and configuration.
 */
@Configuration
public class AwsSesConfig {
  
  @Value("${aws.region}")
  private String awsRegion;

  @Value("${aws.accessKeyId}")
  private String accessKey;

  @Value("${aws.secretKey}")
  private String secretKey;

  @Bean
  public AmazonSimpleEmailService amazonSimpleEmailService() {
    BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

    return AmazonSimpleEmailServiceClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(awsRegion)
        .build();
  }
}
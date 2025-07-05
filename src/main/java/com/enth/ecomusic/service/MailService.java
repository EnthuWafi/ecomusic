package com.enth.ecomusic.service;

import java.time.LocalDate;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.util.AppConfig;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailService {
	private final Session session;

    public MailService(String host, int port, final String username, final String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        
        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(AppConfig.get("mailUsername")));
            message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8"); 

            Transport.send(message);

            System.out.println("Email sent to " + to);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    public String buildCancelSubscriptionEmail(UserDTO user, SubscriptionDTO subscription, String userMessage) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body>");
        sb.append("<h2>Subscription Cancellation Notice</h2>");
        sb.append("<p>Dear ").append(user.getFirstName()).append(",</p>");
        sb.append("<p>This is to confirm that your subscription to <strong>")
          .append(subscription.getSubscriptionPlan().getName()).append("</strong> has been cancelled successfully.</p>");

        sb.append("<p><strong>Subscription ID:</strong> ").append(subscription.getSubscriptionId()).append("<br>");
        sb.append("<strong>Cancellation Date:</strong> ").append(LocalDate.now()).append("</p>");

        if (StringUtils.isNotBlank(userMessage)) {
            sb.append("<p><strong>Your Message:</strong><br>").append(userMessage).append("</p>");
        }

        sb.append("<p>If this was not you or you have concerns, please reach out to our support team.</p>");

        sb.append("<hr>");
        sb.append("<small>This email is sent for educational/demo purposes only. If you believe this message was sent in error, please ignore it.</small>");
        sb.append("</body></html>");

        return sb.toString();
    }
}

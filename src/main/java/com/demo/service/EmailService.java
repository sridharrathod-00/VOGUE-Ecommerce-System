package com.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(String to, String fullName, String referenceId, double total) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlBody = "<div style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                                "<h1 style='text-align: center; border-bottom: 2px solid #000; padding-bottom: 10px;'>VOGUE.</h1>" +
                                "<p>Hi <strong>" + fullName + "</strong>,</p>" +
                                "<p>Thank you for your purchase! We are currently processing your order.</p>" +
                                "<div style='background: #f4f4f4; padding: 15px; border-radius: 5px;'>" +
                                    "<p><strong>Order ID:</strong> " + referenceId + "</p>" +
                                    "<p><strong>Amount Paid:</strong> ₹" + total + "</p>" +
                                    "<p><strong>Status:</strong> Processing</p>" +
                                "</div>" +
                                "<p>You will receive another update once your package has been shipped.</p>" +
                                "<p style='margin-top: 30px; font-size: 0.8em; color: #777;'>Stay Fashionable,<br>The VOGUE Team</p>" +
                              "</div>";

            helper.setTo(to);
            helper.setSubject("Your VOGUE Order Confirmation - " + referenceId);
            helper.setText(htmlBody, true); // true = isHtml

            mailSender.send(message);
        } catch (MessagingException e) {
            // We log the error but don't stop the user's checkout flow
            System.err.println("Email failed to send: " + e.getMessage());
        }
    }
}
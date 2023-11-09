package cooklyst.utils;

import javax.mail.*;
import javax.mail.internet.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Properties;

public class Email {
    private static Properties prop;
    private static Authenticator auth;
    private static String approvedEmail;
    private static String rejectedEmail;

    private static void setPropAuth() {
        prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", Env.SMTP_HOST);
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", Env.SMTP_HOST);
        auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Env.SMTP_USERNAME, Env.SMTP_PASSWORD);
            }
        };
    }

    public Email() {
        if (prop == null || auth == null) {
            setPropAuth();
        }

        if (rejectedEmail == null) {
            try (BufferedReader br = new BufferedReader(new FileReader(Paths.get("").toAbsolutePath() + "/src/main/java/cooklyst/utils/declined.html"));) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }

                rejectedEmail = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                rejectedEmail = "Your subscription is rejected.";
            }
        }

        if (approvedEmail == null) {
            try (BufferedReader br = new BufferedReader(new FileReader(Paths.get("").toAbsolutePath() + "/src/main/java/cooklyst/utils/approved.html"));) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }

                approvedEmail = sb.toString();
            } catch (Exception e) {
                approvedEmail = "Your subscription is approved.";
            }
        }
    }

    public void send(String receiverEmail, boolean isApproved) {
        Session session = Session.getInstance(prop, auth);
        try{
            MimeMessage message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.setFrom(new InternetAddress(Env.SENDER_EMAIL));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(receiverEmail));
            message.setSubject(isApproved ? "Subscription Approved" : "Subscription Rejected");
            message.setContent(isApproved ? approvedEmail : rejectedEmail, "text/html");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    // for testing purposes
    public static void main(String[] args) {
        Email e = new Email();
        e.send("hai@email.com", true);
    }
}



package co.edu.uniquindio.reservasapp.Utils;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import java.io.File;

public class EnvioEmail {

    // Existing method with attachment
    public static void enviarNotificacion(String destinatario, String asunto, String mensaje, File attachment) {
        DataSource dataSource = new FileDataSource(attachment);
        Email email = EmailBuilder.startingBlank()
                .from("bookyourstayuniqundio@gmail.com")
                .to(destinatario)
                .withSubject(asunto)
                .withPlainText(mensaje)
                .withAttachment("QRCode.png", dataSource)
                .buildEmail();

        try (Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "bookyourstayuniqundio@gmail.com", "your_email_password")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {

            mailer.sendMail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Overloaded method without attachment
    public static void enviarNotificacion(String destinatario, String asunto, String mensaje) {

        Email email = EmailBuilder.startingBlank()
                .from("bookyourstayuniqundio@gmail.com")
                .to(destinatario)
                .withSubject(asunto)
                .withPlainText(mensaje)
                .buildEmail();

        try (Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "bookyourstayuniqundio@gmail.com", "your_email_password")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {

            mailer.sendMail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*public class EnvioEmail {

    public static void enviarNotificacion(String destinatario, String asunto, String mensaje, File attachment) {

        Email email = EmailBuilder.startingBlank()
                .from("bookyourstayuniqundio@gmail.com")
                .to(destinatario)
                .withSubject(asunto)
                .withPlainText(mensaje)
                .withAttachment("QRCode.png", attachment)
                .buildEmail();


        try (Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "bookyourstayuniqundio@gmail.com", "fnee aryw abzk xjkh")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {

            mailer.sendMail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
package co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.utils;

import co.edu.uniquindio.reservasapp.plataforma.modelo.Factura;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import jakarta.mail.util.ByteArrayDataSource;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

/*public class EnvioEmail {

    public static void sendEmailWithQRCode(String toEmail, Factura factura, Reserva reserva, BufferedImage qrCodeImage) throws Exception {
        // Sender's email credentials
//        String host = "smtp.example.com";
        final String fromEmail = "your_email@example.com";
        final String password = "your_email_password";

        // Email SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com"); // Replace with your SMTP host
        props.put("mail.smtp.port", "587"); // Replace with your SMTP port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Get the Session object
        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Set Subject
            message.setSubject("Detalles de su Reserva - Factura #" + factura.getCodigo());

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(buildEmailBody(factura, reserva));

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment (QR Code)
            messageBodyPart = new MimeBodyPart();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            DataSource source = new ByteArrayDataSource(bytes, "image/png");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("QRCode.png");
            messageBodyPart.setHeader("Content-ID", "<qr>");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static String buildEmailBody(Factura factura, Reserva reserva) {
        StringBuilder sb = new StringBuilder();
        sb.append("Estimado cliente,\n\n");
        sb.append("Gracias por su reserva. A continuación, encontrará los detalles de su reserva:\n\n");
        sb.append("Código de Factura: ").append(factura.getCodigo()).append("\n");
        sb.append("Fecha de Inicio: ").append(factura.getFechaComienzo()).append("\n");
        sb.append("Fecha de Fin: ").append(factura.getFechaTerminacion()).append("\n");
        sb.append("Subtotal: ").append(factura.getSubtotal()).append("\n");
        sb.append("Total: ").append(factura.getTotal()).append("\n\n");
        sb.append("Detalles de la Reserva:\n");
        sb.append("Alojamiento: ").append(reserva.getNombreHospedaje()).append("\n");
        sb.append("Ciudad: ").append(reserva.getCiudadAlojamiento()).append("\n");
        sb.append("Número de Huéspedes: ").append(reserva.getCapacidadMaxima()).append("\n");
        sb.append("Hora de Inicio: ").append(reserva.getHoraInicioReserva()).append("\n");
        sb.append("Hora de Fin: ").append(reserva.getHoraFinReserva()).append("\n\n");
        sb.append("Adjunto encontrará el código QR de su factura.\n\n");
        sb.append("Saludos cordiales,\n");
        sb.append("Equipo de Reservas");

        return sb.toString();
    }
}*/



public class EnvioEmail {

    public void sendEmailWithQRCode(String toEmail, String subject, String body) {
        // SMTP server information
        String host = "smtp.your-email-provider.com"; // Replace with your SMTP server
        String port = "587"; // Replace with your SMTP port
        String username = "your-email@example.com"; // Replace with your email
        String password = "your-email-password"; // Replace with your email password

        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

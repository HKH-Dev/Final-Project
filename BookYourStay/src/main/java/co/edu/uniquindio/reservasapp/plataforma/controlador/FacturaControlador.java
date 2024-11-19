package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.Utils.EnvioEmail;
import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import co.edu.uniquindio.reservasapp.plataforma.modelo.QRCodeGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class FacturaControlador implements Initializable {
    @FXML
    private Label lblNombreHospedaje;
    @FXML
    private Label lblCiudadAlojamiento;
    @FXML
    private Label lblFechaInicio;
    @FXML
    private Label lblFechaFin;
    @FXML
    private Label lblHoraInicio;
    @FXML
    private Label lblHoraFin;
    @FXML
    private Label lblNumeroHuespedes;
    @FXML
    private Label lblPrecioPorNoche;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblTotal;
    @FXML
    private TextField txtMontoCarga;
    @FXML
    private Label lblSaldoActual;
    @FXML
    private Label lblInvoiceSubtotal;
    @FXML
    private Label lblInvoiceTotal;
    @FXML
    private Label lblInvoiceDate;
    @FXML
    private Label lblInvoiceCode;
    @FXML
    private ImageView qrCodeImageView;

    private AppReservasPrincipal appReservasPrincipal;
    private Alojamiento currentAlojamiento;
    private Persona usuarioActual;
    private Reserva reserva; // Added
    private double totalReserva;
    private double subtotalDia;
    private LocalDate diaInicioReserva;
    private LocalDate diaFinReserva;
    private String horaInicioReserva;
    private String horaFinReserva;
    private int numeroHuespedes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appReservasPrincipal = AppReservasPrincipal.getInstance();

        // Check if `reserva` is null
        if (reserva != null) {
            this.totalReserva = reserva.getCosto();
            lblInvoiceSubtotal.setText(String.valueOf(reserva.getCosto() / reserva.getCapacidadMaxima()));
            lblInvoiceTotal.setText(String.valueOf(reserva.getCosto()));
            lblInvoiceDate.setText(reserva.getDiaInicioReserva().toString() + " - " + reserva.getDiaFinReserva().toString());
            lblInvoiceCode.setText(reserva.getIdReserva());
        } else {
            System.out.println("Reserva object is null. Set it using setReserva() before loading this scene.");
        }
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;

        // Update labels if they are already initialized
        if (reserva != null) {
            this.totalReserva = reserva.getCosto();
            if (lblInvoiceSubtotal != null) {
                lblInvoiceSubtotal.setText(String.valueOf(reserva.getCosto() / reserva.getCapacidadMaxima()));
                lblInvoiceTotal.setText(String.valueOf(reserva.getCosto()));
                lblInvoiceDate.setText(reserva.getDiaInicioReserva().toString() + " - " + reserva.getDiaFinReserva().toString());
                lblInvoiceCode.setText(reserva.getIdReserva());
            }
        }
    }

    public void setDatosReserva(Alojamiento alojamiento, Persona usuario, LocalDate diaInicio, LocalDate diaFin, String horaInicio, String horaFin, int numeroHuespedes) {
        this.currentAlojamiento = alojamiento;
        this.usuarioActual = usuario;
        this.diaInicioReserva = diaInicio;
        this.diaFinReserva = diaFin;
        this.horaInicioReserva = horaInicio;
        this.horaFinReserva = horaFin;
        this.numeroHuespedes = numeroHuespedes;

        mostrarDetallesFactura();
    }

    private void mostrarDetallesFactura() {
        lblNombreHospedaje.setText(currentAlojamiento.getNombre());
        lblCiudadAlojamiento.setText(currentAlojamiento.getCiudad().name());
        lblFechaInicio.setText(diaInicioReserva.toString());
        lblFechaFin.setText(diaFinReserva.toString());
        lblHoraInicio.setText(horaInicioReserva);
        lblHoraFin.setText(horaFinReserva);
        lblNumeroHuespedes.setText(String.valueOf(numeroHuespedes));
        lblPrecioPorNoche.setText(String.valueOf(currentAlojamiento.getPrecioNoche()));

        long diasReserva = ChronoUnit.DAYS.between(diaInicioReserva, diaFinReserva);
        if (diasReserva == 0) {
            diasReserva = 1; // At least one day
        }
        double precioPorNoche = currentAlojamiento.getPrecioNoche();
        subtotalDia = precioPorNoche * numeroHuespedes;
        double total = subtotalDia * diasReserva;

        lblSubtotal.setText(String.valueOf(subtotalDia));
        lblTotal.setText(String.valueOf(total));

        totalReserva = total;

        lblSaldoActual.setText(String.valueOf(usuarioActual.getSaldo()));
    }

    public void cargarSaldo(ActionEvent event) {
        try {
            double montoCarga = Double.parseDouble(txtMontoCarga.getText());
            if (montoCarga <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Ingrese un monto válido para cargar.");
                return;
            }
            double nuevoSaldo = usuarioActual.getSaldo() + montoCarga;
            usuarioActual.setSaldo(nuevoSaldo);
            lblSaldoActual.setText(String.valueOf(nuevoSaldo));
            txtMontoCarga.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Ingrese un monto válido para cargar.");
        }
    }

    public void pagarReserva(ActionEvent event) {
        try {
            if (usuarioActual.getSaldo() < totalReserva) {
                showAlert(Alert.AlertType.ERROR, "Error", "No tienes saldo suficiente para realizar el pago. Por favor, carga más saldo.");
                return;
            }

            // Deduct the amount from user's balance
            usuarioActual.setSaldo(usuarioActual.getSaldo() - totalReserva);

            // Generate unique IDs for reservation and invoice
            String idReserva = UUID.randomUUID().toString();
            String invoiceCode = UUID.randomUUID().toString();
            LocalDate invoiceDate = LocalDate.now();

            // Create the reservation
//            Reserva nuevaReserva = appReservasPrincipal.crearReserva(idReserva, currentAlojamiento.getCiudad().name(), currentAlojamiento.getNombre(), usuarioActual.getCedula(), diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, totalReserva, numeroHuespedes);
            // After
            Reserva nuevaReserva = new Reserva(idReserva, currentAlojamiento.getCiudad().name(), currentAlojamiento.getNombre(), usuarioActual.getCedula(), diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, totalReserva, numeroHuespedes);
            appReservasPrincipal.listarTodasReservas().add(nuevaReserva);

            appReservasPrincipal.listarTodasReservas().add(nuevaReserva);

            // Update invoice details
            lblInvoiceSubtotal.setText(String.valueOf(subtotalDia));
            lblInvoiceTotal.setText(String.valueOf(totalReserva));
            lblInvoiceDate.setText(invoiceDate.toString());
            lblInvoiceCode.setText(invoiceCode);

            // Generate QR code image and save to file
            String qrCodeFilePath = "QRCode.png";
            QRCodeGenerator.generateQRCodeImage(invoiceCode, 200, 200, qrCodeFilePath);

            // Load the image from the file
            Image qrImage = new Image(new FileInputStream(qrCodeFilePath));

            // Display QR code in ImageView
            qrCodeImageView.setImage(qrImage);

            // Prepare the QR code file for email attachment
            File qrCodeFile = new File(qrCodeFilePath);

            // Send email to client with QR code and reservation details
            String destinatario = usuarioActual.getEmail();
            String asunto = "Detalles de su reserva y factura";
            String mensaje = "Estimado " + usuarioActual.getNombre() + ",\n\n"
                    + "Gracias por su reserva. Adjuntamos el código QR de su factura y los detalles de su reserva.\n\n"
                    + "Código de Factura: " + invoiceCode + "\n"
                    + "Fecha: " + invoiceDate + "\n"
                    + "Total: " + totalReserva + "\n"
                    + "Subtotal: " + subtotalDia + "\n\n"
                    + "Detalles de la reserva:\n"
                    + "Alojamiento: " + currentAlojamiento.getNombre() + "\n"
                    + "Fecha Inicio: " + diaInicioReserva.toString() + "\n"
                    + "Fecha Fin: " + diaFinReserva.toString() + "\n"
                    + "Número de Huéspedes: " + numeroHuespedes + "\n\n"
                    + "Atentamente,\n"
                    + "El equipo de BookYourStay";

            EnvioEmail.enviarNotificacion(destinatario, asunto, mensaje, qrCodeFile);

            // Delete temporary QR code file
            qrCodeFile.delete();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reserva pagada exitosamente. Se ha enviado un correo electrónico con los detalles de su reserva.");

            // Prompt user to submit a review
            submitReview(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al pagar la reserva: " + e.getMessage());
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void volverAtras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservar.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNombreHospedaje.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al volver atrás: " + e.getMessage());
        }
    }


    public void setDatosReservaFromReserva(Reserva reserva) {
        this.diaInicioReserva = reserva.getDiaInicioReserva();
        this.diaFinReserva = reserva.getDiaFinReserva();
        this.horaInicioReserva = reserva.getHoraInicioReserva();
        this.horaFinReserva = reserva.getHoraFinReserva();
        this.numeroHuespedes = reserva.getCapacidadMaxima();
        this.totalReserva = reserva.getCosto();

        // Get the Persona
        this.usuarioActual = appReservasPrincipal.obtenerPersona(reserva.getCedulaReservante()).orElse(null);

        // Get the Alojamiento
        this.currentAlojamiento = appReservasPrincipal.getListaAlojamientos().stream()
                .filter(a -> a.getNombre().equals(reserva.getNombreHospedaje()))
                .findFirst()
                .orElse(null);

        mostrarDetallesFactura();
    }

    public void submitReview(ActionEvent event) {
        try {
            // Create a dialog to get the rating and comment from the user
            Dialog<Review> dialog = new Dialog<>();
            dialog.setTitle("Submit Review");

            // Set the button types
            ButtonType submitButtonType = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

            // Create the rating and comment fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField ratingField = new TextField();
            ratingField.setPromptText("Rating (1-5)");
            TextArea commentField = new TextArea();
            commentField.setPromptText("Comentario");

            grid.add(new Label("Rating:"), 0, 0);
            grid.add(ratingField, 1, 0);
            grid.add(new Label("Comentario:"), 0, 1);
            grid.add(commentField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to a Review when the submit button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    try {
                        int rating = Integer.parseInt(ratingField.getText());
                        if (rating < 1 || rating > 5) {
                            showAlert(Alert.AlertType.ERROR, "Error", "El rating debe ser entre 1 y 5.");
                            return null;
                        }
                        String comment = commentField.getText();
                        return new Review(usuarioActual.getCedula(), rating, comment);
                    } catch (NumberFormatException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Ingrese un número válido para el rating.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Review> result = dialog.showAndWait();

            result.ifPresent(review -> {
                // Add the review to the currentAlojamiento
                currentAlojamiento.addReview(review);
                showAlert(Alert.AlertType.INFORMATION, "Gracias", "Su reseña ha sido enviada.");
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al enviar la reseña: " + e.getMessage());
        }
    }
}


/*
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;

import co.edu.uniquindio.reservasapp.Utils.EnvioEmail;
import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.Review;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.QRCodeGenerator;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FacturaControlador implements Initializable {
    @FXML private Label lblNombreHospedaje;
    @FXML private Label lblCiudadAlojamiento;
    @FXML private Label lblFechaInicio;
    @FXML private Label lblFechaFin; // Ensure @FXML is present
    @FXML private Label lblHoraInicio;
    @FXML private Label lblHoraFin;
    @FXML private Label lblNumeroHuespedes;
    @FXML private Label lblPrecioPorNoche;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTotal;
    @FXML private TextField txtMontoCarga;
    @FXML private Label lblSaldoActual;

    @FXML private Label lblInvoiceSubtotal;
    @FXML private Label lblInvoiceTotal;
    @FXML private Label lblInvoiceDate;
    @FXML private Label lblInvoiceCode;
    @FXML private ImageView qrCodeImageView;

    private double subtotalDia;

    private Alojamiento currentAlojamiento;
    private Persona usuarioActual;
    private LocalDate diaInicioReserva;
    private LocalDate diaFinReserva;
    private String horaInicioReserva;
    private String horaFinReserva;
    private int numeroHuespedes;
    private double totalReserva;
    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
    Reserva reserva;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.totalReserva = reserva.getCosto();
        lblInvoiceSubtotal.setText(String.valueOf(reserva.getCosto() / reserva.getCapacidadMaxima()));
        lblInvoiceTotal.setText(String.valueOf(reserva.getCosto()));
        lblInvoiceDate.setText(reserva.getDiaInicioReserva().toString() + " - " + reserva.getDiaFinReserva().toString());
        lblInvoiceCode.setText(reserva.getIdReserva());
    }

    public void setDatosReserva(Alojamiento alojamiento, Persona usuario, LocalDate diaInicio, LocalDate diaFin, String horaInicio, String horaFin, int numeroHuespedes) {
        this.currentAlojamiento = alojamiento;
        this.usuarioActual = usuario;
        this.diaInicioReserva = diaInicio;
        this.diaFinReserva = diaFin;
        this.horaInicioReserva = horaInicio;
        this.horaFinReserva = horaFin;
        this.numeroHuespedes = numeroHuespedes;

        mostrarDetallesFactura();
    }
//    /*public void setDatosReserva(Alojamiento alojamiento, Persona usuario, LocalDate diaInicio, LocalDate diaFin, String horaInicio, String horaFin, int numeroHuespedes) {
//        this.currentAlojamiento = alojamiento;
//        this.usuarioActual = usuario;
//        this.diaInicioReserva = diaInicio;
//        this.diaFinReserva = diaFin;
//        this.horaInicioReserva = horaInicio;
//        this.horaFinReserva = horaFin;
//        this.numeroHuespedes = numeroHuespedes;
//        if (lblFechaInicio != null) {
//            lblFechaInicio.setText("Some date");
//        }
//        mostrarDetallesFactura();
//    }
///*
//    private void mostrarDetallesFactura() {
//        lblNombreHospedaje.setText(currentAlojamiento.getNombre());
//        lblCiudadAlojamiento.setText(currentAlojamiento.getCiudad().name());
//        lblFechaInicio.setText(diaInicioReserva.toString());
//        lblFechaFin.setText(diaFinReserva.toString());
//        lblHoraInicio.setText(horaInicioReserva);
//        lblHoraFin.setText(horaFinReserva);
//        lblNumeroHuespedes.setText(String.valueOf(numeroHuespedes));
//        lblPrecioPorNoche.setText(String.valueOf(currentAlojamiento.getPrecioNoche()));
//        this.subtotalDia = subtotalDia; // Store the subtotal for later use
////
//        if (lblFechaInicio == null) {
//            lblFechaInicio.setText(LocalDate.now().toString());
//        }
//        long diasReserva = ChronoUnit.DAYS.between(diaInicioReserva, diaFinReserva);
//        if (diasReserva == 0) {
//            diasReserva = 1; // At least one day
//        }
//        double precioPorNoche = currentAlojamiento.getPrecioNoche();
//        double subtotalDia = precioPorNoche * numeroHuespedes;
//        double total = subtotalDia * diasReserva;
//
//        lblSubtotal.setText(String.valueOf(subtotalDia));
//        lblTotal.setText(String.valueOf(total));
//
//        totalReserva = total;
//
//        lblSaldoActual.setText(String.valueOf(usuarioActual.getSaldo()));
//    }

    private void mostrarDetallesFactura() {
        lblNombreHospedaje.setText(currentAlojamiento.getNombre());
        lblCiudadAlojamiento.setText(currentAlojamiento.getCiudad().name());
        lblFechaInicio.setText(diaInicioReserva.toString());
        lblFechaFin.setText(diaFinReserva.toString());
        lblHoraInicio.setText(horaInicioReserva);
        lblHoraFin.setText(horaFinReserva);
        lblNumeroHuespedes.setText(String.valueOf(numeroHuespedes));
        lblPrecioPorNoche.setText(String.valueOf(currentAlojamiento.getPrecioNoche()));

        long diasReserva = ChronoUnit.DAYS.between(diaInicioReserva, diaFinReserva);
        if (diasReserva == 0) {
            diasReserva = 1; // At least one day
        }
        double precioPorNoche = currentAlojamiento.getPrecioNoche();
        subtotalDia = precioPorNoche * numeroHuespedes;
        double total = subtotalDia * diasReserva;

        lblSubtotal.setText(String.valueOf(subtotalDia));
        lblTotal.setText(String.valueOf(total));

        totalReserva = total;

        lblSaldoActual.setText(String.valueOf(usuarioActual.getSaldo()));
    }

    public void cargarSaldo(ActionEvent event) {
        try {
            double montoCarga = Double.parseDouble(txtMontoCarga.getText());
            if (montoCarga <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Ingrese un monto válido para cargar.");
                return;
            }
            double nuevoSaldo = usuarioActual.getSaldo() + montoCarga;
            usuarioActual.setSaldo(nuevoSaldo);
            lblSaldoActual.setText(String.valueOf(nuevoSaldo));
            txtMontoCarga.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Ingrese un monto válido para cargar.");
        }
    }

//    public void pagarReserva(ActionEvent event) {
//        try {
//            if (usuarioActual.getSaldo() < totalReserva) {
//                showAlert(Alert.AlertType.ERROR, "Error", "No tienes saldo suficiente para realizar el pago. Por favor, carga más saldo.");
//                return;
//            }
//
//            // Deduct the amount from user's balance
//            usuarioActual.setSaldo(usuarioActual.getSaldo() - totalReserva);
//
//            // Generate unique ID for reservation
//            String idReserva = UUID.randomUUID().toString();
//
//            // Create the reservation and generate factura
//            Reserva nuevaReserva = appReservasPrincipal.crearReserva(idReserva, currentAlojamiento.getCiudad().name(), currentAlojamiento.getNombre(), usuarioActual.getCedula(), diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, totalReserva, numeroHuespedes);
//
//            appReservasPrincipal.listarTodasReservas().add(nuevaReserva);
//
//            // Show success message
//            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reserva pagada exitosamente. Se ha enviado un correo electrónico con los detalles de su reserva.");
//
//            // Navigate back to profile or main menu
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) lblNombreHospedaje.getScene().getWindow();
//            stage.setScene(new Scene(root));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Error", "Error al pagar la reserva: " + e.getMessage());
//        }
//    }

//   /* public void pagarReserva(ActionEvent event) {
//        try {
//            if (usuarioActual.getSaldo() < totalReserva) {
//                showAlert(Alert.AlertType.ERROR, "Error", "No tienes saldo suficiente para realizar el pago. Por favor, carga más saldo.");
//                return;
//            }
//
//            // Deduct the amount from user's balance
//            usuarioActual.setSaldo(usuarioActual.getSaldo() - totalReserva);
//
//            // Generate unique IDs for reservation and invoice
//            String idReserva = UUID.randomUUID().toString();
//            String invoiceCode = UUID.randomUUID().toString();
//            LocalDate invoiceDate = LocalDate.now();
//
//            // Create the reservation
//            Reserva nuevaReserva = appReservasPrincipal.crearReserva(idReserva, currentAlojamiento.getCiudad().name(), currentAlojamiento.getNombre(), usuarioActual.getCedula(), diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, totalReserva, numeroHuespedes);
//
//            appReservasPrincipal.listarTodasReservas().add(nuevaReserva);
//            // Display invoice details
//            lblInvoiceSubtotal.setText(String.valueOf(subtotalDia));
//            lblInvoiceTotal.setText(String.valueOf(totalReserva));
//            lblInvoiceDate.setText(invoiceDate.toString());
//            lblInvoiceCode.setText(invoiceCode);
//            BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImage(invoiceCode);
//
//            // Convert BufferedImage to byte array
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(qrCodeImage, "png", baos);
//            baos.flush();
//            byte[] imageInByte = baos.toByteArray();
//            baos.close();
//
//            // Create JavaFX Image from byte array
//            ByteArrayInputStream bais = new ByteArrayInputStream(imageInByte);
//            Image qrImage = new Image(bais);
//
//            // Display QR code in ImageView
//            qrCodeImageView.setImage(qrImage);
//
//            // Save QR code image to a temporary file for email attachment
//            File qrCodeFile = new File("QRCode.png");
//            ImageIO.write(qrCodeImage, "png", qrCodeFile);
//
//
//
//            // Send email to client with QR code and reservation details
//            String destinatario = usuarioActual.getEmail();
//            String asunto = "Detalles de su reserva y factura";
//            String mensaje = "Estimado " + usuarioActual.getNombre() + ",\n\n"
//                    + "Gracias por su reserva. Adjuntamos el código QR de su factura y los detalles de su reserva.\n\n"
//                    + "Código de Factura: " + invoiceCode + "\n"
//                    + "Fecha: " + invoiceDate.toString() + "\n"
//                    + "Total: " + totalReserva + "\n"
//                    + "Subtotal: " + subtotalDia + "\n\n"
//                    + "Detalles de la reserva:\n"
//                    + "Alojamiento: " + currentAlojamiento.getNombre() + "\n"
//                    + "Fecha Inicio: " + diaInicioReserva.toString() + "\n"
//                    + "Fecha Fin: " + diaFinReserva.toString() + "\n"
//                    + "Número de Huéspedes: " + numeroHuespedes + "\n\n"
//                    + "Atentamente,\n"
//                    + "El equipo de BookYourStay";
//
//            EnvioEmail.enviarNotificacion(destinatario, asunto, mensaje, qrCodeFile);
//
//            // Delete temporary QR code file
//            qrCodeFile.delete();
//
//            // Show success message
//            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reserva pagada exitosamente. Se ha enviado un correo electrónico con los detalles de su reserva.");
//
//// Prompt user to submit a review
//            submitReview(event);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Error", "Error al pagar la reserva: " + e.getMessage());
//        }
//
//    }

    public void pagarReserva(ActionEvent event) {
        try {
            if (usuarioActual.getSaldo() < totalReserva) {
                showAlert(Alert.AlertType.ERROR, "Error", "No tienes saldo suficiente para realizar el pago. Por favor, carga más saldo.");
                return;
            }

            // Deduct the amount from user's balance
            usuarioActual.setSaldo(usuarioActual.getSaldo() - totalReserva);

            // Generate unique IDs for reservation and invoice
            String idReserva = UUID.randomUUID().toString();
            String invoiceCode = UUID.randomUUID().toString();
            LocalDate invoiceDate = LocalDate.now();

            // Create the reservation
            Reserva nuevaReserva = appReservasPrincipal.crearReserva(idReserva, currentAlojamiento.getCiudad().name(), currentAlojamiento.getNombre(), usuarioActual.getCedula(), diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, totalReserva, numeroHuespedes);

            appReservasPrincipal.listarTodasReservas().add(nuevaReserva);

            // Update invoice details
            lblInvoiceSubtotal.setText(String.valueOf(subtotalDia));
            lblInvoiceTotal.setText(String.valueOf(totalReserva));
            lblInvoiceDate.setText(invoiceDate.toString());
            lblInvoiceCode.setText(invoiceCode);

            // Generate QR code image and save to file
            String qrCodeFilePath = "QRCode.png";
            QRCodeGenerator.generateQRCodeImage(invoiceCode, 200, 200, qrCodeFilePath);

            // Load the image from the file
            Image qrImage = new Image(new FileInputStream(qrCodeFilePath));

            // Display QR code in ImageView
            qrCodeImageView.setImage(qrImage);

            // Prepare the QR code file for email attachment
            File qrCodeFile = new File(qrCodeFilePath);

            // Send email to client with QR code and reservation details
            String destinatario = usuarioActual.getEmail();
            String asunto = "Detalles de su reserva y factura";
            String mensaje = "Estimado " + usuarioActual.getNombre() + ",\n\n"
                    + "Gracias por su reserva. Adjuntamos el código QR de su factura y los detalles de su reserva.\n\n"
                    + "Código de Factura: " + invoiceCode + "\n"
                    + "Fecha: " + invoiceDate.toString() + "\n"
                    + "Total: " + totalReserva + "\n"
                    + "Subtotal: " + subtotalDia + "\n\n"
                    + "Detalles de la reserva:\n"
                    + "Alojamiento: " + currentAlojamiento.getNombre() + "\n"
                    + "Fecha Inicio: " + diaInicioReserva.toString() + "\n"
                    + "Fecha Fin: " + diaFinReserva.toString() + "\n"
                    + "Número de Huéspedes: " + numeroHuespedes + "\n\n"
                    + "Atentamente,\n"
                    + "El equipo de BookYourStay";

            EnvioEmail.enviarNotificacion(destinatario, asunto, mensaje, qrCodeFile);

            // Delete temporary QR code file
            qrCodeFile.delete();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reserva pagada exitosamente. Se ha enviado un correo electrónico con los detalles de su reserva.");

            // Prompt user to submit a review
            submitReview(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al pagar la reserva: " + e.getMessage());
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void volverAtras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservar.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNombreHospedaje.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al volver atrás: " + e.getMessage());
        }
    }

    public void printFactura(ActionEvent event) {

    }

    public void setDatosReservaFromReserva(Reserva reserva) {
        this.diaInicioReserva = reserva.getDiaInicioReserva();
        this.diaFinReserva = reserva.getDiaFinReserva();
        this.horaInicioReserva = reserva.getHoraInicioReserva();
        this.horaFinReserva = reserva.getHoraFinReserva();
        this.numeroHuespedes = reserva.getCapacidadMaxima();
        this.totalReserva = reserva.getCosto();

        // Get the Persona
        this.usuarioActual = appReservasPrincipal.obtenerPersona(reserva.getCedulaReservante()).orElse(null);

        // Get the Alojamiento
        this.currentAlojamiento = appReservasPrincipal.getListaAlojamientos().stream()
                .filter(a -> a.getNombre().equals(reserva.getNombreHospedaje()))
                .findFirst()
                .orElse(null);

        mostrarDetallesFactura();
    }

    public void submitReview(ActionEvent event) {
        try {
            // Create a dialog to get the rating and comment from the user
            Dialog<Review> dialog = new Dialog<>();
            dialog.setTitle("Submit Review");

            // Set the button types
            ButtonType submitButtonType = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

            // Create the rating and comment fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField ratingField = new TextField();
            ratingField.setPromptText("Rating (1-5)");
            TextArea commentField = new TextArea();
            commentField.setPromptText("Comentario");

            grid.add(new Label("Rating:"), 0, 0);
            grid.add(ratingField, 1, 0);
            grid.add(new Label("Comentario:"), 0, 1);
            grid.add(commentField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to a Review when the submit button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    try {
                        int rating = Integer.parseInt(ratingField.getText());
                        if (rating < 1 || rating > 5) {
                            showAlert(Alert.AlertType.ERROR, "Error", "El rating debe ser entre 1 y 5.");
                            return null;
                        }
                        String comment = commentField.getText();
                        return new Review(usuarioActual.getCedula(), rating, comment);
                    } catch (NumberFormatException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Ingrese un número válido para el rating.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Review> result = dialog.showAndWait();

            result.ifPresent(review -> {
                // Add the review to the currentAlojamiento
                currentAlojamiento.addReview(review);
                showAlert(Alert.AlertType.INFORMATION, "Gracias", "Su reseña ha sido enviada.");
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al enviar la reseña: " + e.getMessage());
        }
    }
}*/


package co.edu.uniquindio.reservasapp.plataforma.controlador;

import java.time.LocalDate;
import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Horario;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;
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

    // Rest of your code...


    private Alojamiento currentAlojamiento;
    private Persona usuarioActual;
    private LocalDate diaInicioReserva;
    private LocalDate diaFinReserva;
    private String horaInicioReserva;
    private String horaFinReserva;
    private int numeroHuespedes;
    private double totalReserva;

    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize if needed
    }

    public void setDatosReserva(Alojamiento alojamiento, Persona usuario, LocalDate diaInicio, LocalDate diaFin, String horaInicio, String horaFin, int numeroHuespedes) {
        this.currentAlojamiento = alojamiento;
        this.usuarioActual = usuario;
        this.diaInicioReserva = diaInicio;
        this.diaFinReserva = diaFin;
        this.horaInicioReserva = horaInicio;
        this.horaFinReserva = horaFin;
        this.numeroHuespedes = numeroHuespedes;
        if (lblFechaInicio != null) {
            lblFechaInicio.setText("Some date");
        }

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
        if (lblFechaInicio != null) {
            lblFechaInicio.setText("Some date");
        }
        long diasReserva = ChronoUnit.DAYS.between(diaInicioReserva, diaFinReserva);
        if (diasReserva == 0) {
            diasReserva = 1; // At least one day
        }
        double precioPorNoche = currentAlojamiento.getPrecioNoche();
        double subtotalDia = precioPorNoche * numeroHuespedes;
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

            // Generate unique ID for reservation
            String idReserva = UUID.randomUUID().toString();

            // Create the reservation and generate factura
            Reserva nuevaReserva = appReservasPrincipal.crearReserva(idReserva, currentAlojamiento.getCiudad().name(), currentAlojamiento.getNombre(), usuarioActual.getCedula(), diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, totalReserva, numeroHuespedes);

            appReservasPrincipal.listarTodasReservas().add(nuevaReserva);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reserva pagada exitosamente. Se ha enviado un correo electrónico con los detalles de su reserva.");

            // Navigate back to profile or main menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNombreHospedaje.getScene().getWindow();
            stage.setScene(new Scene(root));

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
}


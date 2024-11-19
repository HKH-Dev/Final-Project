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

public class RegistrarReservaControlador implements Initializable {
    @FXML private Label lblCedulaReservante;
    @FXML private Label lblCosto;
    @FXML private Label lblNombre;
    @FXML private Label lblCiudadAlojamiento;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblNumeroHuespedes;
    @FXML private ImageView imgPrincipal;
    @FXML private ComboBox<String> cbHoraInicioReserva;
    @FXML private ComboBox<String> cbHoraFinReserva;
    @FXML private TextArea txtTarifaReserva;
    @FXML private Label lblAverageRating;
    private List<String> imagenes;
    private int currentImageIndex = 0;
    private Alojamiento currentAlojamiento;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numeroHuespedes;
    private Persona usuarioActual;

    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    Horario tablaHorarios = new Horario();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize time pickers
        ObservableList<String> horariosSeleccionar = tablaHorarios.getHorarios();
        cbHoraInicioReserva.setItems(horariosSeleccionar);
        cbHoraFinReserva.setItems(horariosSeleccionar);

        // Add listeners to update the tariff when values change
        dpFechaInicio.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        dpFechaFin.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        cbHoraInicioReserva.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        cbHoraFinReserva.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
    }

public void setAlojamiento(Alojamiento alojamiento, LocalDate startDate, LocalDate endDate, int numeroHuespedes, Persona usuarioActual) {
    if (alojamiento != null) {
        lblAverageRating.setText(String.valueOf(alojamiento.getAverageRating()));
    }
    this.currentAlojamiento = alojamiento;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numeroHuespedes = numeroHuespedes;
        this.usuarioActual = usuarioActual;

        // Update UI with accommodation details
        lblNombre.setText(alojamiento.getNombre());
        lblCiudadAlojamiento.setText(alojamiento.getCiudad().name());
        dpFechaInicio.setValue(startDate);
        dpFechaFin.setValue(endDate);
        lblNumeroHuespedes.setText(String.valueOf(numeroHuespedes));
        lblCedulaReservante.setText(usuarioActual.getCedula());
        lblCosto.setText(String.valueOf(alojamiento.getPrecioNoche()));

        double averageRating = currentAlojamiento.getAverageRating();
        lblAverageRating.setText(String.format("%.1f", averageRating));

        // Load images
        imagenes = alojamiento.getImagenes();
        if (imagenes != null && !imagenes.isEmpty()) {
            loadImage(imagenes.get(0));
        }

        // Update tariff
        updateTarifaReserva();
    }

    private void updateTarifaReserva() {
        txtTarifaReserva.setText(calcularTarifaReserva());
    }

    public String calcularTarifaReserva() {
        try {
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();
            if (fechaInicio == null || fechaFin == null) {
                return "Seleccione las fechas de reserva.";
            }
            if (fechaFin.isBefore(fechaInicio)) {
                return "La fecha fin no puede ser antes de la fecha inicio.";
            }
            long diasReserva = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
            if (diasReserva == 0) {
                diasReserva = 1; // Minimum one day
            }
            double precioPorNoche = currentAlojamiento.getPrecioNoche();
            double subtotalDia = precioPorNoche * numeroHuespedes;
            double total = subtotalDia * diasReserva;

            return "Subtotal por Día: " + subtotalDia + "\n" +
                    "Total: " + total;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al calcular la tarifa.";
        }
    }

    public void continuarReserva(ActionEvent actionEvent) {
        try {
            String ciudadAlojamiento = lblCiudadAlojamiento.getText();
            String nombreHospedaje = lblNombre.getText();
            String cedulaReservante = lblCedulaReservante.getText();
            LocalDate diaInicioReserva = dpFechaInicio.getValue();
            LocalDate diaFinReserva = dpFechaFin.getValue();
            String horaInicioReserva = cbHoraInicioReserva.getValue();
            String horaFinReserva = cbHoraFinReserva.getValue();

            // Validations
            if (diaInicioReserva == null || diaFinReserva == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Seleccione las fechas de reserva.");
                return;
            }

            if (diaFinReserva.isBefore(diaInicioReserva)) {
                showAlert(Alert.AlertType.ERROR, "Error", "La fecha fin no puede ser antes de la fecha inicio.");
                return;
            }

            if (!appReservasPrincipal.isAccommodationAvailable(currentAlojamiento, diaInicioReserva, diaFinReserva)) {
                showAlert(Alert.AlertType.ERROR, "Error", "El alojamiento no está disponible en las fechas seleccionadas.");
                return;
            }

            if (numeroHuespedes > currentAlojamiento.getCapacidadMaxima()) {
                showAlert(Alert.AlertType.ERROR, "Error", "El alojamiento no tiene capacidad suficiente para el número de huéspedes.");
                return;
            }

            // Navigate to Factura scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/factura.fxml"));
            Parent root = loader.load();

            FacturaControlador facturaControlador = loader.getController();
            facturaControlador.setDatosReserva(currentAlojamiento, usuarioActual, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, numeroHuespedes);

            Stage stage = (Stage) lblCedulaReservante.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al continuar con la reserva: " + e.getMessage());
        }
    }

    private void loadImage(String imagePath) {
        try {
            String adjustedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            InputStream imageStream = getClass().getResourceAsStream(adjustedPath);
            if (imageStream != null) {
                imgPrincipal.setImage(new Image(imageStream));
            } else {
                System.out.println("Image not found: " + adjustedPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void showPreviousImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenes.size()) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    @FXML private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
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


/*
public class RegistrarReservaControlador implements Initializable {
    @FXML private Label lblCedulaReservante;
    @FXML private Label lblCosto;
    @FXML private Label lblNombre;
    @FXML private Label lblCiudadAlojamiento;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblNumeroHuespedes;
    @FXML private ImageView imgPrincipal;
    @FXML private ComboBox<String> cbHoraInicioReserva;
    @FXML private ComboBox<String> cbHoraFinReserva;
    @FXML private TextArea txtTarifaReserva;
    @FXML private DatePicker dpAvailabilityCalendar; // Added for availability calendar

    private List<String> imagenes;
    private int currentImageIndex = 0;
    private Alojamiento currentAlojamiento;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numeroHuespedes;
    private Persona usuarioActual;

    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    Horario tablaHorarios = new Horario();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize time pickers
        ObservableList<String> horariosSeleccionar = tablaHorarios.getHorarios();
        cbHoraInicioReserva.setItems(horariosSeleccionar);
        cbHoraFinReserva.setItems(horariosSeleccionar);

        // Add listeners to update the tariff when values change
        dpFechaInicio.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        dpFechaFin.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        cbHoraInicioReserva.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        cbHoraFinReserva.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
    }

    public void setAlojamiento(Alojamiento alojamiento, LocalDate startDate, LocalDate endDate, int numeroHuespedes, Persona usuarioActual) {
        this.currentAlojamiento = alojamiento;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numeroHuespedes = numeroHuespedes;
        this.usuarioActual = usuarioActual;

        // Update UI with accommodation details
        lblNombre.setText(alojamiento.getNombre());
        lblCiudadAlojamiento.setText(alojamiento.getCiudad().name());
        dpFechaInicio.setValue(startDate);
        dpFechaFin.setValue(endDate);
        lblNumeroHuespedes.setText(String.valueOf(numeroHuespedes));
        lblCedulaReservante.setText(usuarioActual.getCedula());
        lblCosto.setText(String.valueOf(alojamiento.getPrecioNoche()));

        // Load images
        imagenes = alojamiento.getImagenes();
        if (imagenes != null && !imagenes.isEmpty()) {
            loadImage(imagenes.get(0));
        }

        // Setup availability calendar
//        setupAvailabilityCalendar();

        // Update tariff
        updateTarifaReserva();
    }

//    private void setupAvailabilityCalendar() {
//        List<Reserva> reservas = appReservasPrincipal.getReservasForAlojamiento(currentAlojamiento);
//
//        // Collect all the dates that are reserved
//        Set<LocalDate> reservedDates = new HashSet<>();
//        for (Reserva reserva : reservas) {
//            LocalDate start = reserva.getDiaInicioReserva();
//            LocalDate end = reserva.getDiaFinReserva();
//            while (!start.isAfter(end)) {
//                reservedDates.add(start);
//                start = start.plusDays(1);
//            }
//        }
//        dpAvailabilityCalendar.setDayCellFactory(datePicker -> new DateCell() {
//            @Override
//            public void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item.isBefore(LocalDate.now())) {
//                    setDisable(true);
//                } else if (reservedDates.contains(item)) {
//                    setDisable(true);
//                    setStyle("-fx-background-color: #ffc0cb;"); // Pink color for reserved dates
//                }
//            }
//        });
//
//        // Disable editing on the availability calendar
//        dpAvailabilityCalendar.setDisable(true);
//    }

    private void updateTarifaReserva() {
        txtTarifaReserva.setText(calcularTarifaReserva());
    }

    public String calcularTarifaReserva(){
        try {
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();
            if (fechaInicio == null || fechaFin == null) {
                return "Seleccione las fechas de reserva.";
            }
            if (fechaFin.isBefore(fechaInicio)) {
                return "La fecha fin no puede ser antes de la fecha inicio.";
            }
            long diasReserva = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
            if (diasReserva == 0) {
                diasReserva = 1; // Minimum one day
            }
            double precioPorNoche = currentAlojamiento.getPrecioNoche();
            double subtotalDia = precioPorNoche * numeroHuespedes;
            double total = subtotalDia * diasReserva;

            String numeroFactura = UUID.randomUUID().toString();
            String fechaIncioReserva = fechaInicio.toString();
            String fechaFinReserva = fechaFin.toString();

            String facturaFinal = "Número de Factura: \n, Fecha de Inicio de Reserva:\n , Fecha de Fin de Reserva: \n, Subtotal por Día: \n, Total: \n", numeroFactura, fechaIncioReserva, fechaFinReserva, subtotalDia, total;

            return facturaFinal;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al calcular la tarifa.";
        }
    }


    public void crearReserva(ActionEvent actionEvent) {
        try {
            String idReserva = UUID.randomUUID().toString();
            String ciudadAlojamiento = lblCiudadAlojamiento.getText();
            String nombreHospedaje = lblNombre.getText();
            String cedulaReservante = lblCedulaReservante.getText();
            LocalDate diaInicioReserva = dpFechaInicio.getValue();
            LocalDate diaFinReserva = dpFechaFin.getValue();
            String horaInicioReserva = cbHoraInicioReserva.getValue();
            String horaFinReserva = cbHoraFinReserva.getValue();

            // Validations
            if (diaInicioReserva == null || diaFinReserva == null) {showAlert(Alert.AlertType.ERROR, "Error", "Seleccione las fechas de reserva.");return;}

            if (diaFinReserva.isBefore(diaInicioReserva)) {showAlert(Alert.AlertType.ERROR, "Error", "La fecha fin no puede ser antes de la fecha inicio.");return;}

            if (!appReservasPrincipal.isAccommodationAvailable(currentAlojamiento, diaInicioReserva, diaFinReserva)) {showAlert(Alert.AlertType.ERROR, "Error", "El alojamiento no está disponible en las fechas seleccionadas.");return;}

            if (numeroHuespedes > currentAlojamiento.getCapacidadMaxima()) {showAlert(Alert.AlertType.ERROR, "Error", "El alojamiento no tiene capacidad suficiente para el número de huéspedes.");return;}

            // Calculate the total cost
            long diasReserva = ChronoUnit.DAYS.between(diaInicioReserva, diaFinReserva);
            if (diasReserva == 0) {diasReserva = 1; // At least one day
            }

            double precioPorNoche = currentAlojamiento.getPrecioNoche();
            double subtotalDia = precioPorNoche * numeroHuespedes;
            double total = subtotalDia * diasReserva;

            // Check if the user has enough balance
            if (usuarioActual.getSaldo() < total) {showAlert(Alert.AlertType.ERROR, "Error", "No tienes saldo suficiente para realizar la reserva.");return;}

            // Deduct the amount from user's balance
            usuarioActual.setSaldo(usuarioActual.getSaldo() - total);

            // Generate factura
//            appReservasPrincipal.crearReserva(idReserva, ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, total, numeroHuespedes);

            // Create the reservation and generate factura

            Reserva nuevaReserva = appReservasPrincipal.crearReserva(idReserva, ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, total, numeroHuespedes);
            appReservasPrincipal.listarTodasReservas().add(nuevaReserva);
            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reserva creada exitosamente. Se ha enviado un correo electrónico con los detalles de su reserva.");

            controladorPrincipal.cerrarVentana(lblCedulaReservante); ///*** I want when I choose reserve, the window closes and goes to the profile window
            controladorPrincipal.navegarVentana("/profile.fxml", "Perfil");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al crear la reserva: " + e.getMessage());
        }


    }

    private void loadImage(String imagePath) {
        try {
            String adjustedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            InputStream imageStream = getClass().getResourceAsStream(adjustedPath);
            if (imageStream != null) {
                imgPrincipal.setImage(new Image(imageStream));
            } else {
                System.out.println("Image not found: " + adjustedPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void showPreviousImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenes.size()) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    @FXML private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
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
}*/


/*
public class RegistrarReservaControlador implements Initializable {
    @FXML private Label lblCedulaReservante;
    @FXML private Label lblCosto;
    @FXML private Label lblNombre;
    @FXML private Label lblCiudadAlojamiento;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblNumeroHuespedes;
    @FXML private ImageView imgPrincipal;
    @FXML private ComboBox<String> cbHoraInicioReserva;
    @FXML private ComboBox<String> cbHoraFinReserva;
    @FXML private TextArea txtTarifaReserva;
    @FXML private DatePicker dpAvailabilityCalendar; // Added for availability calendar

    private List<String> imagenes;
    private int currentImageIndex = 0;
    private Alojamiento currentAlojamiento;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numeroHuespedes;
    private Persona usuarioActual;

    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
    Horario horario = new Horario();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize time pickers
        ObservableList<String> horarios = horario.getHorarios();
        cbHoraInicioReserva.setItems(horarios);
        cbHoraFinReserva.setItems(horarios);

        // Add listeners to update the tariff when values change
        dpFechaInicio.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        dpFechaFin.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        cbHoraInicioReserva.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
        cbHoraFinReserva.valueProperty().addListener((obs, oldVal, newVal) -> updateTarifaReserva());
    }

    public void setAlojamiento(Alojamiento alojamiento, LocalDate startDate, LocalDate endDate, int numeroHuespedes, Persona usuarioActual) {
        this.currentAlojamiento = alojamiento;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numeroHuespedes = numeroHuespedes;
        this.usuarioActual = usuarioActual;

        // Update UI with accommodation details
        lblNombre.setText(alojamiento.getNombre());
        lblCiudadAlojamiento.setText(alojamiento.getCiudad().name());
        dpFechaInicio.setValue(startDate);
        dpFechaFin.setValue(endDate);
        lblNumeroHuespedes.setText(String.valueOf(numeroHuespedes));
        lblCedulaReservante.setText(usuarioActual.getCedula());
        lblCosto.setText(String.valueOf(alojamiento.getPrecioNoche()));

        // Load images
        imagenes = alojamiento.getImagenes();
        if (imagenes != null && !imagenes.isEmpty()) {
            loadImage(imagenes.get(0));
        }

        // Setup availability calendar
        setupAvailabilityCalendar();

        // Update tariff
        updateTarifaReserva();
    }

    private void setupAvailabilityCalendar() {
        List<Reserva> reservas = appReservasPrincipal.getReservasForAlojamiento(currentAlojamiento);

        // Collect all the dates that are reserved
        Set<LocalDate> reservedDates = new HashSet<>();
        for (Reserva reserva : reservas) {
            LocalDate start = reserva.getDiaInicioReserva();
            LocalDate end = reserva.getDiaFinReserva();
            while (!start.isAfter(end)) {
                reservedDates.add(start);
                start = start.plusDays(1);
            }
        }

        dpAvailabilityCalendar.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item.isBefore(LocalDate.now())) {
                    setDisable(true);
                } else if (reservedDates.contains(item)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Pink color for reserved dates
                }
            }
        });
    }

    private void updateTarifaReserva() {
        txtTarifaReserva.setText(calcularTarifaReserva());
    }

    public String calcularTarifaReserva(){
        try {
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();
            if (fechaInicio == null || fechaFin == null) {
                return "Seleccione las fechas de reserva.";
            }
            if (fechaFin.isBefore(fechaInicio)) {
                return "La fecha fin no puede ser antes de la fecha inicio.";
            }
            long diasReserva = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
            if (diasReserva == 0) {
                diasReserva = 1; // Minimum one day
            }
            double precioPorNoche = currentAlojamiento.getPrecioNoche();
            double subtotalDia = precioPorNoche * numeroHuespedes;
            double total = subtotalDia * diasReserva;

            String numeroFactura = String.valueOf(UUID.randomUUID());
            String fechaIncioReserva = fechaInicio.toString();
            String fechaFinReserva = fechaFin.toString();

            return "Número de Factura: " + numeroFactura + "\n" +
                    "Fecha de Inicio de Reserva: " + fechaIncioReserva + "\n" +
                    "Fecha de Fin de Reserva: " + fechaFinReserva + "\n" +
                    "Subtotal por Día: " + subtotalDia + "\n" +
                    "Total: " + total;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al calcular la tarifa.";
        }
    }

    public void crearReserva(ActionEvent actionEvent) {
        try {
            String ciudadAlojamiento = lblCiudadAlojamiento.getText();
            String nombreHospedaje = lblNombre.getText();
            String cedulaReservante = lblCedulaReservante.getText();
            LocalDate diaInicioReserva = dpFechaInicio.getValue();
            LocalDate diaFinReserva = dpFechaFin.getValue();
            String horaInicioReserva = cbHoraInicioReserva.getValue();
            String horaFinReserva = cbHoraFinReserva.getValue();

            if (diaInicioReserva == null || diaFinReserva == null) {
                System.out.println("Seleccione las fechas de reserva.");
                return;
            }

            if (diaFinReserva.isBefore(diaInicioReserva)) {
                System.out.println("La fecha fin no puede ser antes de la fecha inicio.");
                return;
            }

            if (!appReservasPrincipal.isAccommodationAvailable(currentAlojamiento, diaInicioReserva, diaFinReserva)) {
                System.out.println("El alojamiento no está disponible en las fechas seleccionadas.");
                return;
            }

            if (numeroHuespedes > currentAlojamiento.getCapacidadMaxima()) {
                System.out.println("El alojamiento no tiene capacidad suficiente para el número de huéspedes.");
                return;
            }

            // Calculate the total cost
            long diasReserva = ChronoUnit.DAYS.between(diaInicioReserva, diaFinReserva);
            if (diasReserva == 0) {
                diasReserva = 1; // At least one day
            }

            double precioPorNoche = currentAlojamiento.getPrecioNoche();
            double subtotalDia = precioPorNoche * numeroHuespedes;
            double total = subtotalDia * diasReserva;

            // Check if the user has enough balance (assuming 'getSaldo()' is implemented)
            if (usuarioActual.getSaldo() < total) {
                System.out.println("No tienes saldo suficiente para realizar la reserva.");
                return;
            }

            // Deduct the amount from user's balance
            usuarioActual.setSaldo(usuarioActual.getSaldo() - total);

            // Create the reservation
            Reserva nuevaReserva = appReservasPrincipal.crearReserva(ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, total, numeroHuespedes);

            // Generate factura
            appReservasPrincipal.generarFactura(nuevaReserva);

            // Show success message
            System.out.println("Reserva creada exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al crear la reserva: " + e.getMessage());
        }
    }

    private void loadImage(String imagePath) {
        try {
            String adjustedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            InputStream imageStream = getClass().getResourceAsStream(adjustedPath);
            if (imageStream != null) {
                imgPrincipal.setImage(new Image(imageStream));
            } else {
                System.out.println("Image not found: " + adjustedPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void showPreviousImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenes.size()) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    @FXML private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }
}
*/

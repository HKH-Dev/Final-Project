package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import javafx.collections.FXCollections;
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
import java.util.List;
import java.util.ResourceBundle;

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

    private List<String> imagenes;
    private int currentImageIndex = 0;

    private Alojamiento currentAlojamiento;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numeroHuespedes;
    private Persona usuarioActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize time pickers
        ObservableList<String> horas = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            horas.add(String.format("%02d:00", i));
        }
        cbHoraInicioReserva.setItems(horas);
        cbHoraFinReserva.setItems(horas);
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

        // Load images
        imagenes = alojamiento.getImagenes();
        if (imagenes != null && !imagenes.isEmpty()) {
            loadImage(imagenes.get(0));
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

    public void crearReserva(ActionEvent actionEvent) {
        try {
            // Validate time selections
            String horaInicioReserva = cbHoraInicioReserva.getValue();
            String horaFinReserva = cbHoraFinReserva.getValue();
            if (horaInicioReserva == null || horaFinReserva == null) {
                mostrarAlerta("Por favor, seleccione las horas de inicio y fin de la reserva.", Alert.AlertType.ERROR);
                return;
            }

            // Get the current user's ID
            String cedulaReservante = usuarioActual.getCedula();

            // Calculate the total cost
            double costo = currentAlojamiento.getPrecioNoche() * numeroHuespedes;

            // Create the reservation
            AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
            appReservasPrincipal.crearReserva(
                    currentAlojamiento.getCiudad().name(),
                    currentAlojamiento.getNombre(),
                    cedulaReservante,
                    dpFechaInicio.getValue(),
                    dpFechaFin.getValue(),
                    horaInicioReserva,
                    horaFinReserva,
                    costo,
                    numeroHuespedes
            );

            // Show success message
            mostrarAlerta("Reserva creada correctamente", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


/*public class RegistrarReservaControlador implements Initializable {
    private double costo;
    @FXML private Label lblCedulaReservante;
    @FXML private Label lblCosto;
    @FXML private TableView<?> tablaAlojamientos;


    //Accomodation
    @FXML private Label lblNombre;
    @FXML private Label lblCiudadAlojamiento;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblNumeroHuespedes;
    @FXML private ImageView imgPrincipal;

    private List<String> imagenes;
    private int currentImageIndex = 0;

    private Alojamiento currentAlojamiento;
    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();    BuscadorAlojamientosControlador busquedaActual = BuscadorAlojamientosControlador.getInstance();
    AccommodationItemController alojamientoElegido = AccommodationItemController.getInstance();
    LoginControlador usuarioActual = LoginControlador.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Any initialization logic can go here
    }


    public void setAlojamiento(Alojamiento alojamiento) {
        this.currentAlojamiento = alojamiento;

        // Update UI with accommodation details
        lblNombre.setText(alojamientoElegido.getCurrentAlojamiento().getNombre());
        lblCiudadAlojamiento.setText(alojamientoElegido.getCurrentAlojamiento().getCiudad().name());
        dpFechaInicio.setValue(busquedaActual.getStartDate());
        dpFechaFin.setValue(busquedaActual.getEndDate());
        lblNumeroHuespedes.setText(String.valueOf(busquedaActual.getTxtNumeroHuespedes()));
        lblCedulaReservante.setText(usuarioActual.persona.getCedula());

        // Load images
        imagenes = alojamiento.getImagenes();
        // Load primary image
        if (alojamiento.getImagenes() != null && !alojamiento.getImagenes().isEmpty()) {
            loadImage(alojamiento.getImagenes().get(0));
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

   ]
    @FXML private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    @FXML private void handleItemClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservacion.fxml"));
            Parent root = loader.load();

            // Pass the selected accommodation to the next controller
            RegistrarReservaControlador controller = loader.getController();
            controller.setAlojamiento(currentAlojamiento);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservar Alojamiento");
            stage.show();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void crearReserva(ActionEvent actionEvent){
        try {
            // Get the selected accommodation
            Alojamiento alojamiento = alojamientoElegido.getCurrentAlojamiento();
            // Get the selected dates
            String fechaInicio = dpFechaInicio.getValue().toString();
            String fechaFin = dpFechaFin.getValue().toString();
            // Get the number of guests
            int numeroHuespedes = Integer.parseInt(lblNumeroHuespedes.getText());
            // Get the current user's ID
            String cedulaReservante = usuarioActual.persona.getCedula();
            // Calculate the total cost
            double costo = alojamiento.getPrecio() * numeroHuespedes; // error

            // Create the reservation
            appReservasPrincipal.crearReserva(alojamiento,lblNombre, cedulaReservante, fechaInicio, fechaFin, horaentrada, horasalida costo, numeroHuespedes); // both hours should be choose with a time picker or combobox
            // Show success message
            mostrarAlerta("Reserva creada correctamente", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);

        }
    }
}*/

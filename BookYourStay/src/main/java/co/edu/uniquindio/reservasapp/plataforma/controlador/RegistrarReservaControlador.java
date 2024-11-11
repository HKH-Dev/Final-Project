package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Horario;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RegistrarReservaControlador implements Initializable {
    @FXML
    private TextField txtidInstalacion;
    @FXML
    private TextField txtCedula;
    @FXML
    private ComboBox<String> cbTipoAlojamiento;
    @FXML
    private DatePicker dpDiaReserva;
    @FXML
    private ComboBox<String> cbhoraReserva;
    @FXML
    private TextField txtCosto;
//    @FXML
//    private TableView<Instalaciones> tablaInstalaciones;
//    @FXML
//    private TableColumn<Reserva, String> colNombreInstalacion;
//    @FXML
//    private TableColumn<Reserva, String> colcodigoIdInstalacion;
    @FXML
    private TableView<Reserva> tablaReservas;
    @FXML
    private TableColumn<Reserva, String> colidInstalacion;
    @FXML
    private TableColumn<Reserva, String> colNombreReservaInstalacion;
    @FXML
    private TableColumn<Reserva, String> colCedula;
    @FXML
    private TableColumn<Reserva, String> diaReserva;
    @FXML
    private TableColumn<Reserva, String> horaReserva;

    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    Sesion sesion = Sesion.getInstancia();
    Persona persona = sesion.getPersona();
    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

    private ObservableList<Horario> observableListHoras;
    private ObservableList<Alojamiento> observableListaAlojamientos;
    private ObservableList<Reserva> observableListReservas;


    public void crearReserva(ActionEvent actionEvent) {
        try {
            String tipoAlojamiento = cbTipoAlojamiento.getValue();
            String idInstalacion = txtidInstalacion.getText();
            String cedulaCliente = txtCedula.getText();
            LocalDate diaReserva = dpDiaReserva.getValue();
            String horaReserva = cbhoraReserva.getValue();

            if (tipoAlojamiento == null || idInstalacion.isEmpty() || cedulaCliente.isEmpty() || diaReserva == null || horaReserva == null) {
                mostrarAlerta("Todos los campos deben estar completos para crear la reserva.", Alert.AlertType.WARNING);
                return;
            }

            double costo = appReservasPrincipal.calcularCosto(tipoAlojamiento, cedulaCliente);
            txtCosto.setText(String.format("%.2f", costo)); // Automatically set cost in UI

            Reserva reserva = appReservasPrincipal.crearReserva(tipoAlojamiento, idInstalacion, cedulaCliente, diaReserva, horaReserva);
            observableListReservas.add(reserva);
            limpiarFormularioReserva();
            actualizarTabla();
            mostrarAlerta("Reserva creada exitosamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void limpiarFormularioReserva() {
        txtidInstalacion.setText("");
        txtCedula.setText("");
        dpDiaReserva.setValue(null);
        cbhoraReserva.setValue(null);
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void cargarIntalaciones(ActionEvent actionEvent) {
        appReservasPrincipal.getListaAlojamientos().stream()
                .filter(instalacion -> instalacion.getNombre().equals(cbTipoAlojamiento.getValue()))
//                .filter(reserva -> reserva.getCedula().equals(txtCedula.getText()))
                .forEach(System.out::println);
    }

    public void cargarReservas(ActionEvent actionEvent) {
        appReservasPrincipal.getListaReservas().stream()
                .filter(reserva -> reserva.getCedulaReservante().equals(txtCedula.getText()))
                .forEach(System.out::println);
    }

    private void actualizarTabla() {
        observableListReservas.setAll(appReservasPrincipal.listarTodasReservas());
        observableListaAlojamientos.setAll(appReservasPrincipal.getListaAlojamientos());
//        observableListInstalaciones.setAll(reservasUQ.getListaInstalaciones());
        tablaReservas.setItems(observableListReservas);
//        tablaInstalaciones.setItems(observableListaAlojamientos);
    }

    public void cargarHorasDisponibles() {
        String selectedInstalacion = cbTipoAlojamiento.getValue();

        if (selectedInstalacion != null) {
            Alojamiento alojamiento = appReservasPrincipal.getListaAlojamientos().stream()
                    .filter(inst -> inst.getNombre().equals(selectedInstalacion))
                    .findFirst()
                    .orElse(null);

            if (alojamiento != null) {
                LocalDateTime horaInicio = alojamiento.getHoraInicio();
                LocalDateTime horaFin = alojamiento.getHoraFin();

                ObservableList<String> horasDisponibles = FXCollections.observableArrayList();
                LocalTime hora = horaInicio.toLocalTime();

                while (!hora.isAfter(horaFin.toLocalTime())) {
                    horasDisponibles.add(hora.toString());
                    hora = hora.plusHours(1); // Adjust as needed for different intervals
                }

                cbhoraReserva.setItems(horasDisponibles);
            }
        }
    }


    public void actualizarCosto() {
        try {
            String tipoInstalacion = cbTipoAlojamiento.getValue();
            String cedula = txtCedula.getText();

            if (tipoInstalacion != null && cedula != null && !cedula.isEmpty()) {
                double costo = appReservasPrincipal.calcularCosto(tipoInstalacion, cedula);
                txtCosto.setText(String.format("%.2f", costo));
            }
        } catch (Exception e) {
            mostrarAlerta("Error al calcular el costo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void cargarCostoReserva() {
        String tipoInstalacion = cbTipoAlojamiento.getValue();
        String cedula = txtCedula.getText();

        if (tipoInstalacion != null && !cedula.isEmpty()) {
            try {
                Persona persona = appReservasPrincipal.obtenerPersona(cedula).orElseThrow(() -> new Exception("Persona no encontrada"));
                double costo = appReservasPrincipal.calcularCosto(tipoInstalacion, cedula);
                txtCosto.setText(String.valueOf(costo));
            } catch (Exception e) {
                mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void calcularYMostrarCosto() {
        try {
            String tipoInstalacion = cbTipoAlojamiento.getValue();
            String cedula = txtCedula.getText();

            // Ensure both fields are not empty before calculating the cost
            if (tipoInstalacion != null && !tipoInstalacion.isEmpty() && cedula != null && !cedula.isEmpty()) {
                double costo = appReservasPrincipal.calcularCosto(tipoInstalacion, cedula);
                txtCosto.setText(String.format("%.2f", costo)); // Format to two decimal places
            } else {
                txtCosto.setText(""); // Clear cost if required fields are missing
            }
        } catch (Exception e) {
            mostrarAlerta("Error al calcular el costo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        colNombreInstalacion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colcodigoIdInstalacion.setCellValueFactory(new PropertyValueFactory<>("codigoId"));
        colidInstalacion.setCellValueFactory(new PropertyValueFactory<>("idInstalacion"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        diaReserva.setCellValueFactory(new PropertyValueFactory<>("diaReserva"));
        horaReserva.setCellValueFactory(new PropertyValueFactory<>("horaReserva"));*/

        colidInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreHospedaje().toString()));
//        colNombreReservaInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreHospedaje(observableListaAlojamientos)));
        colNombreReservaInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreHospedaje()));


        //  colNombreReservaInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreInstalacion()));
        colCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedulaReservante()));
        diaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDiaInicioReserva().toString()));
        horaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoraReserva()));

        cbhoraReserva.setItems(FXCollections.observableArrayList(new Horario().generarHorarios()));

        cbTipoAlojamiento.setItems(FXCollections.observableArrayList(
                appReservasPrincipal.getListaAlojamientos().stream()
                        .map(Alojamiento::getNombre)
                        .collect(Collectors.toList())
        ));
        // Initialize reservations and installations
        observableListReservas = FXCollections.observableArrayList(appReservasPrincipal.listarTodasReservas());
        observableListaAlojamientos = FXCollections.observableArrayList(appReservasPrincipal.getListaAlojamientos());

        // Populate TableViews
        tablaReservas.setItems(observableListReservas);
//        tablaInstalaciones.setItems(observableListaAlojamientos);

        // Event handler for selecting an instalacion
        cbTipoAlojamiento.setOnAction(event -> {
            cargarHorasDisponibles();
            String selectedInstalacion = cbTipoAlojamiento.getValue();
            Alojamiento instalacion = appReservasPrincipal.getListaAlojamientos().stream()
                    .filter(inst -> inst.getNombre().equals(selectedInstalacion))
                    .findFirst()
                    .orElse(null);
            if (instalacion != null) {
                txtidInstalacion.setText(instalacion.getNombre());
            }
        });

        // Listener for automatic cost calculation when cedula changes
        txtCedula.textProperty().addListener((observable, oldValue, newValue) -> {
            calcularYMostrarCosto();
        });

        cbTipoAlojamiento.setItems(FXCollections.observableArrayList(
                appReservasPrincipal.getListaAlojamientos().stream()
                        .map(Alojamiento::getNombre)
                        .collect(Collectors.toList())
        ));

        cbTipoAlojamiento.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());
        txtCedula.textProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());

        actualizarTabla();
    }
}


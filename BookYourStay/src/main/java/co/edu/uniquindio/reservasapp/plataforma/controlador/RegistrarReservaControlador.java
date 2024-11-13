package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Horario;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RegistrarReservaControlador implements Initializable {

    @FXML
    private TextField txtNombreHospedaje;
    @FXML
    private TextField txtCedulaReservante;
    @FXML
    private TextField txtCosto;
    @FXML
    private TextField txtNumeroHuespedes;
    @FXML
    private TableView<?> tablaAlojamientos;


    private LocalDate startDate;
    private LocalDate endDate;

    private ObservableList<Alojamiento> observableListaAlojamientos;
    private ObservableList<Reserva> observableListReservas;

    private final AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    Sesion sesion = Sesion.getInstancia();
    Persona persona = sesion.getPersona();



//    public void crearReserva(ActionEvent actionEvent) {
//        try {
//            String ciudadAlojamiento = cbCiudadSeleccionado.getValue();
//            String nombreHospedaje = txtNombreHospedaje.getText();
//            String cedulaReservante = txtCedulaReservante.getText();
//            String horaInicioReserva = cbhoraInicioReserva.getValue();
//            String horaFinReserva = cbhoraFinReserva.getValue();
//            double costo = Double.parseDouble(txtCosto.getText());
//            int capacidadMaxima = Integer.parseInt(txtNumeroHuespedes.getText());
//
//            if (startDate == null || endDate == null || ciudadAlojamiento == null || nombreHospedaje.isEmpty() || cedulaReservante.isEmpty() || horaInicioReserva == null || horaFinReserva == null) {
//                mostrarAlerta("All fields must be completed to create a reservation.", Alert.AlertType.WARNING);
//                return;
//            }
//
//            long diasReservados = ChronoUnit.DAYS.between(startDate, endDate);
//            Reserva reserva = appReservasPrincipal.crearReserva(ciudadAlojamiento, nombreHospedaje, cedulaReservante, startDate, endDate, horaInicioReserva, horaFinReserva, costo, capacidadMaxima);
//            observableListReservas.add(reserva);
//            limpiarFormularioReserva();
//            actualizarTabla();
//            mostrarAlerta("Reservation created successfully.", Alert.AlertType.INFORMATION);
//        } catch (Exception e) {
//            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }





//    private void limpiarFormularioReserva() {
//        cbCiudadSeleccionado.setValue(null);
//        txtNombreHospedaje.setText("");
//        txtCedulaReservante.setText("");
//        dpDiaInicioReserva.setValue(null);
//        dpDiaFinReserva.setValue(null);
//        startDate = null;
//        endDate = null;
//        cbhoraInicioReserva.setValue(null);
//        cbhoraFinReserva.setValue(null);
//        txtCosto.setText("");
//        txtNumeroHuespedes.setText("");
//    }


    private void actualizarTabla() {
        if (observableListReservas != null) {
            observableListReservas.setAll(appReservasPrincipal.listarTodasReservas());
        }
        if (observableListaAlojamientos != null) {
            observableListaAlojamientos.setAll(appReservasPrincipal.getListaAlojamientos());
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public List<String> cargarCiudades() {
        // Convert the Ciudad enum values to a List<String>
        return Arrays.stream(Ciudad.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }





    public void cargarReservas(ActionEvent actionEvent) {
        appReservasPrincipal.getListaReservas().stream()
                .filter(reserva -> reserva.getCedulaReservante().equals(txtCedulaReservante.getText()))
                .forEach(System.out::println);
    }

//    public void cargarHorasDisponibles() {
//        String selectedInstalacion = txtNombreHospedaje.getText();
//
//        if (selectedInstalacion != null) {
//            Alojamiento alojamiento = appReservasPrincipal.getListaAlojamientos().stream()
//                    .filter(inst -> inst.getNombre().equals(selectedInstalacion))
//                    .findFirst()
//                    .orElse(null);
//
//            if (alojamiento != null) {
////                LocalDateTime horaInicio = cbhoraInicioReserva.getValue() != null ? LocalDateTime.of(dpDiaInicioReserva.getValue(), LocalTime.parse(cbhoraInicioReserva.getValue())) : alojamiento.getHoraInicio();
//                LocalDateTime horaInicio = LocalDateTime.parse(cbhoraInicioReserva.getValue());
//                LocalDateTime horaFin = LocalDateTime.parse(cbhoraFinReserva.getValue());
//
//
//                ObservableList<String> horasDisponibles = FXCollections.observableArrayList();
//                LocalTime hora = horaInicio.toLocalTime();
//
//                while (!hora.isAfter(horaFin.toLocalTime())) {
//                    horasDisponibles.add(hora.toString());
//                    hora = hora.plusHours(1); // Adjust as needed for different intervals
//                }
//
////                cbhoraReserva.setItems(horasDisponibles);
//            }
//        }
//    }

//    public void actualizarCosto() {
//        try {
//            String tipoInstalacion = cbCiudadSeleccionado.getValue();
//            String cedula = txtCedulaReservante.getText();
//
//            if (tipoInstalacion != null && cedula != null && !cedula.isEmpty()) {
//                double costo = appReservasPrincipal.calcularCosto(tipoInstalacion, cedula);
//                txtCosto.setText(String.format("%.2f", costo));
//            }
//        } catch (Exception e) {
//            mostrarAlerta("Error al calcular el costo: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }

//    public void cargarCostoReserva() {
//        String tipoInstalacion = cbCiudadSeleccionado.getValue();
//        String cedula = txtCedulaReservante.getText();
//
//        if (tipoInstalacion != null && !cedula.isEmpty()) {
//            try {
//                Persona persona = appReservasPrincipal.obtenerPersona(cedula).orElseThrow(() -> new Exception("Persona no encontrada"));
//                double costo = appReservasPrincipal.calcularCosto(tipoInstalacion, cedula);
//                txtCosto.setText(String.valueOf(costo));
//            } catch (Exception e) {
//                mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
//            }
//        }
//    }

//    private void calcularYMostrarCosto() {
//        try {
//            String tipoInstalacion = cbCiudadSeleccionado.getValue();
//            String cedula = txtCedulaReservante.getText();
//
//            // Ensure both fields are not empty before calculating the cost
//            if (tipoInstalacion != null && !tipoInstalacion.isEmpty() && cedula != null && !cedula.isEmpty()) {
//                double costo = appReservasPrincipal.calcularCosto(tipoInstalacion, cedula);
//                txtCosto.setText(String.format("%.2f", costo)); // Format to two decimal places
//            } else {
//                txtCosto.setText(""); // Clear cost if required fields are missing
//            }
//        } catch (Exception e) {
//            mostrarAlerta("Error al calcular el costo: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
/*
        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
        setupDateRangeSelection();

        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
        dpDiaInicioReserva.setOnAction(event -> updateEndDate());
        dpDiaFinReserva.setOnAction(event -> validateEndDate());

        // Initialize observableListReservas
        observableListReservas = FXCollections.observableArrayList();
        observableListaAlojamientos = FXCollections.observableArrayList();

        actualizarTabla();

        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
        observableListReservas = FXCollections.observableArrayList(appReservasPrincipal.listarTodasReservas());
        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(
                appReservasPrincipal.getListaAlojamientos().stream()
                        .map(Alojamiento::getNombre)
                        .collect(Collectors.toList())
        ));
        cbCiudadSeleccionado.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());
        txtCedulaReservante.textProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());
        actualizarTabla();


        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
//        cargarCiudadesDisponibles();
        observableListReservas = FXCollections.observableArrayList(appReservasPrincipal.listarTodasReservas());
//        tablaReservas.setItems(observableListReservas);

        observableListReservas = FXCollections.observableArrayList(appReservasPrincipal.listarTodasReservas());
//        tablaReservas.setItems(observableListReservas);

        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(
                appReservasPrincipal.getListaAlojamientos().stream()
                        .map(Alojamiento::getNombre)
                        .collect(Collectors.toList())
        ));
        // Initialize reservations and installations
        observableListReservas = FXCollections.observableArrayList(appReservasPrincipal.listarTodasReservas());
        observableListaAlojamientos = FXCollections.observableArrayList(appReservasPrincipal.getListaAlojamientos());


        // Event handler for selecting an instalacion
        cbCiudadSeleccionado.setOnAction(event -> {
            cargarHorasDisponibles();
            String selectedInstalacion = cbCiudadSeleccionado.getValue();
            Alojamiento instalacion = appReservasPrincipal.getListaAlojamientos().stream()
                    .filter(inst -> inst.getNombre().equals(selectedInstalacion))
                    .findFirst()
                    .orElse(null);
            if (instalacion != null) {
                txtNombreHospedaje.setText(instalacion.getNombre());
            }
        });
        // Listener for automatic cost calculation when cedula changes
        txtCedulaReservante.textProperty().addListener((observable, oldValue, newValue) -> {
            calcularYMostrarCosto();
        });

        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(
                appReservasPrincipal.getListaAlojamientos().stream()
                        .map(Alojamiento::getNombre)
                        .collect(Collectors.toList())
        ));
        cbCiudadSeleccionado.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());
        txtCedulaReservante.textProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());*/
        actualizarTabla();
    }
}

    /*@FXML
    public void crearReserva(ActionEvent actionEvent) {
        try {
            String ciudadAlojamiento = cbCiudadSeleccionado.getValue();
            String nombreHospedaje = txtNombreHospedaje.getText();
            String cedulaReservante = txtCedulaReservante.getText();
            String horaInicioReserva = cbhoraInicioReserva.getValue();
            String horaFinReserva = cbhoraFinReserva.getValue();
            double costo = Double.parseDouble(txtCosto.getText());
            int capacidadMaxima = Integer.parseInt(txtNumeroHuespedes.getText());

            if (ciudadAlojamiento == null || nombreHospedaje.isEmpty() || cedulaReservante.isEmpty() || cbhoraInicioReserva == null || cbhoraFinReserva == null || horaInicioReserva == null || horaFinReserva == null) {
                mostrarAlerta("Todos los campos deben estar completos para crear la reserva.", Alert.AlertType.WARNING);
                return;
            }

            long diasReservados = ChronoUnit.DAYS.between(cbhoraInicioReserva, cbhoraFinReserva);

            Reserva reserva = appReservasPrincipal.crearReserva(ciudadAlojamiento, nombreHospedaje, cedulaReservante, cbhoraInicioReserva, cbhoraFinReserva, horaInicioReserva, horaFinReserva, costo, capacidadMaxima);
            observableListReservas.add(reserva);
            limpiarFormularioReserva();
            actualizarTabla();
            mostrarAlerta("Reserva creada exitosamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }*/

/*
    // Setup for Date Range Selection
    @FXML
    private void setupDateRangeSelection() {
        dpRangoReserva.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (startDate != null && endDate == null) {
                    setDisable(empty || item.isBefore(startDate));
                }
            }
        });

        dpRangoReserva.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (startDate == null) {
                startDate = newValue;
            } else {
                endDate = newValue;
                dpRangoReserva.setValue(null);  // Reset for potential new selection
            }
        });
    }*/
    /*// Function to set up the date range selection
    @FXML
    private void setupDateRangeSelection() {
        dpRangoReserva.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (cbhoraInicioReserva != null && cbhoraFinReserva != null) {
                    setDisable(item.isBefore(cbhoraInicioReserva) || item.isAfter(cbhoraFinReserva));
                }
            }
        });

        dpRangoReserva.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (cbhoraInicioReserva == null) {
                cbhoraInicioReserva = newValue;
            } else {
                cbhoraFinReserva = newValue;
            }
        });
    }*/


/*
    @FXML
    public void crearReserva(ActionEvent actionEvent) {
        try {
            String ciudadAlojamiento = cbCiudadSeleccionado.getValue();
            String nombreHospedaje = txtNombreHospedaje.getText();
            String cedulaReservante = txtCedulaReservante.getText();
            LocalDate diaInicioReserva = dpDiaInicioReserva.getValue();
            LocalDate diaFinReserva = dpDiaFinReserva.getValue();
            String horaInicioReserva = cbhoraInicioReserva.getValue();
            String horaFinReserva = cbhoraFinReserva.getValue();
            double costo = Double.parseDouble(txtCosto.getText());
            int capacidadMaxima = Integer.parseInt(txtNumeroHuespedes.getText());

            if (ciudadAlojamiento == null || nombreHospedaje.isEmpty() || cedulaReservante.isEmpty() || diaInicioReserva == null || diaFinReserva == null || horaInicioReserva == null || horaFinReserva == null) {
                mostrarAlerta("Todos los campos deben estar completos para crear la reserva.", Alert.AlertType.WARNING);
                return;
            }

            LocalDateTime fechaInicioReserva = LocalDateTime.of(diaInicioReserva, LocalTime.parse(horaInicioReserva));
            LocalDateTime fechaFinReserva = LocalDateTime.of(diaFinReserva, LocalTime.parse(horaFinReserva));

            long diasReservados = ChronoUnit.DAYS.between(fechaInicioReserva, fechaFinReserva);

            Reserva reserva = appReservasPrincipal.crearReserva(ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, costo, capacidadMaxima);
            observableListReservas.add(reserva);
            limpiarFormularioReserva();
            actualizarTabla();
            mostrarAlerta("Reserva creada exitosamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    */


// This method is called when an accommodation is selected from the list
//    @FXML
//    private void mostrarDetallesAlojamiento() {
//        Alojamiento selectedAlojamiento = listAlojamientos.getSelectionModel().getSelectedItem();
//        if (selectedAlojamiento != null) {
//            txtDetallesAlojamiento.setText(selectedAlojamiento.getDescripcion());
//            Image image = new Image(selectedAlojamiento.getImagenURL());
//            imageAlojamiento.setImage(image);
//        }
//    }
/*
    @FXML
    private void mostrarDetallesAlojamiento() {
        Alojamiento selectedAlojamiento = listAlojamientos.getSelectionModel().getSelectedItem();
        if (selectedAlojamiento != null) {
            txtDetallesAlojamiento.setText(selectedAlojamiento.getDescripcion());

            // Use getClass().getResource to load from the resources folder
            String imagePath = selectedAlojamiento.getImagenURL();
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            imageAlojamiento.setImage(image);
        }
    }*/


//    @FXML
//    private void onAlojamientoSeleccionado() {
//        Alojamiento alojamientoSeleccionado = tablaAlojamientos.getSelectionModel().getSelectedItem();
//        if (alojamientoSeleccionado != null) {
//            txtNombreHospedaje.setText(alojamientoSeleccionado.getNombre());
//            txtCosto.setText(String.format("%.2f", alojamientoSeleccionado.getTarifa()));
//            txtNumeroHuespedes.setText(String.valueOf(alojamientoSeleccionado.getCapacidadMaxima()));
//        }
//    }


//    @FXML
//    private TableView<Reserva> tablaReservas;
//    @FXML
//    private TableColumn<Reserva, String> colidInstalacion;
//    @FXML
//    private TableColumn<Reserva, String> colNombreReservaInstalacion;
//    @FXML
//    private TableColumn<Reserva, String> colCedula;
//    @FXML
//    private TableColumn<Reserva, String> diaReserva;
//    @FXML
//    private TableColumn<Reserva, String> horaReserva;

    /*
    @FXML
    private ComboBox<String> cbCiudadSeleccionado;
    @FXML
    private TextField txtNombreHospedaje;
    @FXML
    private TextField txtCedulaReservante;
    @FXML
    private DatePicker dpDiaInicioReserva;
    @FXML
    private DatePicker dpDiaFinReserva;
    @FXML
    private ComboBox<String> cbhoraInicioReserva;
    @FXML
    private ComboBox<String> cbhoraFianReserva;
    @FXML
    private TextField txtCosto;
    @FXML
    private TextField txtCapacidadMaxima;
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

      private ObservableList<Horario> observableListHoras;
    private ObservableList<Alojamiento> observableListaAlojamientos;
    private ObservableList<Reserva> observableListReservas;

*/

/*
    public void crearReserva(ActionEvent actionEvent) {
        try {
            String ciudadAlojamiento = cbCiudadSeleccionado.getValue();
            String nombreHospedaje = txtNombreHospedaje.getText();
            String cedulaReservante = txtCedulaReservante.getText();
            LocalDate diaInicioReserva = dpDiaInicioReserva.getValue();
            LocalDate diaFinReserva = dpDiaFinReserva.getValue();
            String horaInicioReserva = cbhoraInicioReserva.getValue();
            String horaFinReserva = cbhoraFinReserva.getValue();
            double costo = Double.parseDouble(txtCosto.getText());
            int capacidadMaxima = Integer.parseInt(txtCapacidadMaxima.getText());

            if (ciudadAlojamiento == null || nombreHospedaje.isEmpty() || cedulaReservante.isEmpty() || diaInicioReserva == null || diaFinReserva == null || horaInicioReserva == null || horaFinReserva == null) {
                mostrarAlerta("Todos los campos deben estar completos para crear la reserva.", Alert.AlertType.WARNING);
                return;
            }

            LocalDateTime fechaInicioReserva = LocalDateTime.of(diaInicioReserva, LocalTime.parse(horaInicioReserva));
            LocalDateTime fechaFinReserva = LocalDateTime.of(diaFinReserva, LocalTime.parse(horaFinReserva));
//            double costoCaLculado = appReservasPrincipal.calcularCosto(nombreHospedaje, cedulaReservante);

//            double costo = appReservasPrincipal.calcularCosto(tipoAlojamiento, cedulaCliente);
            txtCosto.setText(String.format("%.2f", costo)); // Automatically set cost in UI

            Reserva reserva = appReservasPrincipal.crearReserva(ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, costo, capacidadMaxima);
            observableListReservas.add(reserva);
            limpiarFormularioReserva();
            actualizarTabla();
            mostrarAlerta("Reserva creada exitosamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }*/


//    private void limpiarFormularioReserva() {
//        cbCiudadSeleccionado.setValue(null);
//        txtNombreHospedaje.setText("");
//        txtCedulaReservante.setText("");
//        dpDiaInicioReserva.setValue(null);
//        dpDiaFinReserva.setValue(null);
//        cbhoraInicioReserva.setValue(null);
//        cbhoraFianReserva.setValue(null);
//        txtCosto.setText("");
//        txtCapacidadMaxima.setText("");
//    }

//    private void cargarCiudadesDisponibles() {
//        List<String> ciudades = appReservasPrincipal.getListaAlojamientos().stream()
//                .map(Alojamiento::getCiudad)
//                .distinct()
//                .collect(Collectors.toList());
//        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(ciudades));
//    }


//    public void cargarIntalaciones(ActionEvent actionEvent) {
//        appReservasPrincipal.getListaAlojamientos().stream()
//                .filter(instalacion -> instalacion.getNombre().equals(cbCiudadSeleccionado.getValue()))
////                .filter(reserva -> reserva.getCedula().equals(txtCedula.getText()))
//                .forEach(System.out::println);
//    }
/*
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
//        cargarCiudadesDisponibles();
        observableListReservas = FXCollections.observableArrayList(appReservasPrincipal.listarTodasReservas());
        tablaReservas.setItems(observableListReservas);
    }

*/
       /*
        colNombreInstalacion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colcodigoIdInstalacion.setCellValueFactory(new PropertyValueFactory<>("codigoId"));
        colidInstalacion.setCellValueFactory(new PropertyValueFactory<>("idInstalacion"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        diaReserva.setCellValueFactory(new PropertyValueFactory<>("diaReserva"));
        horaReserva.setCellValueFactory(new PropertyValueFactory<>("horaReserva"));

        colidInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreHospedaje().toString()));
//        colNombreReservaInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreHospedaje(observableListaAlojamientos)));
        colNombreReservaInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreHospedaje()));


        //  colNombreReservaInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreInstalacion()));
        colCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedulaReservante()));
        diaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDiaInicioReserva().toString()));
//        horaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoraReserva()));
//
//        cbhoraReserva.setItems(FXCollections.observableArrayList(new Horario().generarHorarios()));*/

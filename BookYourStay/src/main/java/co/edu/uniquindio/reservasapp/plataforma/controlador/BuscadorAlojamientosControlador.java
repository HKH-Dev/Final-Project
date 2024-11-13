package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BuscadorAlojamientosControlador implements Initializable {
    @FXML
    private DatePicker dpDiasReservar;
    @FXML
    private ListView<Alojamiento> listAlojamientos;
    @FXML
    private ImageView imageAlojamiento;
    @FXML
    private TextArea txtDetallesAlojamiento;
    @FXML
    private ComboBox<String> cbCiudadSeleccionado;

    private LocalDate startDate;
    private LocalDate endDate;
    private ObservableList<Alojamiento> observableListaAlojamientos;

    private final AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

    @FXML
    private void mostrarDetallesAlojamiento() {
        Alojamiento selectedAlojamiento = listAlojamientos.getSelectionModel().getSelectedItem();
        if (selectedAlojamiento != null) {
            txtDetallesAlojamiento.setText(selectedAlojamiento.getDescripcion());
            String imagePath = selectedAlojamiento.getImagenURL();
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            imageAlojamiento.setImage(image);
        }
    }

    private void setupDateRangeSelection() {
        dpDiasReservar.setDayCellFactory(createDayCellFactory());

        dpDiasReservar.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                // Handle the case where the new value is null
                return;
            }

            if (startDate == null || (startDate != null && endDate != null)) {
                startDate = newValue;
                endDate = null;
            } else {
                endDate = newValue;
                dpDiasReservar.setValue(null); // Clear to keep DatePicker open
            }
            dpDiasReservar.setDayCellFactory(createDayCellFactory()); // Refresh to update highlights
        });

        dpDiasReservar.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (startDate != null && endDate != null) {
                    return "From: " + startDate + " To: " + endDate + " (" + calculateReservationDays() + " days)";
                }
                return (date != null) ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return null; // No need for reverse conversion
            }
        });
    }
    private Callback<DatePicker, DateCell> createDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Disable past dates
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;"); // Light gray for disabled past dates
                } else if (startDate != null && endDate != null) {
                    // Different color for start date
                    if (item.equals(startDate)) {
                        setStyle("-fx-background-color: #add8e6;"); // Light blue for start date
                    }
                    // Different color for end date
                    else if (item.equals(endDate)) {
                        setStyle("-fx-background-color: #ffa07a;"); // Light salmon for end date
                    }
                    // Color for dates within the range
                    else if (!item.isBefore(startDate) && !item.isAfter(endDate)) {
                        setStyle("-fx-background-color: #90ee90;"); // Light green for range
                    }
                }
            }
        };
    }

    private long calculateReservationDays() {
        if (startDate != null && endDate != null) {
            return ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        return 0;
    }

    @FXML
    private void onCiudadSeleccionada() {
        String ciudadSeleccionada = cbCiudadSeleccionado.getValue();
        if (ciudadSeleccionada != null) {
            List<Alojamiento> alojamientosFiltrados = appReservasPrincipal.getListaAlojamientos().stream()
                    .filter(aloj -> aloj.getCiudad().equals(ciudadSeleccionada))
                    .collect(Collectors.toList());
            observableListaAlojamientos = FXCollections.observableArrayList(alojamientosFiltrados);
            listAlojamientos.setItems(observableListaAlojamientos);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDateRangeSelection();
        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
//        ----------------------------------------------



        // Add an event handler for the DatePicker showing event
        dpDiasReservar.setOnShowing(event -> {
            if (dpDiasReservar.getEditor() != null) {
                dpDiasReservar.getEditor().clear();
            }
        });
        // Add an event handler for the DatePicker action event
        dpDiasReservar.setOnAction(event -> {
            if (dpDiasReservar.getEditor() != null) {
                dpDiasReservar.getEditor().clear();
            }
        });



    }

    public List<String> cargarCiudades() {
        return Arrays.stream(Ciudad.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
    /*@FXML
    private DatePicker dpDiasReservar; // This will be used as startDatePicker
    @FXML
    private DatePicker dpRangoReserva; // This will be used as endDatePicker
    @FXML
    private ListView<Alojamiento> listAlojamientos;
    @FXML
    private ImageView imageAlojamiento;
    @FXML
    private TextArea txtDetallesAlojamiento;
    @FXML
    private ComboBox<String> cbCiudadSeleccionado;

    private LocalDate startDate;
    private LocalDate endDate;
    private ObservableList<Alojamiento> observableListaAlojamientos;
    private final AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

    @FXML
    private void mostrarDetallesAlojamiento() {
        Alojamiento selectedAlojamiento = listAlojamientos.getSelectionModel().getSelectedItem();
        if (selectedAlojamiento != null) {
            txtDetallesAlojamiento.setText(selectedAlojamiento.getDescripcion());
            String imagePath = selectedAlojamiento.getImagenURL();
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            imageAlojamiento.setImage(image);
        }
    }

    private void setupDateRangeSelection() {
        // Using dpDiasReservar as the start date picker and dpRangoReserva as the end date picker
        dpDiasReservar.setOnAction(e -> {
            startDate = dpDiasReservar.getValue();
            if (startDate != null && endDate != null) {
                highlightDateRange(startDate, endDate);
            }
        });

        dpRangoReserva.setOnAction(e -> {
            endDate = dpRangoReserva.getValue();
            if (startDate != null && endDate != null) {
                highlightDateRange(startDate, endDate);
            }
        });
    }

    private void highlightDateRange(LocalDate start, LocalDate end) {
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Highlight range between start and end dates
                if (item != null && (item.isAfter(start) && item.isBefore(end))) {
                    setStyle("-fx-background-color: #C3FDB8;");  // Highlight color
                }
                // Disable dates before today
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");  // Gray color for disabled dates
                }
            }
        };

        dpDiasReservar.setDayCellFactory(dayCellFactory); // Apply to start date picker
        dpRangoReserva.setDayCellFactory(dayCellFactory); // Apply to end date picker
    }

    @FXML
    private void onCiudadSeleccionada() {
        String ciudadSeleccionada = cbCiudadSeleccionado.getValue();
        if (ciudadSeleccionada != null) {
            List<Alojamiento> alojamientosFiltrados = appReservasPrincipal.getListaAlojamientos().stream()
                    .filter(aloj -> aloj.getCiudad().equals(ciudadSeleccionada))
                    .collect(Collectors.toList());
            observableListaAlojamientos = FXCollections.observableArrayList(alojamientosFiltrados);
            listAlojamientos.setItems(observableListaAlojamientos);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDateRangeSelection();
        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
    }

    private long calculateReservationDays() {
        if (startDate != null && endDate != null) {
            return ChronoUnit.DAYS.between(startDate, endDate) + 1; // Include both start and end
        }
        return 0;
    }

    public List<String> cargarCiudades() {
        // Convert the Ciudad enum values to a List<String>
        return Arrays.stream(Ciudad.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

}*/
//-------------------------------------------------------------
/*
    private void setupDateRangeSelection() {
        // Custom cell factory for DatePicker to highlight the range
        dpDiasReservar.setDayCellFactory(createDayCellFactory());

        dpDiasReservar.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (startDate == null || (startDate != null && endDate != null)) {
                startDate = newValue;
                endDate = null;
            } else {
                endDate = newValue;
                dpDiasReservar.setValue(null); // Clear to keep DatePicker open
            }
            dpDiasReservar.setDayCellFactory(createDayCellFactory()); // Refresh to update highlights
        });

        dpDiasReservar.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (startDate != null && endDate != null) {
                    return "From: " + startDate + " To: " + endDate + " (" + calculateReservationDays() + " days)";
                }
                return (date != null) ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return null; // No need for reverse conversion
            }
        });
    }

    private Callback<DatePicker, DateCell> createDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Highlight dates between startDate and endDate
                if (startDate != null && endDate != null && !item.isBefore(startDate) && !item.isAfter(endDate)) {
                    setStyle("-fx-background-color: #90ee90;"); // Light green for range
                }

                // Disable past dates
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;"); // Light gray for disabled past dates
                }
            }
        };
    }

    private long calculateReservationDays() {
        if (startDate != null && endDate != null) {
            return ChronoUnit.DAYS.between(startDate, endDate) + 1; // Include both start and end
        }
        return 0;
    }






    @FXML
    private void onCiudadSeleccionada() {
        String ciudadSeleccionada = cbCiudadSeleccionado.getValue();
        if (ciudadSeleccionada != null) {
            List<Alojamiento> alojamientosFiltrados = appReservasPrincipal.getListaAlojamientos().stream()
                    .filter(aloj -> aloj.getCiudad().equals(ciudadSeleccionada))
                    .collect(Collectors.toList());
            observableListaAlojamientos = FXCollections.observableArrayList(alojamientosFiltrados);
            listAlojamientos.setItems(observableListaAlojamientos);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDateRangeSelection();
        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));
    }
/*
    private Callback<DatePicker, DateCell> createDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (startDate != null && (item.isBefore(startDate) || (endDate != null && item.isAfter(endDate)))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Light pink for disabled dates
                } else if (item.equals(startDate) || (endDate != null && (item.isAfter(startDate) && item.isBefore(endDate)))) {
                    setStyle("-fx-background-color: #90ee90;"); // Light green for selected range
                }

//                // Highlight dates between firstDate and secondDate
//                if (firstDate != null && secondDate != null) {
//                    if ((item.isAfter(firstDate) || item.equals(firstDate)) &&
//                            (item.isBefore(secondDate) || item.equals(secondDate))) {
//                        setStyle("-fx-background-color: #90ee90;"); // Light green
//                    }
//                }

                // Optionally disable past or future dates if needed
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;"); // Light gray for disabled past dates
                }
            }
        };
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDateRangeSelection();

        // Custom cell factory to highlight the selected range
        dpDiasReservar.setDayCellFactory(createDayCellFactory());

        dpDiasReservar.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (startDate == null || (startDate != null && endDate != null)) {
                startDate = newDate;
                endDate = null;
            } else {
                endDate = newDate;
                dpDiasReservar.setValue(null); // Clear the DatePicker value
            }
            dpDiasReservar.setDayCellFactory(createDayCellFactory()); // Refresh cells to update highlights
        });

        dpDiasReservar.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (startDate != null && endDate != null) {
                    return "From: " + startDate + " To: " + endDate;
                }
                return (date != null) ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return null;  // No conversion needed from string to date
            }
        });


        dpDiasReservar.setOnAction(event -> {
            LocalDate selectedDate = dpDiasReservar.getValue();
            if (selectedDate == null) {
                return;
            }
            if (startDate == null || (startDate != null && endDate != null)) {
                startDate = selectedDate;
                endDate = null;
            } else {
                endDate = selectedDate;
                dpDiasReservar.setValue(null); // Clear the DatePicker value
            }
            dpDiasReservar.setDayCellFactory(createDayCellFactory()); // Refresh cells to update highlights
        });


        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));

    }

    */
/*dpDiasReservar.setOnAction(event -> {
            LocalDate selectedDate = dpDiasReservar.getValue();
            if (selectedDate == null) {
                return;
            }
            if (firstDate == null) {
                // First date selected
                firstDate = selectedDate;
                secondDate = null; // Reset second date
            } else if (secondDate == null) {
                // Second date selected
                secondDate = selectedDate.isAfter(firstDate) ? selectedDate : firstDate;
                firstDate = selectedDate.isAfter(firstDate) ? firstDate : selectedDate;
                dpDiasReservar.setValue(null); // Clear selection to keep DatePicker open
                dpDiasReservar.show(); // Keep the DatePicker open
            } else {
                // Reset if both dates are already selected
                firstDate = selectedDate;
                secondDate = null;
            }
            dpDiasReservar.setDayCellFactory(createDayCellFactory()); // Refresh cells to update highlights
        });

 /* // Custom string converter to keep the DatePicker open until the range is selected
        dpDiasReservar.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (firstDate != null && secondDate != null) {
                    return "From: " + firstDate + " To: " + secondDate;
                }
                return (date != null) ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return null;  // No conversion needed from string to date
            }
        });
//    @FXML
//    private DatePicker dpDiaInicioReserva;
//    @FXML
//    private DatePicker dpDiaFinReserva;
//    @FXML
//    private ComboBox<String> cbhoraInicioReserva;
//    @FXML
//    private ComboBox<String> cbhoraFinReserva;



//    @FXML
//    private void onDateRangeSelected() {
//        dpDiaInicioReserva.setOnAction(event -> updateEndDate());
//        dpDiaFinReserva.setOnAction(event -> validateEndDate());
//    }

//    private void updateEndDate() {
//        LocalDate startDate = dpDiaInicioReserva.getValue();
//        if (startDate != null) {
//            dpDiaFinReserva.setDayCellFactory(datePicker -> new DateCell() {
//                @Override
//                public void updateItem(LocalDate item, boolean empty) {
//                    super.updateItem(item, empty);
//                    setDisable(empty || item.isBefore(startDate));
//                }
//            });
//        }
//    }

//    private void validateEndDate() {
//        if (dpDiaFinReserva.getValue().isBefore(dpDiaInicioReserva.getValue())) {
//            dpDiaFinReserva.setValue(dpDiaInicioReserva.getValue());
//        }
//    }

//    @FXML
//    private void cargarAlojamientos() {
//        String selectedCity = cbCiudadSeleccionado.getValue();
//        if (selectedCity != null) {
//            List<Alojamiento> alojamientos = appReservasPrincipal.getAlojamientosPorCiudad(selectedCity);
//            listAlojamientos.setItems(FXCollections.observableArrayList(alojamientos));
//        }
//    }
//


   /* private Callback<DatePicker, DateCell> createDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {

                super.updateItem(item, empty);

                if (startDate != null && (item.isBefore(startDate) || (endDate != null && item.isAfter(endDate)))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Light pink for disabled dates
                } else if (item.equals(startDate) || (endDate != null && (item.isAfter(startDate) && item.isBefore(endDate)))) {
                    setStyle("-fx-background-color: #90ee90;"); // Light green for selected range
                }


                // Highlight dates between firstDate and secondDate
                if (firstDate != null && secondDate != null) {
                    if ((item.isAfter(firstDate) || item.equals(firstDate)) &&
                            (item.isBefore(secondDate) || item.equals(secondDate))) {
                        setStyle("-fx-background-color: #90ee90;"); // Light green
                    }
                }

                // Optionally disable past or future dates if needed
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;"); // Light gray for disabled past dates
                }
            }
        };
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Custom cell factory to highlight the selected range
        dpDiasReservar.setDayCellFactory(createDayCellFactory());

        dpDiasReservar.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (startDate == null || (startDate != null && endDate != null)) {
                startDate = newDate;
                endDate = null;
            } else {
                endDate = newDate;
                dpDiasReservar.setValue(null); // Clear the DatePicker value
            }
            dpDiasReservar.setDayCellFactory(createDayCellFactory()); // Refresh cells to update highlights
        });


        // Custom string converter to keep the DatePicker open until the range is selected
        dpDiasReservar.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (firstDate != null && secondDate != null) {
                    return "From: " + firstDate + " To: " + secondDate;
                }
                return (date != null) ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return null;  // No conversion needed from string to date
            }
        });
        dpDiasReservar.setOnAction(event -> {
            LocalDate selectedDate = dpDiasReservar.getValue();
            if (firstDate == null) {
                // First date selected
                firstDate = selectedDate;
                secondDate = null; // Reset second date
            } else if (secondDate == null) {
                // Second date selected
                secondDate = selectedDate.isAfter(firstDate) ? selectedDate : firstDate;
                firstDate = selectedDate.isAfter(firstDate) ? firstDate : selectedDate;
                dpDiasReservar.setValue(null); // Clear selection to keep DatePicker open
                dpDiasReservar.show(); // Keep the DatePicker open
            } else {
                // Reset if both dates are already selected
                firstDate = selectedDate;
                secondDate = null;
            }
            dpDiasReservar.setDayCellFactory(createDayCellFactory()); // Refresh cells to update highlights
        });

        cbCiudadSeleccionado.setItems(FXCollections.observableArrayList(cargarCiudades()));

//        dpDiaInicioReserva.setOnAction(event -> updateEndDate());
//        dpDiaFinReserva.setOnAction(event -> validateEndDate());
    }





//cbhoraReserva.setItems(FXCollections.observableArrayList(new Horario().generarHorarios()));
//    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        txtCedulaReservante.textProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());
        actualizarTabla();
    }*/




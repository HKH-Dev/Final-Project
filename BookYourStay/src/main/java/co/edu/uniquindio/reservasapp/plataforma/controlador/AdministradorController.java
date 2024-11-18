package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto.Apartamento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa.Casa;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.hotel.Hotel;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;

public class AdministradorController implements Initializable {

    @FXML
    private TableView<Alojamiento> tablaAlojamientos;

    @FXML
    private TableColumn<Alojamiento, String> columnaNombre;

    @FXML
    private TableColumn<Alojamiento, String> columnaCiudad;

    @FXML
    private TableColumn<Alojamiento, String> columnaDescripcion;

    @FXML
    private TableColumn<Alojamiento, String> columnaPrecio;

    @FXML
    private TableColumn<Alojamiento, String> columnaCapacidad;

    @FXML
    private Button btnAgregarAlojamiento;

    @FXML
    private Button btnEditarAlojamiento;

    @FXML
    private Button btnEliminarAlojamiento;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnMostrarEstadisticas;
    @FXML
    private Button btnDesseleccionar;

    @FXML
    private ChoiceBox<?> choiceTipoEstablecimiento;

    @FXML
    private GridPane panelCRUD;

    @FXML
    private MenuItem menuModificarPerfil;

    @FXML
    private MenuItem menuCerrarSesion;

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private Alojamiento alojamientoSeleccionado;
    private Sesion sesionActual = Sesion.getInstancia();
    private ObservableList<Alojamiento> alojamientosVisible;
    private AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();


    @FXML
    void abrirVentanaStats(ActionEvent event) {

    }

    private void seleccionTableListener(){
        tablaAlojamientos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                alojamientoSeleccionado = newValue;
            CargarCRUD();}
        });
    }


    @FXML
    void onAgregarAlojamiento(ActionEvent event) {
        tablaAlojamientos.getSelectionModel().clearSelection();
        controladorPrincipal.cargarFXMLEnPanel(panelCRUD, "SubventanasAdministradorP/CRUDAlojamientoNuevo.fxml");
    }

    @FXML
    void onCerrarSesion(ActionEvent event) {
        try {
            Sesion.getInstancia().setPersona(null);
            controladorPrincipal.navegarVentana("/inicio.fxml", "Inicio");
            controladorPrincipal.cerrarVentana(btnActualizar);
        } catch (Exception e){e.printStackTrace();
        }
    }

    @FXML
    void onGestionarDescuentos(ActionEvent event) {
        controladorPrincipal.navegarVentana("/GestionDescuentosP.fxml", "Descuentos");
    }

    @FXML
    void onEliminarAlojamiento(ActionEvent event) {

    }

    @FXML
    void onModificarPerfil(ActionEvent event) {
        try {
            controladorPrincipal.navegarVentana("/UserEditarPerfil.fxml", "Editar mis datos");
        } catch (Exception e) {e.printStackTrace();}    }

    public void CargarCRUD(){
        Map<Class<?>, String> alojamientoToFXML = new HashMap<>();
        alojamientoToFXML.put(Apartamento.class, "SubventanasAdministradorP/CRUDApartamento.fxml");
        alojamientoToFXML.put(Hotel.class, "SubventanasAdministradorP/CRUDHotel.fxml");
        alojamientoToFXML.put(Casa.class, "SubventanasAdministradorP/CRUDCasa.fxml");
        String fxmlRUta = alojamientoToFXML.get(alojamientoSeleccionado.getClass());
        if (fxmlRUta != null){
            controladorPrincipal.cargarFXMLEnPanel(panelCRUD, fxmlRUta);
            System.out.println("Cargando panel");
        } else {
            controladorPrincipal.cargarFXMLEnPanel(panelCRUD, "SubventanasAdministradorP/CRUDAlojamientoNuevo.fxml");
        }
    }

    private void actualizarTabla(){
        tablaAlojamientos.setItems(alojamientosVisible);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        alojamientosVisible = FXCollections.observableArrayList(appReservasPrincipal.getListaAlojamientos());
        seleccionTableListener();
        columnaNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNombre())));
        columnaCiudad.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCiudad().name());
        });
        columnaDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getDescripcion())));
        columnaPrecio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getPrecioNoche())));
        columnaCapacidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        actualizarTabla();

    }
}

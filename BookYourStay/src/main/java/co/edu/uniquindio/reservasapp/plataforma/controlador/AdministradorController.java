package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.ObserverPatern.Observable;
import co.edu.uniquindio.reservasapp.ObserverPatern.Observer;
import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto.Apartamento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa.Casa;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.TipoAlojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.TipoAlojamientoObservableList;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.hotel.Hotel;
import co.edu.uniquindio.reservasapp.plataforma.controlador.CRUDControllers.CRUDCasaController;
import co.edu.uniquindio.reservasapp.plataforma.controlador.CRUDControllers.CRUDController;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
@NoArgsConstructor
public class AdministradorController implements Observer, Initializable {

    @Override
    public void update() {
        updateTabla();
        panelCRUD.getChildren().clear();
    }
    private static ObjectProperty<Alojamiento> alojamientoSeleccionadoProperty = new SimpleObjectProperty<>();

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
    private Label lblTipoAlojamiento;

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
    @FXML
    private ComboBox comboTipoAlojamiento;

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private static Alojamiento alojamientoSeleccionado;
    private Sesion sesionActual = Sesion.getInstancia();
    private ObservableList<Alojamiento> alojamientosVisible;
    private AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

    @FXML
    private void abrirVentanaStats(ActionEvent event) {
        controladorPrincipal.navegarVentana("/EstadisticasP.fxml", "Stats");
    }

    private void seleccionTableListener() {
        tablaAlojamientos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                alojamientoSeleccionado = newValue;
                alojamientoSeleccionadoProperty.set(newValue);
                comboTipoAlojamiento.getSelectionModel().clearSelection();
                CargarCRUD();
            }
        });
    }

    public static Alojamiento getAlojamientoSeleccionado() {
        return alojamientoSeleccionado;
    }

    public static ObjectProperty<Alojamiento> getAlojamientoSeleccionadoProperty() {
        return alojamientoSeleccionadoProperty;
    }

    private void visibilidadNodosAlojamiento() {
        tablaAlojamientos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lblTipoAlojamiento.setVisible(false);
                comboTipoAlojamiento.setVisible(false);
            } else {
                lblTipoAlojamiento.setVisible(true);
                comboTipoAlojamiento.setVisible(true);
            }
        });
    }

    private void cargarCRUDCBTipoAlojamiento() {
        comboTipoAlojamiento.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            Map<TipoAlojamiento, String> alojamientoToFXML = new HashMap<>();
            alojamientoToFXML.put(TipoAlojamiento.HOTEL, "/CRUDHotel.fxml");
            alojamientoToFXML.put(TipoAlojamiento.APARTAMENTO, "/CRUDApartamento.fxml");
            alojamientoToFXML.put(TipoAlojamiento.CASA, "/CRUDCasa.fxml");

            TipoAlojamiento alojamientoSeleccionadoCB = (TipoAlojamiento) newValue;

            String fxmlRuta = alojamientoToFXML.get(alojamientoSeleccionadoCB);

            if (fxmlRuta != null) {
                controladorPrincipal.cargarFXMLEnPanelcoObserver(panelCRUD, fxmlRuta, this);
                System.out.println("Cargando panel para " + alojamientoSeleccionadoCB);
            } else {
                controladorPrincipal.cargarFXMLEnPanelcoObserver(panelCRUD, "/CRUDAlojamientoNuevo.fxml", this);
            }
        });
    }

    @FXML
    void onAgregarAlojamiento(ActionEvent event) {
        tablaAlojamientos.getSelectionModel().clearSelection();
        controladorPrincipal.cargarFXMLEnPanelcoObserver(panelCRUD, "/CRUDAlojamientoNuevo.fxml", this);
    }

    @FXML
    void onCerrarSesion(ActionEvent event) {
        try {
            Sesion.getInstancia().setPersona(null);
            controladorPrincipal.navegarVentana("/inicio.fxml", "Inicio");
            controladorPrincipal.cerrarVentana(btnAgregarAlojamiento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onGestionarDescuentos(ActionEvent event) {
        controladorPrincipal.navegarVentana("/GestionDescuentosP.fxml", "Descuentos");

    }

    @FXML
    void onEliminarAlojamiento(ActionEvent event) {
    }

    private void setItemsCBAlojamiento() {
        ObservableList<TipoAlojamiento> listaAlojamientos = TipoAlojamientoObservableList.obtenerListaAlojamientos();
        comboTipoAlojamiento.setItems(listaAlojamientos);
    }
    @FXML
    void onModificarPerfil(ActionEvent event) {
        try {
            controladorPrincipal.navegarVentana("/UserEditarPerfil.fxml", "Editar mis datos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CargarCRUD() {if (alojamientoSeleccionado != null){
        Map<Class<?>, String> alojamientoToFXML = new HashMap<>();
        alojamientoToFXML.put(Apartamento.class, "/CRUDApartamento.fxml");
        alojamientoToFXML.put(Hotel.class, "/CRUDHotel.fxml");
        alojamientoToFXML.put(Casa.class, "/CRUDCasa.fxml");
        String fxmlRUta = alojamientoToFXML.get(alojamientoSeleccionado.getClass());
        if (fxmlRUta != null) {
            controladorPrincipal.cargarFXMLEnPanelcoObserver(panelCRUD, fxmlRUta, this);
            System.out.println("Cargando panel");
        }
        else if (alojamientoSeleccionado == null)
        {controladorPrincipal.cargarFXMLEnPanelcoObserver(panelCRUD, "/CRUDAlojamientoNuevo.fxml", this);}
        }
    }
    private void updateTabla(){
        alojamientosVisible.clear();
        alojamientosVisible.addAll(appReservasPrincipal.getListaAlojamientos());
        tablaAlojamientos.refresh();
    }
    private void actualizarTabla() {
        columnaNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNombre())));
        columnaCiudad.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCiudad().name());
        });
        columnaDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getDescripcion())));
        columnaPrecio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getPrecioNoche())));
        columnaCapacidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        tablaAlojamientos.setItems(alojamientosVisible);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        alojamientoSeleccionadoProperty.set(alojamientoSeleccionado);
        alojamientosVisible = FXCollections.observableArrayList(appReservasPrincipal.getListaAlojamientos());
        seleccionTableListener();
        setItemsCBAlojamiento();
        visibilidadNodosAlojamiento();
        cargarCRUDCBTipoAlojamiento();
        actualizarTabla();

    }
}

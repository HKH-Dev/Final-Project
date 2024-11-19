package co.edu.uniquindio.reservasapp.plataforma.controlador.CRUDControllers;

import co.edu.uniquindio.reservasapp.ObserverPatern.Observable;
import co.edu.uniquindio.reservasapp.ObserverPatern.Observer;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa.Casa;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.CiudadObservable;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.TipoAlojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.TipoAlojamientoObservableList;
import co.edu.uniquindio.reservasapp.plataforma.controlador.AdministradorController;
import co.edu.uniquindio.reservasapp.plataforma.controlador.ControladorPrincipal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.EventListener;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class CRUDCasaController extends CRUDController implements Observable, Initializable {

    Set<Observer> observerSet = new HashSet<>();
    @Override
    public void addObserver(Observer observer){
        observerSet.add(observer);
        System.out.println("Can" + observerSet.size());
    }
    @Override
    public void deleteObserver(Observer observer){
    };
    @Override
    public void notifyObserver(){
            for (Observer observer :observerSet){
                observer.update();
            }

    }
    public static Alojamiento alojamientoSeleccionado = AdministradorController.getAlojamientoSeleccionado();
    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private IntegerProperty indiceImagen = new SimpleIntegerProperty();

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Ciudad> comboCiudad;
    @FXML
    private Button btnServicios;
    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtImagenes;

    @FXML
    private Button btnAgregarImagen;

    @FXML
    private Button btnBorrarImagen;

    @FXML
    private TextField txtPrecioNoche;

    @FXML
    private TextField txtCapacidadMaxima;


    @FXML
    private CheckBox chkTieneCocina;

    @FXML
    private CheckBox chkTieneJardin;

    @FXML
    private ImageView imageView;

    @FXML
    private Button btnAnteriorImagen;

    @FXML
    private Button btnSiguienteImagen;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnEliminar;

    @FXML
    private void onMirarServicios(Event event){
        controladorPrincipal.navegarVentana("ServiciosVentana.fxml", "Servicios");
    }

    @FXML
    private void onAnteriorImagen(Event e) {
        if (alojamientoSeleccionado != null) {
            if (indiceImagen.get() > 0) {
                indiceImagen.set(indiceImagen.get() - 1);
            }
        }
    }

    @FXML
    private void onSiguienteImagen(Event e) {
        if (alojamientoSeleccionado != null) {
            if (indiceImagen.get() < alojamientoSeleccionado.getImagenes().size() - 1) {
                indiceImagen.set(indiceImagen.get() + 1);
            }
        }
    }



    public void actualizarAlojamientosSeccionado(){if (alojamientoSeleccionado != null)
        AdministradorController.getAlojamientoSeleccionadoProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null && newValue instanceof Casa) {alojamientoSeleccionado = newValue;
            setDatosAlojamientoSeleccionado((Casa) newValue);
            cargarImagenes(alojamientoSeleccionado, imageView, indiceImagen);
        }
    });
    }

    public void setDatosAlojamientoSeleccionado(Casa alojamiento) {
        if (alojamientoSeleccionado != null && alojamientoSeleccionado instanceof Casa){
        txtNombre.setText(alojamiento.getNombre());
        comboCiudad.setValue(alojamiento.getCiudad());
        txtDescripcion.setText(alojamiento.getDescripcion());
        txtPrecioNoche.setText(String.valueOf(alojamiento.getPrecioNoche()));
        txtCapacidadMaxima.setText(String.valueOf(alojamiento.getCapacidadMaxima()));
        chkTieneCocina.setSelected(alojamiento.isTieneCocina());
        chkTieneJardin.setSelected(alojamiento.isTieneJardin());}
    }
    @Override
    public boolean camposVacios() {
        return txtNombre.getText().isEmpty() ||
                comboCiudad.getValue() == null ||
                txtDescripcion.getText().isEmpty() ||
                txtPrecioNoche.getText().isEmpty() ||
                txtCapacidadMaxima.getText().isEmpty();
    }
    @Override
    public void validarCampos() throws IllegalArgumentException {
        if (camposVacios()) {
            throw new IllegalArgumentException("Por favor, completa todos los campos obligatorios.");
        }
    }
    private void limpiarCampos() {
        txtNombre.clear();
        comboCiudad.setValue(null);
        txtDescripcion.clear();
        txtPrecioNoche.clear();
        txtCapacidadMaxima.clear();
        chkTieneCocina.setSelected(false);
        chkTieneJardin.setSelected(false);
        indiceImagen.set(0);
        imageView.setImage(null);
    }
    @FXML
    public void onGuardarCambios() {
        if (alojamientoSeleccionado != null && alojamientoSeleccionado instanceof Casa casaSeleccionada) {
            try {
                validarCampos();
                if(!camposVacios()){
                casaSeleccionada.setNombre(txtNombre.getText());
                casaSeleccionada.setCiudad(comboCiudad.getValue());
                casaSeleccionada.setDescripcion(txtDescripcion.getText());
                casaSeleccionada.setPrecioNoche(Double.parseDouble(txtPrecioNoche.getText()));
                casaSeleccionada.setCapacidadMaxima(Integer.parseInt(txtCapacidadMaxima.getText()));
                casaSeleccionada.setTieneCocina(chkTieneCocina.isSelected());
                casaSeleccionada.setTieneJardin(chkTieneJardin.isSelected());
                controladorPrincipal.mostrarAlerta("Los datos de la casa se han actualizado correctamente.", Alert.AlertType.INFORMATION);
                notifyObserver();


            }
    }catch (IllegalArgumentException e) {
            controladorPrincipal.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);}
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        actualizarAlojamientosSeccionado();
        indiceImagen.setValue(0);
        cargarImagenes(alojamientoSeleccionado, imageView, indiceImagen);
        updateImagenCargada(indiceImagen, imageView, alojamientoSeleccionado);
     setItemsCBAlojamiento(comboCiudad);
     setDatosAlojamientoSeleccionado((Casa) alojamientoSeleccionado);
    }
}

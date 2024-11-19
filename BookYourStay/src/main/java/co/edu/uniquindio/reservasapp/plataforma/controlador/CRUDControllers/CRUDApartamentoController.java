package co.edu.uniquindio.reservasapp.plataforma.controlador.CRUDControllers;

import co.edu.uniquindio.reservasapp.ObserverPatern.Observable;
import co.edu.uniquindio.reservasapp.ObserverPatern.Observer;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto.Apartamento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.controlador.AdministradorController;
import co.edu.uniquindio.reservasapp.plataforma.controlador.ControladorPrincipal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class CRUDApartamentoController extends CRUDController implements Observable, Initializable {

    private Set<Observer> observerSet = new HashSet<>();

    @Override
    public void addObserver(Observer observer) {
        observerSet.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observerSet.remove(observer);
    }

    @Override
    public void notifyObserver() {
        for (Observer observer : observerSet) {
            observer.update();
        }
    }

    public static Alojamiento alojamientoSeleccionado = AdministradorController.getAlojamientoSeleccionado();
    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private IntegerProperty indiceImagen = new SimpleIntegerProperty();

    @FXML private TextField txtNombre;
    @FXML private ComboBox<Ciudad> comboCiudad;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtImagenes;
    @FXML private Button btnAgregarImagen;
    @FXML private Button btnBorrarImagen;
    @FXML private TextField txtPrecioNoche;
    @FXML private TextField txtCapacidadMaxima;
    @FXML private TextField txtHabitacionNumero;
    @FXML private TextField txtPrecioHabitacion;
    @FXML private CheckBox chkTieneCocina;
    @FXML private CheckBox chkTieneBalcon;
    @FXML private ImageView imageView;
    @FXML private Button btnAnteriorImagen;
    @FXML private Button btnSiguienteImagen;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnServicios;

    @FXML
    private void onMirarServicios(Event event) {
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

    public void actualizarAlojamientosSeccionado() {
        if (alojamientoSeleccionado != null) {
            AdministradorController.getAlojamientoSeleccionadoProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue instanceof Apartamento) {
                    alojamientoSeleccionado = newValue;
                    setDatosAlojamientoSeleccionado((Apartamento) newValue);
                    cargarImagenes(alojamientoSeleccionado, imageView, indiceImagen);
                }
            });
        }
    }

    public void setDatosAlojamientoSeleccionado(Apartamento alojamiento) {
        if (alojamientoSeleccionado != null && alojamientoSeleccionado instanceof Apartamento) {
            txtNombre.setText(alojamiento.getNombre());
            comboCiudad.setValue(alojamiento.getCiudad());
            txtDescripcion.setText(alojamiento.getDescripcion());
            txtPrecioNoche.setText(String.valueOf(alojamiento.getPrecioNoche()));
            txtCapacidadMaxima.setText(String.valueOf(alojamiento.getCapacidadMaxima()));
            chkTieneCocina.setSelected(alojamiento.isTieneCocina());
            chkTieneBalcon.setSelected(alojamiento.isTieneBalcon());
        }
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
        txtHabitacionNumero.clear();
        txtPrecioHabitacion.clear();
        chkTieneCocina.setSelected(false);
        chkTieneBalcon.setSelected(false);
        indiceImagen.set(0);
        imageView.setImage(null);
    }

    @FXML
    public void onGuardarCambios() {
        if (alojamientoSeleccionado != null && alojamientoSeleccionado instanceof Apartamento apartamentoSeleccionado) {
            try {
                validarCampos();
                if (!camposVacios()) {
                    apartamentoSeleccionado.setNombre(txtNombre.getText());
                    apartamentoSeleccionado.setCiudad(comboCiudad.getValue());
                    apartamentoSeleccionado.setDescripcion(txtDescripcion.getText());
                    apartamentoSeleccionado.setPrecioNoche(Double.parseDouble(txtPrecioNoche.getText()));
                    apartamentoSeleccionado.setCapacidadMaxima(Integer.parseInt(txtCapacidadMaxima.getText()));
                    apartamentoSeleccionado.setTieneCocina(chkTieneCocina.isSelected());
                    apartamentoSeleccionado.setTieneBalcon(chkTieneBalcon.isSelected());

                    controladorPrincipal.mostrarAlerta("Los datos del apartamento se han actualizado correctamente.", Alert.AlertType.INFORMATION);
                    notifyObserver();
                }
            } catch (IllegalArgumentException e) {
                controladorPrincipal.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualizarAlojamientosSeccionado();
        indiceImagen.setValue(0);
        cargarImagenes(alojamientoSeleccionado, imageView, indiceImagen);
        updateImagenCargada(indiceImagen, imageView, alojamientoSeleccionado);
        setItemsCBAlojamiento(comboCiudad);
        setDatosAlojamientoSeleccionado((Apartamento) alojamientoSeleccionado);
    }
}
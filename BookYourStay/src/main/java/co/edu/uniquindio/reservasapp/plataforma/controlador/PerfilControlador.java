package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class PerfilControlador implements Initializable {

    @FXML private MenuItem btnEditarDatos;
    @FXML private MenuItem btnCerrarsesion;
    @FXML private Label lblCargo;
    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, String> idColumn;
    @FXML private TableColumn<Reserva, String> horaColumn;
    @FXML private TableColumn<Reserva, String> fechaColumn;
    @FXML private TableColumn<Reserva, String> instalacionColumn;
    @FXML private Label lblName;


    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
    ObservableList <Reserva> reservas;
    FilteredList <Reserva> reservasUsuarioActual;

    private Persona usarioActual;


    @FXML
    void onCancelarReserva(ActionEvent event) {
            for (Reserva reserva : appReservasPrincipal.getListaReservas()){
                if (tablaReservas.getSelectionModel().getSelectedItem() == reserva){
                    reservas.remove(reserva);
                    tablaReservas.setItems(reservasUsuarioActual);
                    System.out.println(reservasUsuarioActual.size()+ appReservasPrincipal.getListaReservas().size());
                }
            }
        }

        @FXML
        void onCerrarSesion(ActionEvent event) {
            try {
                controladorPrincipal.navegarVentana("/inicio.fxml", "Inicio");
                controladorPrincipal.cerrarVentana(lblName);
            } catch (Exception e){e.printStackTrace();
            }
        }

        @FXML
        void onCrearReserva(ActionEvent event) {
            try {
                controladorPrincipal.navegarVentana("/buscadorAlojamientos.fxml", "Buscar alojamiento");

//                controladorPrincipal.navegarVentana("/reservacion.fxml", "Crear reserva");
            } catch (Exception e) {e.printStackTrace();}
        }

        @FXML
        void onEditarDatos(ActionEvent event) {
            try {
                controladorPrincipal.navegarVentana("/profileEditarDatos.fxml", "Editar mis datos");
            } catch (Exception e) {e.printStackTrace();}
        }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usarioActual = Sesion.getInstancia().getPersona();
        lblName.setText(usarioActual.getNombre());
        configurarColumnasTabla();
        actualizarTabla();

//        @Override
//        public void initialize(URL location, ResourceBundle resources){

//            usarioActual = Sesion.getInstancia().getPersona();
//            lblName.setText(usarioActual.getNombre());
//
//            reservas = FXCollections.observableArrayList(appReservasPrincipal.getListaReservas());
//            reservasUsuarioActual = new FilteredList<Reserva>(reservas, p -> p.getCedulaReservante().equals(usarioActual.getCedula()));
//            tablaReservas.setItems(actualizarTabla());
//            idColumn.setCellValueFactory(new PropertyValueFactory<>("idInstalacion"));
//            horaColumn.setCellValueFactory(new PropertyValueFactory<>("horaReserva"));
//            fechaColumn.setCellValueFactory(new PropertyValueFactory<>("diaReserva"));
//            instalacionColumn.setCellValueFactory(celldata -> {
//            return new SimpleStringProperty(celldata.getValue().getNombreHospedaje());});
//            configurarColumnasTabla();
//            actualizarTabla();
    }

    private void configurarColumnasTabla() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        horaColumn.setCellValueFactory(new PropertyValueFactory<>("horaInicioReserva"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("diaInicioReserva"));
        instalacionColumn.setCellValueFactory(new PropertyValueFactory<>("nombreHospedaje"));
    }

    private void actualizarTabla() {
        reservas = FXCollections.observableArrayList(appReservasPrincipal.getListaReservas());
        reservasUsuarioActual = new FilteredList<>(reservas, p -> p.getCedulaReservante().equals(usarioActual.getCedula()));
        tablaReservas.setItems(reservasUsuarioActual);
        tablaReservas.refresh();
    }

    /*private ObservableList actualizarTabla() {
        reservas = FXCollections.observableArrayList(appReservasPrincipal.getListaReservas());
        reservasUsuarioActual = new FilteredList<Reserva>(reservas, p -> p.getCedulaReservante().equals(usarioActual.getCedula()));
        tablaReservas.setItems(reservasUsuarioActual);
        return reservasUsuarioActual;
    }*/
}




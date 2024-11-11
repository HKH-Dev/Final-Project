package co.edu.uniquindio.reservasapp.plataforma.controlador;
import javafx.event.ActionEvent;


public class InicioControlador{

    private final co.edu.uniquindio.reservasapp.plataforma.controlador.ControladorPrincipal controladorPrincipal;

    public InicioControlador() {
        this.controladorPrincipal = co.edu.uniquindio.reservasapp.plataforma.controlador.ControladorPrincipal.getInstancia();
    }

    public void irIniciarSesion(ActionEvent actionEvent) {
        controladorPrincipal.navegarVentana("/login.fxml","Iniciar Sesión");
    }

    public void irRegistroCliente(ActionEvent actionEvent) {
        controladorPrincipal.navegarVentana("/registro.fxml", "Registro Persona");
    }

}



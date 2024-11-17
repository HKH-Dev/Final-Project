package co.edu.uniquindio.reservasapp.plataforma.controlador;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;

import co.edu.uniquindio.reservasapp.Utils.EnvioEmail;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class VentanaConfirmarCambiosController implements Initializable {

    @FXML
    private Label labelMensaje;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Button btnConfirmar;

    public ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    public EnvioEmail envioEmail;
    public Sesion sesionActual = Sesion.getInstancia();

    private String codigoVerificacion;
    private UserEditarPerfilController userEditarPerfilController = UserEditarPerfilController.getInstancia();
    @FXML
    public void CrearCodigoVerificacion() {
        codigoVerificacion = generarCodigoAleatorioSeguro();
        envioEmail.enviarNotificacion(sesionActual.getPersona().getEmail(), "Código de verificación", "Su codigo de verificación es: " + codigoVerificacion);
    }
    public String generarCodigoAleatorioSeguro() {
        SecureRandom secureRandom = new SecureRandom();
        int codigo = 100000 + secureRandom.nextInt(900000); // Genera un número entre 100000 y 99999
        return String.valueOf(codigo);
    }

    public void onVerificarCodigo(ActionEvent event){
        if (txtCodigo.getText().equals(codigoVerificacion)){
            sesionActual.getPersona().setEmail(userEditarPerfilController.usuarioDatosCambiados.getEmail());
            sesionActual.getPersona().setNombre(userEditarPerfilController.usuarioDatosCambiados.getNombre());
            sesionActual.getPersona().setContrasena(userEditarPerfilController.usuarioDatosCambiados.getContrasena());
            sesionActual.getPersona().setApellido(userEditarPerfilController.usuarioDatosCambiados.getApellido());
            controladorPrincipal.mostrarAlerta("Datos Actualizados con éxito", Alert.AlertType.INFORMATION);
            controladorPrincipal.cerrarVentana(txtCodigo);
        }
        else {controladorPrincipal.mostrarAlerta("Los codigos no coinciden", Alert.AlertType.ERROR);}
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        CrearCodigoVerificacion();
    }
    }

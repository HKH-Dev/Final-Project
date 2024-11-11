package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

public class LoginControlador {
    @FXML
    public TextField txtCorreo;
    @FXML
    public TextField txtPassword;

    private final ControladorPrincipal controladorPrincipal;
    Sesion sesion = Sesion.getInstancia();
    Persona persona = sesion.getPersona();


    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

    public LoginControlador() {
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    public boolean validarPassword(String contrasena, String hashed) {
        return BCrypt.checkpw(String.valueOf(contrasena), hashed);
    }


    public void login(ActionEvent actionEvent) {
        boolean loginSuccessful = false;
        String errorMsg = "Correo o contraseña incorrectos";

        for (Persona persona : appReservasPrincipal.getListaClientes()) {
            if (txtCorreo.getText().equals(persona.getEmail())) {
                if (validarPassword(txtPassword.getText(), persona.getContrasena())) {
                    sesion.setPersona(persona);
                    controladorPrincipal.cerrarVentana(txtCorreo);
                    loginSuccessful = true;
                    controladorPrincipal.navegarVentana("/profile.fxml", "Perfil");
                    break;
                } else {
                    errorMsg = "Contraseña incorrecta";
                }
            }
        }
        if (!loginSuccessful) {
            mostrarAlerta(errorMsg, Alert.AlertType.ERROR);
            limpiarFormularioRegistro();
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    private void limpiarFormularioRegistro() {
        txtCorreo.clear();
        txtPassword.clear();
    }
}
package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroControlador implements Initializable {
    AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    Sesion sesion = Sesion.getInstancia();
    Persona persona = sesion.getPersona();

    @FXML
    public TextField txtCedula;
    @FXML
    public TextField txtNombre;
    @FXML
    public TextField txtApellido;
    @FXML
    public TextField txtEmail;
//    @FXML
//    private ComboBox<TipoPersona> cbTipoPersona;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPass;
    @FXML
    private TextField txtPasswordShow;
    @FXML
    private TextField txtConfirmPassShow;
    @FXML
    private CheckBox checkShowPass;

    public void registrarPersona(ActionEvent actionEvent) {
        try {
            if (validarPassword()) {
                String cedula = txtCedula.getText();
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                String email = txtEmail.getText();
                String contrasena = BCrypt.hashpw(txtPassword.getText(), BCrypt.gensalt());
                String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());


//                TipoPersona tipoPersona = cbTipoPersona.getValue();
                appReservasPrincipal.registrarPersona(cedula, nombre, apellido, email, contrasena);

                for (Persona newUser : appReservasPrincipal.getListaClientes()) {
                    if (newUser.getCedula().equals(cedula)) {
                        persona = newUser;
                        break;
                    }
                }

                sesion.setPersona(persona);
                limpiarFormularioRegistro();
                mostrarAlerta("Persona registrada correctamente", Alert.AlertType.INFORMATION);


                controladorPrincipal.cerrarVentana(txtCedula);
                controladorPrincipal.navegarVentana("/profile.fxml", "Perfil");
            }
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void OnMostrarPassword(){
        boolean mostrar = checkShowPass.isSelected();
        txtPassword.setVisible(!mostrar);
        txtConfirmPass.setVisible(!mostrar);
        txtPasswordShow.setVisible(mostrar);
        txtConfirmPassShow.setVisible(mostrar);
    }
    private void bindingPasswords(){
        txtPassword.textProperty().addListener((observable,oldValue, newValue) ->
                txtPasswordShow.setText(newValue));
        txtConfirmPass.textProperty().addListener((observable, oldValue, newValue) ->
                txtConfirmPassShow.setText(newValue));
        txtPasswordShow.textProperty().addListener((observable, oldValue, newValue)->
                txtPassword.setText(newValue));
        txtConfirmPassShow.textProperty().addListener((observabe, oldValue, newValue)->
                txtConfirmPass.setText(newValue));
    }
    private boolean validarPassword() throws Exception{
        if (!txtPassword.getText().equals(txtConfirmPass.getText())){
            throw new Exception("Las contraseñas con coinciden");
        }
        return txtPassword.getText().equals(txtConfirmPass.getText());

    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    private void limpiarFormularioRegistro() {
        txtConfirmPass.clear();
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtPassword.clear();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        cbTipoPersona.getItems().addAll(TipoPersona.values());
        bindingPasswords();
    }

}

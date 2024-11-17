package co.edu.uniquindio.reservasapp.plataforma.controlador;
import co.edu.uniquindio.reservasapp.Utils.EncriptadorPass;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Sesion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class UserEditarPerfilController implements Initializable {

        @FXML
        private TextField txtCedula;

        @FXML
        private TextField txtNombre;

        @FXML
        private TextField txtApellido;

        @FXML
        private TextField txtEmail;

        @FXML
        private PasswordField txtContrasena;


        @FXML
        private Button btnVolverInicio;

        @FXML
        private Button btnModificarPerfil;
        @FXML
        private CheckBox checkBoxEditPass;
        @FXML
        private Label lblNuevaContrasena;
        @FXML
        private PasswordField txtContrasenaNueva;
        @FXML
        private Label lblNuevaContrasenaConf;
        @FXML
        private PasswordField txtContrasenaNuevaConf;
        public ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
        private static UserEditarPerfilController instancia;

        public Sesion sesionActual = Sesion.getInstancia();

        public static Persona usuarioDatosCambiados = new Persona(null, null, null, null, null, null);

        public static synchronized UserEditarPerfilController getInstancia() {
        if (instancia == null) {
            instancia = new UserEditarPerfilController();
        }
        return instancia;
    }

        @FXML
        public void onVolverAlInicio (){
            controladorPrincipal.cerrarVentana(txtApellido);
        }
        @FXML
        public void onCambiarPass(){
            boolean mostrar = checkBoxEditPass.isSelected();
            lblNuevaContrasena.setVisible(mostrar);
            lblNuevaContrasenaConf.setVisible(mostrar);
            txtContrasenaNueva.setVisible(mostrar);
            txtContrasenaNuevaConf.setVisible(mostrar);
        }

        public void setDatosUsuarioCambios(){
            usuarioDatosCambiados.setNombre(txtNombre.getText());
            usuarioDatosCambiados.setApellido(txtApellido.getText());
            usuarioDatosCambiados.setEmail(txtEmail.getText());
            usuarioDatosCambiados.setContrasena(txtContrasenaNueva.getText());
        }
        @FXML
        public void onGuardarCambios (){
            try{verificarContrasenasIguales();
                verificarCamposVacios();
                verificarNuevasContrasenas();
            if (!verificarCamposVacios() && cambioCorreoOPass() && sonContrasenasIguales() && verificarNuevasContrasenas()){
                controladorPrincipal.mostrarAlerta("Por favor, haga el siguiente proceso de verficacion", Alert.AlertType.INFORMATION);
                controladorPrincipal.navegarVentana("/VentanaConfirmarCambios.fxml", "Confirmar Cambios");
                controladorPrincipal.cerrarVentana(txtApellido);
            }
            if (!verificarCamposVacios() && sonContrasenasIguales() && !checkBoxEditPass.isSelected()){
                sesionActual.getPersona().setApellido(txtApellido.getText());
                sesionActual.getPersona().setNombre(txtNombre.getText());
                controladorPrincipal.mostrarAlerta("Sus datos han sido actualizados", Alert.AlertType.INFORMATION);
                controladorPrincipal.cerrarVentana(txtApellido);
            }
            }catch (Exception e){
                controladorPrincipal.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
            }
            public boolean sonContrasenasIguales() {
                EncriptadorPass encriptadorPass = new EncriptadorPass();
                return txtContrasena.getText().equals(Sesion.getInstancia().getPersona().getContrasena())
                        || encriptadorPass.verificarContrasena(txtContrasena.getText(), Sesion.getInstancia().getPersona().getContrasena());
            }

            public void verificarContrasenasIguales() throws Exception {
                if (!sonContrasenasIguales()) {
                    throw new Exception("Verifique que las contraseñas sean iguales");
                }
            }


    private void setDatosTextField(){
            txtCedula.setEditable(false);
            txtContrasena.setEditable(true);
            txtCedula.setText(sesionActual.getPersona().getCedula());
            txtContrasena.setText("");
            txtApellido.setText(sesionActual.getPersona().getApellido());
            txtEmail.setText(sesionActual.getPersona().getEmail());
            txtNombre.setText(sesionActual.getPersona().getNombre());
        }


        private boolean cambioCorreoOPass(){
            return !txtEmail.getText().equals(sesionActual.getPersona().getEmail()) || checkBoxEditPass.isSelected();
        }


    private void agregarCambioListener() {
        sesionActual.setOnPersonaChange(() -> setDatosTextField());
    }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle){
            setDatosTextField();
            agregarCambioListener();
            txtNombre.setOnKeyReleased(e -> actualizarDatosUsuario());
            txtApellido.setOnKeyReleased(e -> actualizarDatosUsuario());
            txtEmail.setOnKeyReleased(e -> actualizarDatosUsuario());
            txtContrasenaNueva.setOnKeyReleased(e -> actualizarDatosUsuario());
            txtContrasenaNuevaConf.setOnKeyReleased(e -> actualizarDatosUsuario());
        }
    private void actualizarDatosUsuario() {
        // Actualiza los datos de usuario en tiempo real cada vez que cambien los campos
        usuarioDatosCambiados.setNombre(txtNombre.getText());
        usuarioDatosCambiados.setApellido(txtApellido.getText());
        usuarioDatosCambiados.setEmail(txtEmail.getText());

        // Si el campo de nueva contraseña tiene texto, actualiza la contraseña en el modelo
        if (!txtContrasenaNueva.getText().isEmpty() && txtContrasenaNueva.getText().equals(txtContrasenaNuevaConf.getText())) {
            usuarioDatosCambiados.setContrasena(txtContrasenaNueva.getText());
        }
    }
    public boolean verificarCamposVacios() throws Exception {
        EncriptadorPass encriptadorPass = new EncriptadorPass();

        if (txtNombre.getText().isEmpty()) {
            throw new Exception("El campo 'Nombre' está vacío");
        }

        if (txtApellido.getText().isEmpty()) {
            throw new Exception("El campo 'Apellido' está vacío");
        }

        if (txtEmail.getText().isEmpty()) {
            throw new Exception("El campo 'Email' está vacío");
        }

        if (!sonContrasenasIguales()) {
            throw new Exception("La contraseña actual debe ser correcta para actualizar datos");
        }

        if (checkBoxEditPass.isSelected()) {
            if (txtContrasenaNueva.getText().isEmpty()) {
                throw new Exception("El campo 'Nueva Contraseña' está vacío");
            }
            if (txtContrasenaNuevaConf.getText().isEmpty()) {
                throw new Exception("El campo 'Confirmación de Nueva Contraseña' está vacío");
            }
        }
        return false;
    }

    private boolean verificarNuevasContrasenas() throws Exception {
        if (!txtContrasenaNueva.getText().equals(txtContrasenaNuevaConf.getText())) {
            throw new Exception("Las contraseñas de confirmación no coinciden");
        }
        return true;
    }

}



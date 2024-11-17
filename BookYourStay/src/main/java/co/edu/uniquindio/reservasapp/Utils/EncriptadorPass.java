package co.edu.uniquindio.reservasapp.Utils;

import org.mindrot.jbcrypt.BCrypt;

public class EncriptadorPass {

    public static boolean verificarContrasena(String contrasenaIngresada, String contrasenaEncriptada) {
        return BCrypt.checkpw(contrasenaIngresada, contrasenaEncriptada);
    }

    public static String encriptarContrasena(String contrasena) {
        // Generamos el "sal" con bcrypt
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(contrasena, salt);
    }
}

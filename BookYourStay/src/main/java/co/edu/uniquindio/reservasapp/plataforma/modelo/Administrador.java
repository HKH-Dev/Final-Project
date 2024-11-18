package co.edu.uniquindio.reservasapp.plataforma.modelo;

import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Billetera;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

@Getter
public class Administrador extends Persona {
    private String rol;


    private static final Administrador administrador = new Administrador(
            "1006775046",
            "Sebastian",
            "Admin",
            "bastianlesnk@gmail.com",
            BCrypt.hashpw("123", BCrypt.gensalt()),
            new Billetera(100000),
            "Administrador"
    );
    private Administrador(String cedula, String nombre, String apellido, String email, String contrasena, Billetera billetera, String rol) {
        super(cedula, nombre, apellido, email, contrasena, billetera);
        this.rol = rol;
    }

    public static Administrador getInstancia() {
        return administrador;
    }
}

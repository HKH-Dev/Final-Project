package co.edu.uniquindio.reservasapp.plataforma.modelo.cliente;

import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sesion {
    private Persona persona;
    private static Sesion INSTANCIA;

    private Sesion() {}

    public static Sesion getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new Sesion();
        }
        return INSTANCIA;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void cerrarSesion() {
        persona = null;
    }
}



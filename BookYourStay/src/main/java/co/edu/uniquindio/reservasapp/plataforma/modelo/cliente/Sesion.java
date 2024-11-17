package co.edu.uniquindio.reservasapp.plataforma.modelo.cliente;

import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Sesion {
    private static Sesion instancia;
    private Persona persona;
    private Runnable onPersonaChange;
    private Sesion() {}

    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    public void setOnPersonaChange(Runnable listener) {
        this.onPersonaChange = listener;
    }
}




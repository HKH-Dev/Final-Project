package co.edu.uniquindio.reservasapp.plataforma.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
//@AllArgsConstructor
public class Persona {
    protected String cedula;
    protected String nombre;
    protected String apellido;
    protected String email ;
    protected String contrasena;


    public Persona(String cedula, String nombre, String apellido, String email,  String hashedPassword) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = hashedPassword;
    }
}

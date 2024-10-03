package co.edu.uniquindio.plataforma.modelo;

import lombok.Data;

@Data
public class Persona {
    protected String cedula;
    protected String nombreCompleto;
    protected String email ;
    protected String contrasena;


    public Persona(String cedula, String nombreCompleto, String email, String contrasena) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.contrasena = contrasena;
    }


}

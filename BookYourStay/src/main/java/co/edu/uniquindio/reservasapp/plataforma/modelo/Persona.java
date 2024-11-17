package co.edu.uniquindio.reservasapp.plataforma.modelo;

import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Billetera;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class Persona {
    protected String cedula;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String contrasena;
    protected Billetera billetera; // Added Billetera field

    // Updated Constructor to include Billetera
    public Persona(String cedula, String nombre, String apellido, String email, String contrasena, Billetera billetera) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.billetera = billetera;
    }

    // Convenience methods to access saldo
    public double getSaldo() {
        return billetera.getSaldo();
    }

    public void setSaldo(double saldo) {
        billetera.setSaldo(saldo);
    }
}



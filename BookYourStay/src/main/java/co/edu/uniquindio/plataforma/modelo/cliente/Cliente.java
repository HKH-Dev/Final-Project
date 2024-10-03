package co.edu.uniquindio.plataforma.modelo.cliente;

import co.edu.uniquindio.plataforma.modelo.Persona;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString

public class Cliente extends Persona {
    private List<Cliente> listaClientes = new ArrayList<>();
    private String numeroTelefono;

    public Cliente(String cedula, String nombreCompleto, String email, String contrasena, String numeroTelefono) {
        super(cedula, nombreCompleto, email, contrasena);
        this.numeroTelefono = numeroTelefono;
    }

    public void registroCliente(String cedula, String nombreCompleto, String email, String contrasena, String numeroTelefono){
        if(cedula.isEmpty() || nombreCompleto.isEmpty() || email.isEmpty() || contrasena.isEmpty() || numeroTelefono.isEmpty()){
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        Persona persona = new Cliente(cedula, nombreCompleto, email, contrasena, numeroTelefono);
        listaClientes.add((Cliente) persona);
    }
    public void loginCliente(String email, String contrasena){
        if(email.isEmpty() || contrasena.isEmpty()){
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        for (Cliente cliente : listaClientes) {
            if (cliente.getEmail().equals(email) && cliente.getContrasena().equals(contrasena)) {
                System.out.println("Bienvenido " + cliente.getNombreCompleto());
            }
        }
    }

    public void actualizarCLientes(String email, String contrasena){
        if (email.isEmpty() || contrasena.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        for(Cliente cliente : listaClientes){
            if(cliente.getEmail().equals(email) && cliente.getContrasena().equals(contrasena)){
                cliente.setNombreCompleto(nombreCompleto);
                cliente.setNumeroTelefono(numeroTelefono);
                cliente.setContrasena(contrasena);
                System.out.println("Contraseña actualizada correctamente");
            }
        }
    }

    public void buscarAlojamiento(){}


}

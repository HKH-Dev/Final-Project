package co.edu.uniquindio.reservasapp.plataforma;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import co.edu.uniquindio.reservasapp.plataforma.servicio.ServiciosApp;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Getter
public class AppReservasPrincipal implements ServiciosApp {
    public static AppReservasPrincipal INSTANCIA;
    private List<Persona> listaClientes;
    private List<Alojamiento> listaAlojamientos;
    private List<Reserva> listaReservas;


    private AppReservasPrincipal() {
        this.listaClientes = new ArrayList<>();
        this.listaAlojamientos = new ArrayList<>();
        this.listaReservas = new ArrayList<>();

    }

    public static synchronized AppReservasPrincipal getInstance() {
        if (INSTANCIA == null) {
            INSTANCIA = new AppReservasPrincipal();
        }
        return INSTANCIA;
    }

    @Override
    public void registrarPersona(String cedula, String nombre, String Apellido, String email, String contrasena) throws Exception {
        // Perform validations...
        String ValidationMessage = "";
        if (cedula == null || cedula.isEmpty()) {
            ValidationMessage += "La cedula no puede ser nula o vacia\n";
        }
        if (nombre == null || nombre.isEmpty()) {
            ValidationMessage += "El nombre no puede ser nulo o vacio\n";
        }
        if (Apellido == null || Apellido.isEmpty()) {
            ValidationMessage += "El apellido no puede ser nulo o vacio\n";
        }
        if (email == null || email.isEmpty()) {
            ValidationMessage += "El correo no puede ser nulo o vacio\n";
        }
        if (contrasena == null || contrasena.isEmpty()) {
            ValidationMessage += "La contraseña no puede ser nula o vacia\n";
        }
        if (!ValidationMessage.isEmpty()) {
            throw new Exception(ValidationMessage);
        }
        if (listaClientes.stream().anyMatch(cliente -> cliente.getCedula().equals(cedula))) {
            throw new Exception("La cédula ya está registrada");
        }
        // Password hashing
        String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());

        Persona clienteNuevo = new Persona(cedula, nombre, Apellido, email, hashedPassword);
        listaClientes.add(clienteNuevo);
    }

//    @Override
//    public Persona login(String correo, String contrasena) throws Exception {
//        if (correo == null || correo.isEmpty() && contrasena == null || contrasena.isEmpty()) {
//            throw new Exception("El correo ni la contraseña pueden ser nulos o vacíos");
//        }
//        Persona usuarioRegistrado = listaClientes.stream()
//                .filter(persona -> persona.getEmail().equals(correo))
//                .findFirst()
//                .orElseThrow(() -> new Exception("El correo no está registrado"));
//
//        if (!BCrypt.checkpw(contrasena, usuarioRegistrado.getContrasena())) {
//            throw new Exception("La contraseña es incorrecta");
//        }
//        return usuarioRegistrado;
//    }

    @Override
    public Persona login(String correo, String contrasena) throws Exception {
        if (correo == null || correo.isEmpty()) {
            throw new Exception("El correo no puede ser nulo o vacío");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new Exception("La contraseña no puede ser nula o vacía");
        }
        Persona usuarioRegistrado = listaClientes.stream()
                .filter(persona -> persona.getEmail().equals(correo))
                .findFirst()
                .orElseThrow(() -> new Exception("El correo no está registrado"));

        if (!BCrypt.checkpw(contrasena, usuarioRegistrado.getContrasena())) {
            throw new Exception("La contraseña es incorrecta");
        }
        return usuarioRegistrado;
    }


    @Override
    public Reserva crearReserva(String tipoInstalacion, String nombreHospedajeReservar, String cedulaCliente, LocalDate diaReserva, String horaReserva) throws Exception {
        // Validate required parameters
        if (nombreHospedajeReservar == null || nombreHospedajeReservar.isEmpty() || cedulaCliente == null || cedulaCliente.isEmpty() || diaReserva == null || horaReserva == null || horaReserva.isEmpty()) {
            throw new Exception("Todos los campos son obligatorios.");
        }

        LocalDateTime fechaReserva = LocalDateTime.of(diaReserva, LocalTime.parse(horaReserva)); // Convert date and time into LocalDateTime

        Alojamiento instalaciones = listaAlojamientos.stream()
                .filter(instalacion -> instalacion.getNombre().equals(nombreHospedajeReservar))
                .findFirst()
                .orElseThrow(() -> new Exception("No hay ninguna instalación asociada al ID ingresado."));


        // Check if the installation is available
        if (!isAvailable(nombreHospedajeReservar, diaReserva, diaReserva, horaReserva, instalaciones.getCapacidadMaxima())) {
            throw new Exception("La instalación ya está reservada para la fecha y hora seleccionadas.");
        }

        Persona persona = obtenerPersona(cedulaCliente).orElseThrow(() -> new Exception("Persona no encontrada"));
        double costoReserva = calcularCosto(nombreHospedajeReservar, cedulaCliente);

        // Create and add the reservation to the list, now with the calculated cost
        Reserva reserva = Reserva.builder()
                .nombreHospedaje(nombreHospedajeReservar)
                .cedulaReservante(cedulaCliente)
                .diaInicioReserva(diaReserva)
                .diaFinReserva(diaReserva)
                .horaReserva(horaReserva)
                .costo(costoReserva)  // Ensure cost is included in the reservation
                .capacidadMaxima(instalaciones.getCapacidadMaxima())
                .build();
        listaReservas.add(reserva);

        return reserva;
    }

    private boolean isAvailable(String nombreHospedaje, LocalDate inicioReserva, LocalDate FinReserva,String horaReserva, int CapacidadMaxima){
        for (Reserva reserva : listaReservas) {
            if (reserva.getNombreHospedaje().equals(nombreHospedaje) &&
                    reserva.getDiaInicioReserva().equals(inicioReserva) &&
                    reserva.getDiaFinReserva().equals(FinReserva) &&
                    reserva.getHoraReserva().equals(horaReserva) &&
                    reserva.getCapacidadMaxima() == CapacidadMaxima) {
                return false; // Time slot already booked
            }
        }
        return true; // Time slot available

    }

    public double calcularCosto(String tipoAlojamiento, String cedulaCliente) {
        Alojamiento hospedaje = listaAlojamientos.stream()
                .filter(instalacion -> instalacion.getNombre().equals(tipoAlojamiento))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Instalación no encontrada"));

        // Base cost of the installation
        long diasReserva = ChronoUnit.DAYS.between(hospedaje.getFechaInicio() , hospedaje.getFechaFin());
        double costoTotal = hospedaje.getPrecioNoche()*diasReserva;

        return costoTotal;
    }



    @Override
    public Optional<Persona> obtenerPersona(String cedulaCliente) {
        return listaClientes.stream()
                .filter(persona -> persona.getCedula().equals(cedulaCliente))
                .findFirst();
    }

    @Override
    public List<Reserva> listarTodasReservas() {
        return new ArrayList<>(listaReservas); // Return a copy to avoid external modification
    }

}

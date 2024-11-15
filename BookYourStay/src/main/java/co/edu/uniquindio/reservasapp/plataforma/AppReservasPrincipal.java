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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class AppReservasPrincipal implements ServiciosApp {
    private static AppReservasPrincipal instance;
    private List<Persona> listaClientes;
    @Getter
    private List<Alojamiento> listaAlojamientos;
    private List<Reserva> listaReservas;

    private AppReservasPrincipal() {
        this.listaClientes = new ArrayList<>();
        this.listaAlojamientos = new ArrayList<>();
        this.listaReservas = new ArrayList<>();
    }

    public static AppReservasPrincipal getInstance() {
        if (instance == null) {
            instance = new AppReservasPrincipal();
        }
        return instance;
    }

    public void agregarAlojamiento(Alojamiento alojamiento) {
        listaAlojamientos.add(alojamiento);
    }


    @Override
    public void registrarPersona(String cedula, String nombre, String apellido, String email, String contrasena) throws Exception {
        // Validate fields
        String validationMessage = Stream.of(
                cedula == null || cedula.isEmpty() ? "La cedula no puede ser nula o vacía\n" : "",
                nombre == null || nombre.isEmpty() ? "El nombre no puede ser nulo o vacío\n" : "",
                apellido == null || apellido.isEmpty() ? "El apellido no puede ser nulo o vacío\n" : "",
                email == null || email.isEmpty() ? "El correo no puede ser nulo o vacío\n" : "",
                contrasena == null || contrasena.isEmpty() ? "La contraseña no puede ser nula o vacía\n" : ""
        ).reduce(String::concat).orElse("");

        if (!validationMessage.isEmpty()) {
            throw new Exception(validationMessage);
        }

        if (listaClientes.stream().anyMatch(cliente -> cliente.getCedula().equals(cedula))) {
            throw new Exception("La cédula ya está registrada");
        }

        // Password hashing
        String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        Persona clienteNuevo = new Persona(cedula, nombre, apellido, email, hashedPassword);
        listaClientes.add(clienteNuevo);
    }

    @Override
    public Persona login(String correo, String contrasena) throws Exception {
        if (correo == null || correo.isEmpty()) {
            throw new Exception("El correo no puede ser nulo o vacío");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new Exception("La contraseña no puede ser nula o vacía");
        }
        // Search for user by email
        /*Persona usuarioRegistrado = listaPersonas.stream()
                .filter(persona -> persona.getCorreoInstitucional().equals(correo))
                .findFirst()
                .orElse(null);

        if (usuarioRegistrado == null) {
            throw new Exception("El correo no está registrado");
        }

        // Verify password using BCrypt
        if (!BCrypt.checkpw(contrasena, usuarioRegistrado.getContrasena())) {
            throw new Exception("La contraseña es incorrecta");
        }*/
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
    public Reserva crearReserva(String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante,
                                LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva,
                                String horaFinReserva, double costo, int capacidadMaxima) throws Exception {
        // Validation for required fields
        if (Stream.of(ciudadAlojamiento, nombreHospedaje, cedulaReservante, horaInicioReserva, horaFinReserva)
                .anyMatch(field -> field == null || field.isEmpty()) || diaInicioReserva == null || diaFinReserva == null) {
            throw new Exception("Todos los campos son obligatorios.");
        }

        LocalDateTime fechaInicioReserva = LocalDateTime.of(diaInicioReserva, LocalTime.parse(horaInicioReserva));
        LocalDateTime fechaFinReserva = LocalDateTime.of(diaFinReserva, LocalTime.parse(horaFinReserva));

        Alojamiento hospedaje = listaAlojamientos.stream()
                .filter(instalacion -> instalacion.getNombre().equals(nombreHospedaje))
                .findFirst()
                .orElseThrow(() -> new Exception("No hay ninguna instalación asociada al Nombre ingresado."));

        if (!isAvailable(nombreHospedaje, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, capacidadMaxima)) {
            throw new Exception("La instalación ya está reservada para la fecha y hora seleccionadas.");
        }

        Persona persona = obtenerPersona(cedulaReservante).orElseThrow(() -> new Exception("Persona no encontrada"));
        double costoReserva = calcularCosto(nombreHospedaje, cedulaReservante);

        Reserva reservaNueva = Reserva.builder()
                .ciudadAlojamiento(ciudadAlojamiento)
                .nombreHospedaje(nombreHospedaje)
                .cedulaReservante(cedulaReservante)
                .diaInicioReserva(diaInicioReserva)
                .diaFinReserva(diaFinReserva)
                .horaInicioReserva(horaInicioReserva)
                .horaFinReserva(horaFinReserva)
                .costo(costoReserva)
                .capacidadMaxima(capacidadMaxima)
                .build();
        listaReservas.add(reservaNueva);
        return reservaNueva;
    }

    private boolean isAvailable(String nombreHospedaje, LocalDate inicioReserva, LocalDate finReserva,
                                String horaInicioReserva, String horaFinReserva, int capacidadMaxima) {
        return listaReservas.stream().noneMatch(reserva ->
                reserva.getNombreHospedaje().equals(nombreHospedaje) &&
                        reserva.getDiaInicioReserva().equals(inicioReserva) &&
                        reserva.getDiaFinReserva().equals(finReserva) &&
                        reserva.getHoraInicioReserva().equals(horaInicioReserva) &&
                        reserva.getHoraFinReserva().equals(horaFinReserva) &&
                        reserva.getCapacidadMaxima() == capacidadMaxima);
    }

    public double calcularCosto(String tipoAlojamiento, String cedulaCliente) {
        Alojamiento hospedaje = listaAlojamientos.stream()
                .filter(instalacion -> instalacion.getNombre().equals(tipoAlojamiento))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Instalación no encontrada"));

        long diasReserva = ChronoUnit.DAYS.between(hospedaje.getFechaInicio(), hospedaje.getFechaFin());
        return hospedaje.getPrecioNoche() * diasReserva;
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

    public List<Alojamiento> getAlojamientosPorCiudad(String ciudad) {
        return listaAlojamientos.stream()
                .filter(alojamiento -> alojamiento.getCiudad().equals(ciudad))
                .collect(Collectors.toList());
    }

}
/*
    @Override
    public  Reserva crearReserva(String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima)throws Exception{

        if (ciudadAlojamiento == null || ciudadAlojamiento.isEmpty() || nombreHospedaje == null || nombreHospedaje.isEmpty() || cedulaReservante == null || cedulaReservante.isEmpty() || diaInicioReserva == null || diaFinReserva == null || horaInicioReserva == null || horaInicioReserva.isEmpty() || horaFinReserva == null || horaFinReserva.isEmpty()) {
            throw new Exception("Todos los campos son obligatorios.");
        }
        LocalDateTime fechaInicioReserva = LocalDateTime.of(diaInicioReserva, LocalTime.parse(horaInicioReserva)); // Convert date and time into LocalDateTime
        LocalDateTime fechaFinReserva = LocalDateTime.of(diaFinReserva, LocalTime.parse(horaFinReserva)); // Convert date and time into LocalDateTime

        Alojamiento hospedaje = listaAlojamientos.stream()
                .filter(instalacion -> instalacion.getNombre().equals(nombreHospedaje))
                .findFirst()
                .orElseThrow(() -> new Exception("No hay ninguna instalación asociada al Nombre ingresado."));

        if(!isAvailable(nombreHospedaje, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, capacidadMaxima)){
            throw new Exception("La instalación ya está reservada para la fecha y hora seleccionadas.");
        }
        Persona persona = obtenerPersona(cedulaReservante).orElseThrow(() -> new Exception("Persona no encontrada"));
        double costoReserva = calcularCosto(nombreHospedaje, cedulaReservante);

        Reserva reservaNueva = Reserva.builder()
                .ciudadAlojamiento(ciudadAlojamiento)
                .nombreHospedaje(nombreHospedaje)
                .cedulaReservante(cedulaReservante)
                .diaInicioReserva(diaInicioReserva)
                .diaFinReserva(diaFinReserva)
                .horaInicioReserva(horaInicioReserva)
                .horaFinReserva(horaFinReserva)
                .costo(costoReserva)
                .capacidadMaxima(capacidadMaxima)
                .build();
        listaReservas.add(reservaNueva);
        return reservaNueva;
    }

    private boolean isAvailable(String nombreHospedaje, LocalDate inicioReserva, LocalDate FinReserva,String horaInicioReserva, String horaFinReserva, int CapacidadMaxima){
        for (Reserva reserva : listaReservas) {
            if (reserva.getNombreHospedaje().equals(nombreHospedaje) &&
                    reserva.getDiaInicioReserva().equals(inicioReserva) &&
                    reserva.getDiaFinReserva().equals(FinReserva) &&
                    reserva.getHoraInicioReserva().equals(horaInicioReserva) &&
                    reserva.getHoraFinReserva().equals(horaFinReserva) &&
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
    }*/

    /*
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
    }*/
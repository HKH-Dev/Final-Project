package co.edu.uniquindio.reservasapp.plataforma;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.Review;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Administrador;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Factura;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.QRCodeGenerator;
import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.Billetera;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import co.edu.uniquindio.reservasapp.plataforma.servicio.ServiciosApp;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AppReservasPrincipal implements ServiciosApp {
    private static AppReservasPrincipal instance;
    private List<Persona> listaClientes;
    private List<Alojamiento> listaAlojamientos;
    private List<Reserva> listaReservas;
    private Administrador administrador;

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

        // Initialize Billetera with a default balance, e.g., 1000.0
        Billetera billetera = new Billetera(1000.0);

        Persona clienteNuevo = new Persona(cedula, nombre, apellido, email, hashedPassword, billetera);
        listaClientes.add(clienteNuevo);
    }

    @Override
    public Reserva crearReserva(String idReserva, String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima) throws Exception {
        // Validation for required fields
        if (Stream.of(ciudadAlojamiento, nombreHospedaje, cedulaReservante, horaInicioReserva, horaFinReserva)
                .anyMatch(field -> field == null || field.isEmpty()) || diaInicioReserva == null || diaFinReserva == null) {
            throw new Exception("Todos los campos son obligatorios.");
        }

        // Check if alojamiento exists
        Alojamiento hospedaje = listaAlojamientos.stream()
                .filter(alojamiento -> alojamiento.getNombre().equals(nombreHospedaje))
                .findFirst()
                .orElseThrow(() -> new Exception("No hay ninguna instalación asociada al Nombre ingresado."));

        // Check availability
        if (!isAccommodationAvailable(hospedaje, diaInicioReserva, diaFinReserva)) {
            throw new Exception("La instalación ya está reservada para las fechas seleccionadas.");
        }

        Reserva reservaNueva = new Reserva(idReserva, ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, costo, capacidadMaxima);
        listaReservas.add(reservaNueva);

        // No need to generate the factura here, as it's handled in FacturaControlador
        return reservaNueva;
    }

    @Override
    public void generarFactura(Reserva reserva) throws Exception {
        // This method can be left empty or removed if not used
    }

    @Override
    public void generarCodigoQR() throws Exception {
        // Implement QR code generation if needed
    }

    public List<Reserva> getReservasForAlojamiento(Alojamiento alojamiento) {
        return listaReservas.stream()
                .filter(reserva -> reserva.getNombreHospedaje().equals(alojamiento.getNombre()))
                .collect(Collectors.toList());
    }

    public boolean isAccommodationAvailable(Alojamiento alojamiento, LocalDate startDate, LocalDate endDate) {
        List<Reserva> reservas = getReservasForAlojamiento(alojamiento);
        for (Reserva reserva : reservas) {
            if (!(endDate.isBefore(reserva.getDiaInicioReserva()) || startDate.isAfter(reserva.getDiaFinReserva()))) {
                // Dates overlap
                return false;
            }
        }
        return true;
    }

    public double calcularCosto(String nombreAlojamiento, LocalDate fechaInicio, LocalDate fechaFin, int numeroHuespedes) {
        Alojamiento alojamiento = listaAlojamientos.stream()
                .filter(a -> a.getNombre().equals(nombreAlojamiento))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Alojamiento no encontrado"));

        long diasReserva = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        if (diasReserva == 0) {
            diasReserva = 1; // At least one day
        }
        double precioPorNoche = alojamiento.getPrecioNoche();
        double subtotalDia = precioPorNoche * numeroHuespedes;
        return subtotalDia * diasReserva;
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
                .filter(alojamiento -> alojamiento.getCiudad().name().equalsIgnoreCase(ciudad))
                .collect(Collectors.toList());
    }

    // New methods for handling reviews

    // Add a review to an alojamiento
    public void agregarReviewAlojamiento(String nombreAlojamiento, Review review) throws Exception {
        Alojamiento alojamiento = listaAlojamientos.stream()
                .filter(a -> a.getNombre().equals(nombreAlojamiento))
                .findFirst()
                .orElseThrow(() -> new Exception("Alojamiento no encontrado"));

        alojamiento.addReview(review);
    }

    // Get reviews for an alojamiento
    public List<Review> obtenerReviewsAlojamiento(String nombreAlojamiento) throws Exception {
        Alojamiento alojamiento = listaAlojamientos.stream()
                .filter(a -> a.getNombre().equals(nombreAlojamiento))
                .findFirst()
                .orElseThrow(() -> new Exception("Alojamiento no encontrado"));

        return alojamiento.getReviews();
    }
}

/*
@Getter
public class AppReservasPrincipal implements ServiciosApp {
    private static AppReservasPrincipal instance;
    private List<Persona> listaClientes;
    @Getter
    private List<Alojamiento> listaAlojamientos;
    private List<Reserva> listaReservas;
    private Administrador administrador;

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

        // Initialize Billetera with a default balance, e.g., 1000.0
        Billetera billetera = new Billetera(1000.0);

        Persona clienteNuevo = new Persona(cedula, nombre, apellido, email, hashedPassword, billetera);
        listaClientes.add(clienteNuevo);
    }

    @Override
    public Reserva crearReserva(String idReserva, String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima) throws Exception {
        // Validation for required fields
        if (Stream.of(ciudadAlojamiento, nombreHospedaje, cedulaReservante, horaInicioReserva, horaFinReserva)
                .anyMatch(field -> field == null || field.isEmpty()) || diaInicioReserva == null || diaFinReserva == null) {
            throw new Exception("Todos los campos son obligatorios.");
        }

        // Check if alojamiento exists
        Alojamiento hospedaje = listaAlojamientos.stream()
                .filter(alojamiento -> alojamiento.getNombre().equals(nombreHospedaje))
                .findFirst()
                .orElseThrow(() -> new Exception("No hay ninguna instalación asociada al Nombre ingresado."));

        // Check availability
        if (!isAccommodationAvailable(hospedaje, diaInicioReserva, diaFinReserva)) {
            throw new Exception("La instalación ya está reservada para las fechas seleccionadas.");
        }

        Reserva reservaNueva = new Reserva(idReserva, ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, costo, capacidadMaxima);
        listaReservas.add(reservaNueva);
        generarFactura(reservaNueva);


        return reservaNueva;

    }

    @Override
    public void generarFactura(Reserva reserva) throws Exception {
        // Generate a factura based on the reserva details
        Factura factura = Factura.builder()
                .codigo(UUID.randomUUID().toString())
                .fechaComienzo(reserva.getDiaInicioReserva().toString())
                .fechaTerminacion(reserva.getDiaFinReserva().toString())
                .subtotal(reserva.getCosto() / ChronoUnit.DAYS.between(reserva.getDiaInicioReserva(), reserva.getDiaFinReserva()))
                .total(reserva.getCosto())
                .build();

        // Generate QR code with the invoice code
        BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImage(factura.getCodigo());

        // Send email to the client with the QR code and reservation details
        Persona cliente = obtenerPersona(reserva.getCedulaReservante())
                .orElseThrow(() -> new Exception("Cliente no encontrado"));

//        EnvioEmail.sendEmailWithQRCode(cliente.getEmail(), factura, reserva, qrCodeImage);

        // Optionally, store or log the factura
        // ...
    }


    @Override
    public void generarCodigoQR() throws Exception {
        // Implement QR code generation if needed
    }

    public List<Reserva> getReservasForAlojamiento(Alojamiento alojamiento) {
        return listaReservas.stream()
                .filter(reserva -> reserva.getNombreHospedaje().equals(alojamiento.getNombre()))
                .collect(Collectors.toList());
    }

    public boolean isAccommodationAvailable(Alojamiento alojamiento, LocalDate startDate, LocalDate endDate) {
        List<Reserva> reservas = getReservasForAlojamiento(alojamiento);
        for (Reserva reserva : reservas) {
            if (!(endDate.isBefore(reserva.getDiaInicioReserva()) || startDate.isAfter(reserva.getDiaFinReserva()))) {
                // Dates overlap
                return false;
            }
        }
        return true;
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


//    @Override
//    public void generarFactura(Reserva reserva) throws Exception {
//        // Generate a factura based on the reserva details
//        Factura factura = Factura.builder()
//                .codigo(UUID.randomUUID().toString())
//                .fechaComienzo(reserva.getDiaInicioReserva().toString())
//                .fechaTerminacion(reserva.getDiaFinReserva().toString())
//                .subtotal(reserva.getCosto() / ChronoUnit.DAYS.between(reserva.getDiaInicioReserva(), reserva.getDiaFinReserva()))
//                .total(reserva.getCosto())
//                .build();
//
//        // Here you might want to store the factura or send it to the user
//        // For demonstration, we'll just print it
//        System.out.println("Factura Generada:");
//        System.out.println("Código: " + factura.getCodigo());
//        System.out.println("Fecha Inicio: " + factura.getFechaComienzo());
//        System.out.println("Fecha Fin: " + factura.getFechaTerminacion());
//        System.out.println("Subtotal: " + factura.getSubtotal());
//        System.out.println("Total: " + factura.getTotal());
    }
//    public void crearReserva(Reserva reserva) {
//        listaReservas.add(reserva);
//    }
}*/



/*@Getter
public class AppReservasPrincipal implements ServiciosApp {
    private static AppReservasPrincipal instance;
    private List<Persona> listaClientes;
    private List<Alojamiento> listaAlojamientos;
    private List<Reserva> listaReservas;

    private AppReservasPrincipal() {
        this.listaClientes = new ArrayList<>();
        this.listaAlojamientos = new ArrayList<>();
        this.listaReservas = new ArrayList<>();
    }

    public static AppReservasPrincipal getInstance() {
        if (instance == null) {instance = new AppReservasPrincipal();}
        return instance;
    }

    public void agregarAlojamiento(Alojamiento alojamiento) {
        listaAlojamientos.add(alojamiento);
    }
    @Override
    public Persona login(String correo, String contrasena) throws Exception {
        if (correo == null || correo.isEmpty()) {throw new Exception("El correo no puede ser nulo o vacío");}
        if (contrasena == null || contrasena.isEmpty()) {throw new Exception("La contraseña no puede ser nula o vacía");}
        Persona usuarioRegistrado = listaClientes.stream()
                .filter(persona -> persona.getEmail().equals(correo))
                .findFirst()
                .orElseThrow(() -> new Exception("El correo no está registrado"));
        if (!BCrypt.checkpw(contrasena, usuarioRegistrado.getContrasena())) {throw new Exception("La contraseña es incorrecta");}
        return usuarioRegistrado;
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

        if (!validationMessage.isEmpty()) {throw new Exception(validationMessage);}
        if (listaClientes.stream().anyMatch(cliente -> cliente.getCedula().equals(cedula))) {throw new Exception("La cédula ya está registrada");}
        // Password hashing
        String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        // Initialize Billetera with a default balance, e.g., 1000.0
        Billetera billetera = new Billetera(1000.0);
        Persona clienteNuevo = new Persona(cedula, nombre, apellido, email, hashedPassword, billetera);
        listaClientes.add(clienteNuevo);
    }

    @Override
    public Reserva crearReserva(String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima) throws Exception {
        // Validation for required fields
        if (Stream.of(ciudadAlojamiento, nombreHospedaje, cedulaReservante, horaInicioReserva, horaFinReserva)
                .anyMatch(field -> field == null || field.isEmpty()) || diaInicioReserva == null || diaFinReserva == null) {
            throw new Exception("Todos los campos son obligatorios.");
        }
        Reserva reservaNueva = new Reserva(ciudadAlojamiento, nombreHospedaje, cedulaReservante, diaInicioReserva, diaFinReserva, horaInicioReserva, horaFinReserva, costo, capacidadMaxima);
        listaReservas.add(reservaNueva);
        return reservaNueva;
    }
    public List<Reserva> getReservasForAlojamiento(Alojamiento alojamiento) {
        return listaReservas.stream()
                .filter(reserva -> reserva.getNombreHospedaje().equals(alojamiento.getNombre()))
                .collect(Collectors.toList());
    }

    public boolean isAccommodationAvailable(Alojamiento alojamiento, LocalDate startDate, LocalDate endDate) {
        List<Reserva> reservas = getReservasForAlojamiento(alojamiento);
        for (Reserva reserva : reservas) {
            if (!(endDate.isBefore(reserva.getDiaInicioReserva()) || startDate.isAfter(reserva.getDiaFinReserva()))) {
                // Dates overlap
                return false;
            }
        }
        return true;
    }

    @Override
    public void generarFactura(String codigo, String fechaIncioReserva, String fechaFinReserva, double subtotalDia, double total) throws Exception {
        Factura factura = Factura.builder()
        .codigo(String.valueOf(UUID.randomUUID()))
        .fechaComienzo(String.valueOf(LocalDateTime.now()))
        .fechaTerminacion(String.valueOf(LocalDateTime.now()))
        .subtotal(0)
        .total(0)
        .build();
    }

    @Override
    public void generarCodigoQR() throws Exception {

    }

    /*@Override
    public Reserva crearReserva(String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima) throws Exception {
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
    private boolean isAvailable(String nombreHospedaje, LocalDate inicioReserva, LocalDate finReserva, String horaInicioReserva, String horaFinReserva, int capacidadMaxima) {
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

    public void crearReserva(Reserva reserva) {
    }
}
*/
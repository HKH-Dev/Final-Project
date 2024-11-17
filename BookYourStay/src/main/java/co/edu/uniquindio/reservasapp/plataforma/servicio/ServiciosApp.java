package co.edu.uniquindio.reservasapp.plataforma.servicio;


import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ServiciosApp {
    Persona login(String correo, String contrasena) throws Exception;

    void registrarPersona(String cedula, String nombres, String apellidos, String email, String contrasena) throws Exception;

    List<Reserva> listarTodasReservas();

    Reserva crearReserva(String idReserva, String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima) throws Exception;

    void generarFactura(Reserva reserva) throws Exception;

//    void generarFactura(String codigo, String fechaIncioReserva, String fechaFinReserva, double subtotalDia, double total)throws Exception;


    void generarCodigoQR()throws Exception;


    Optional<Persona> obtenerPersona(String cedulaCliente);
//
//    List<Reserva> listarTodasReservas();
//
//    List<Reserva> listarReservasPorPersona();
//
//    List<Reserva> listarReservasPorPersona(String cedulaPersona);
}


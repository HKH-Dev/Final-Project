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


//    Persona login(String correo, String contrasena) throws Exception;

//    void registrarPersona(String cedula, String nombres, String apellidos,  String email, String contrasena) throws Exception;
//
//    void crearInstalacion(String id, String nombre, int aforo, float costo, LocalDateTime horaInicio, LocalDateTime horaFin);
//
    Reserva crearReserva(String tipoInstalacion, String idInstalacion, String cedula, LocalDate diaReserva, String horaReserva) throws Exception;

    Optional<Persona> obtenerPersona(String cedulaCliente);
//
//    List<Reserva> listarTodasReservas();
//
//    List<Reserva> listarReservasPorPersona();
//
//    List<Reserva> listarReservasPorPersona(String cedulaPersona);
}


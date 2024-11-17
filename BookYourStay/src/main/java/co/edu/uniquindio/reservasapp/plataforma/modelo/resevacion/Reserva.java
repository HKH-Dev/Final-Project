package co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Reserva {
    private String idReserva;
    private String ciudadAlojamiento;
    private String nombreHospedaje;
    private String cedulaReservante;
    private LocalDate diaInicioReserva;
    private LocalDate diaFinReserva;
    private String horaInicioReserva;
    private String horaFinReserva;
    private double costo;
    private int capacidadMaxima;


    public Reserva( String idReserva, String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima) {
        this.idReserva = UUID.randomUUID().toString();
        this.ciudadAlojamiento = ciudadAlojamiento;
        this.nombreHospedaje = nombreHospedaje;
        this.cedulaReservante = cedulaReservante;
        this.diaInicioReserva = diaInicioReserva;
        this.diaFinReserva = diaFinReserva;
        this.horaInicioReserva = horaInicioReserva;
        this.horaFinReserva = horaFinReserva;
        this.costo = costo;
        this.capacidadMaxima = capacidadMaxima;
    }

}

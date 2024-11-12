package co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class Reserva {
    private String ciudadAlojamiento;
    private String nombreHospedaje;
    private String cedulaReservante;
    private LocalDate diaInicioReserva;
    private LocalDate diaFinReserva;
    private String horaInicioReserva;
    private String horaFinReserva;
    private double costo;
    private int capacidadMaxima;

    public Reserva(String ciudadAlojamiento, String nombreHospedaje, String cedulaReservante, LocalDate diaInicioReserva, LocalDate diaFinReserva, String horaInicioReserva, String horaFinReserva, double costo, int capacidadMaxima) {
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

//    public String getNombreInstalacion(List<Instalaciones> listaInstalaciones) {
//        for (Instalaciones instalacion : listaInstalaciones) {
//            if (instalacion.getId().equals(this.idInstalacion)) {
//                return instalacion.getNombre();
//            }
//        }
//        return "Instalacion no encontrada";
//    }
}

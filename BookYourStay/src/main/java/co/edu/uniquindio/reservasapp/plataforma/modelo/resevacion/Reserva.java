package co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class Reserva {
    private String nombreHospedaje;
    private String cedulaReservante;
    private LocalDate diaInicioReserva;
    private LocalDate diaFinReserva;
    private String horaReserva;
    private double costo;
    private int capacidadMaxima;

//    public String getNombreInstalacion(List<Instalaciones> listaInstalaciones) {
//        for (Instalaciones instalacion : listaInstalaciones) {
//            if (instalacion.getId().equals(this.idInstalacion)) {
//                return instalacion.getNombre();
//            }
//        }
//        return "Instalacion no encontrada";
//    }
}

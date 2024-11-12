package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.servicio.ServicioAlojamiento;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public abstract class Alojamiento {
    protected  String nombre;
    protected Ciudad ciudad;
    protected  String descripcion;
    protected  String imagen;
    protected  double precioNoche;
    protected  int capacidadMaxima;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    protected double Tarifa;
    protected ServicioAlojamiento servicios;

    public Alojamiento(String nombre, Ciudad ciudad, String descripcion, String imagen, double precioNoche, int capacidadMaxima){
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioNoche = precioNoche;
        this.capacidadMaxima = capacidadMaxima;
    }

    public Alojamiento(String nombre, Ciudad ciudad, String descripcion, String imagen, double precioNoche, int capacidadMaxima, LocalDate fechaInicio, LocalDate fechaFin, LocalDateTime horaInicio, LocalDateTime horaFin, double tarifa, ServicioAlojamiento servicios) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioNoche = precioNoche;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        Tarifa = tarifa;
        this.servicios = servicios;
    }


    public void agregarServicios(ServicioAlojamiento servicios) {
        this.servicios = servicios;
    }
}


package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.hotel;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.AlojamientoBuilder;
import lombok.Setter;

import java.util.List;

@Setter
public class HotelBuilder extends AlojamientoBuilder<HotelBuilder> {
    private String habitacionNumero;
    private double precioHabitacion;
    private List<String> imagenes;

    public HotelBuilder imagenes(List<String> imagenes) {
        this.imagenes = imagenes;
        return this;
    }
    public HotelBuilder habitacionNumero(String habitacionNumero) {
        this.habitacionNumero = habitacionNumero;
        return this;
    }

    public HotelBuilder precioHabitacion(double precioHabitacion) {
        this.precioHabitacion = precioHabitacion;
        return this;
    }

    @Override
    public Hotel build() {
        return new Hotel(nombre, ciudad, descripcion, imagenes, precioNoche, capacidadMaxima, habitacionNumero, precioHabitacion);
    }

}


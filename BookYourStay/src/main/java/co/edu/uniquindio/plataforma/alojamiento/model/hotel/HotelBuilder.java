package co.edu.uniquindio.plataforma.alojamiento.model.hotel;

import co.edu.uniquindio.plataforma.alojamiento.model.AlojamientoBuilder;


public class HotelBuilder extends AlojamientoBuilder<HotelBuilder> {
    private String habitacionNumero;
    private double precioHabitacion;

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
        return new Hotel(nombre, ciudad, descripcion, imagen, precioNoche, capacidadMaxima, habitacionNumero, precioHabitacion);
    }
}


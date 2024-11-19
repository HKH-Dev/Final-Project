package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.hotel;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
//import co.edu.uniquindio.plataforma.alojamiento.model.servicio.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Hotel extends Alojamiento {
    private String habitacionNumero;
    private double precioHabitacion;

    public Hotel(String nombre, Ciudad ciudad, String descripcion, List<String> imagenes, double precioNoche, int capacidadMaxima, String habitacionNumero, double precioHabitacion) {
        super(nombre, ciudad, descripcion, imagenes, precioNoche, capacidadMaxima);
        this.habitacionNumero = habitacionNumero;
        this.precioHabitacion = precioHabitacion;
    }
    // to string model

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Hotel{");
        sb.append("\n\t nombre = '").append(nombre).append('\'');
        sb.append("\n\t ciudad = ").append(ciudad);
        sb.append("\n\t descripcion = '").append(descripcion).append('\'');
        sb.append("\n\t habitacionNumero = ").append(habitacionNumero);
        sb.append("\n\t imagen = '").append(imagenes).append('\'');
        sb.append("\n\t precioNoche = ").append(precioNoche);
        sb.append("\n\t capacidadMaxima = ").append(capacidadMaxima);
        sb.append("\n\t precioHabitacion = ").append(precioHabitacion);
        sb.append("\n\t servicios =").append(servicios);
        sb.append('}');
        sb.append("\n");
        return sb.toString();
    }
}

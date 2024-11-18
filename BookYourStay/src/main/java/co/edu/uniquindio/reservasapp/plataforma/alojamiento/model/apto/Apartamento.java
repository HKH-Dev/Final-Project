package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import lombok.ToString;


import java.util.List;

@ToString
public class Apartamento extends Alojamiento {
    private boolean tieneCocina;
    private boolean tieneBalcon;

    public Apartamento(String nombre, Ciudad ciudad, String descripcion, List<String> imagenes, double precioNoche, int capacidadMaxima, boolean tieneCocina, boolean tieneBalcon) {
        super(nombre, ciudad, descripcion, imagenes, precioNoche, capacidadMaxima);
        this.tieneCocina = tieneCocina;
        this.tieneBalcon = tieneBalcon;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Apartamento{");
        sb.append("\n\t nombre = '").append(nombre).append('\'');
        sb.append("\n\t ciudad = ").append(ciudad);
        sb.append("\n\t descripcion = '").append(descripcion).append('\'');
        sb.append("\n\t imagen = '").append(imagenes).append('\'');
        sb.append("\n\t tieneCocina = ").append(tieneCocina);
        sb.append("\n\t tieneBalcon = ").append(tieneBalcon);
        sb.append("\n\t precioNoche = ").append(precioNoche);
        sb.append("\n\t capacidadMaxima = ").append(capacidadMaxima);
        sb.append("\n\t\t servicios =").append(servicios);
        sb.append('}');
        sb.append("\n");
        return sb.toString();
    }
}

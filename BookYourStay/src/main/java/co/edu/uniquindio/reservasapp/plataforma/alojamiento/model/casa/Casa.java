package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
//
//@ToString
@Getter
@Setter
public class Casa extends Alojamiento implements CasaFactory{
    private boolean tieneCocina;
    private boolean tieneJardin;

    public Casa(String nombre, Ciudad ciudad, String descripcion, List<String> imagenes, double precioNoche, int capacidadMaxima, boolean tieneCocina, boolean tieneJardin) {
        super(nombre, ciudad, descripcion, imagenes, precioNoche, capacidadMaxima);
        this.tieneCocina = tieneCocina;
        this.tieneJardin = tieneJardin;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Casa{");
        sb.append("\n\t nombre = '").append(nombre).append('\'');
        sb.append("\n\t ciudad = ").append(ciudad);
        sb.append("\n\t descripcion = '").append(descripcion).append('\'');
        sb.append("\n\t tieneCocina = ").append(tieneCocina);
        sb.append("\n\t tieneJardin = ").append(tieneJardin);
        sb.append("\n\t imagen = '").append(imagenes).append('\'');
        sb.append("\n\t precioNoche = ").append(precioNoche);
        sb.append("\n\t capacidadMaxima = ").append(capacidadMaxima);
        sb.append("\n\t\t servicios =").append(servicios);
        sb.append('}');
        sb.append("\n");
        return sb.toString();
    }
}

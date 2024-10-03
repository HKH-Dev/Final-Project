package co.edu.uniquindio.plataforma.alojamiento.model.casa;

import co.edu.uniquindio.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.plataforma.alojamiento.model.AlojamientoBuilder;

public class CasaBuilder extends AlojamientoBuilder<CasaBuilder> {
    private boolean tieneJardin;
    private boolean tieneCocina;

    public CasaBuilder tieneJardin(boolean tieneJardin) {
        this.tieneJardin = tieneJardin;
        return this;
    }

    public CasaBuilder tieneCocina(boolean tieneCocina) {
        this.tieneCocina = tieneCocina;
        return this;
    }

    @Override
    public Casa build() {
        return new Casa(nombre, ciudad, descripcion, imagen, precioNoche, capacidadMaxima, tieneCocina, tieneJardin);
    }
}

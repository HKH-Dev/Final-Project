package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.AlojamientoBuilder;

import java.util.List;

public class CasaBuilder extends AlojamientoBuilder<CasaBuilder> {
    private boolean tieneJardin;
    private boolean tieneCocina;
    private List<String> imagenes;

    public CasaBuilder imagenes(List<String> imagenes) {
        this.imagenes = imagenes;
        return this;
    }
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
        return new Casa(nombre, ciudad, descripcion, imagenes, precioNoche, capacidadMaxima, tieneCocina, tieneJardin);
    }
}

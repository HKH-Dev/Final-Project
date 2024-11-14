package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.AlojamientoBuilder;
import java.util.List;

public class ApartamentoBuilder extends AlojamientoBuilder<ApartamentoBuilder> {
    private boolean tieneCocina;
    private boolean tieneBalcon;
    private List<String> imagenes;

    public ApartamentoBuilder imagenes(List<String> imagenes) {
        this.imagenes = imagenes;
        return this;
    }

    public  ApartamentoBuilder tieneCocina(boolean tieneCocina){
        this.tieneCocina = tieneCocina;
        return this;
    }

    public ApartamentoBuilder tieneBalcon(boolean tieneBalcon){
        this.tieneBalcon = tieneBalcon;
        return this;
    }

    @Override
    public Apartamento build(){
        return new Apartamento(nombre, ciudad, descripcion, imagenes, precioNoche, capacidadMaxima, tieneCocina, tieneBalcon);
    }

}

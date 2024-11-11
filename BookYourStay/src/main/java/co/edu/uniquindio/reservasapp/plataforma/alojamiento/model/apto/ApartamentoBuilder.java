package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.AlojamientoBuilder;

public class ApartamentoBuilder extends AlojamientoBuilder<ApartamentoBuilder> {
    private boolean tieneCocina;
    private boolean tieneBalcon;

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
        return new Apartamento(nombre, ciudad, descripcion, imagen, precioNoche, capacidadMaxima, tieneCocina, tieneBalcon);
    }

}

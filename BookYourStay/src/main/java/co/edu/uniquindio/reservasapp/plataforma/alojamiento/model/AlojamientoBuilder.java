package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;

import java.util.List;

//import lombok.ToString;
//
//@ToString
public abstract class AlojamientoBuilder<T extends AlojamientoBuilder<T>> {
    protected String nombre;
    protected Ciudad ciudad;
    protected String descripcion;
    protected List<String> imagenes;
//    protected String imagen;
    protected double precioNoche;
    protected int capacidadMaxima;

    public T nombre(String nombre) {
        this.nombre = nombre;
        return (T) this;
    }

    public T ciudad(Ciudad ciudad) {
        this.ciudad = ciudad;
        return (T) this;
    }

    public T descripcion(String descripcion) {
        this.descripcion = descripcion;
        return (T) this;
    }

//    public T imagen(String imagen) {
//        this.imagen = imagen;
//        return (T) this;
//    }
    public T imagenes(List<String> imagenes) {
        this.imagenes = imagenes;
        return (T) this;
    }


    public T precioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
        return (T) this;
    }

    public T capacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        return (T) this;
    }

    public abstract Alojamiento build();

}


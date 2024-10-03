package co.edu.uniquindio.plataforma.alojamiento.model;
import co.edu.uniquindio.plataforma.alojamiento.model.servicio.ServicioAlojamiento;


public abstract class Alojamiento {
    protected  String nombre;
    protected Ciudad ciudad;
    protected  String descripcion;
    protected  String imagen;
    protected  double precioNoche;
    protected  int capacidadMaxima;
    protected ServicioAlojamiento servicios;

    protected Alojamiento(String nombre, Ciudad ciudad, String descripcion, String imagen, double precioNoche, int capacidadMaxima) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioNoche = precioNoche;
        this.capacidadMaxima = capacidadMaxima;
    }
  public void agregarServicios(ServicioAlojamiento servicios) {
        this.servicios = servicios;
    }


}



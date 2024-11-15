package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.servicio.ServicioAlojamiento;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter

public abstract class Alojamiento {
    protected String nombre;
    protected Ciudad ciudad;
    protected String descripcion;
    protected List<String> imagenes;  // List to hold paths to multiple images
    protected double precioNoche;
    protected int capacidadMaxima;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    protected double Tarifa;
    protected ServicioAlojamiento servicios;
    private String testimage;

    public Alojamiento(String nombre, Ciudad ciudad, String descripcion, List<String> imagenes, double precioNoche, int capacidadMaxima) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
        this.precioNoche = precioNoche;
        this.capacidadMaxima = capacidadMaxima;
    }

    public Alojamiento(String nombre, Ciudad ciudad, String descripcion, List<String> imagenes, double precioNoche, int capacidadMaxima, LocalDate fechaInicio, LocalDate fechaFin, LocalDateTime horaInicio, LocalDateTime horaFin, double tarifa, ServicioAlojamiento servicios) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
        this.precioNoche = precioNoche;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        Tarifa = tarifa;
        this.servicios = servicios;
    }

    public void agregarServicios(ServicioAlojamiento servicios) {
        this.servicios = servicios;
    }

    public String getImagenURL() {
        return "src/main/resources/images" + this.testimage; // Assuming imagenFileName holds the name of the image file
    }

    public List<String> getGalleryImages() {
        List<String> imagePaths = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            imagePaths.add("/images/*" + i + ".jpg");
        }
        return imagePaths;
    }

    public boolean isAvailableForDates(LocalDate startDate, LocalDate endDate) {
        // For now, assume all accommodations are available
        return true;
    }

//    public boolean isAvailableForDates(LocalDate startDate, LocalDate endDate) {
//
//        if (this.fechaInicio == null || this.fechaFin == null) {
//            return true; // If no dates are set, assume it's available
//        }
//        return (startDate.isAfter(this.fechaFin) || endDate.isBefore(this.fechaInicio));
//    }


}



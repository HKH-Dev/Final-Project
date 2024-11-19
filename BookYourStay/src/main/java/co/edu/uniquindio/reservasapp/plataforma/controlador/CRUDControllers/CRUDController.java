package co.edu.uniquindio.reservasapp.plataforma.controlador.CRUDControllers;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.CiudadObservable;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor

public abstract class CRUDController {
    public void setImagenImageView(ImageView nodoImagen, List<String> listaImagenes, int index){
        if (listaImagenes != null && index >= 0 && index < listaImagenes.size()){
            String rutaImagen = "/" + listaImagenes.get(index);

            Image imagen = new Image(getClass().getResource(rutaImagen).toExternalForm());
            nodoImagen.setImage(imagen);
        }
    }
    public void cargarImagenes(Alojamiento alojamientoSeleccionado, ImageView imageView, IntegerProperty indiceImagen){
        if(alojamientoSeleccionado != null){
            setImagenImageView(imageView, alojamientoSeleccionado.getImagenes(), indiceImagen.get());}
    }
    public void updateImagenCargada(IntegerProperty indiceImagen, ImageView imageView, Alojamiento alojamiento){
        indiceImagen.addListener((observable, oldValue, newValue) -> {
            setImagenImageView(imageView, alojamiento.getImagenes(), newValue.intValue());
        });
    }
    public void setItemsCBAlojamiento(ComboBox comboCiudad) {
        ObservableList<Ciudad> listaCiudades = CiudadObservable.obtenerCiudades();
        comboCiudad.setItems(listaCiudades);
    }

    public abstract void validarCampos() throws IllegalArgumentException;
    public abstract boolean camposVacios();
}

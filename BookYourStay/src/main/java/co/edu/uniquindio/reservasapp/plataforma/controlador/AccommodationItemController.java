package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class AccommodationItemController {
    @FXML private ImageView imgAlojamiento;
    @FXML private Label lblNombreAlojamiento;
    @FXML private Label lblUbicacion;
    @FXML private Text txtDescripcion;

    public void setAlojamiento(Alojamiento alojamiento) {
        lblNombreAlojamiento.setText(alojamiento.getNombre());
        lblUbicacion.setText(alojamiento.getCiudad().name());
        txtDescripcion.setText(alojamiento.getDescripcion());

        if (!alojamiento.getGalleryImages().isEmpty()) {
            imgAlojamiento.setImage(new Image(getClass().getResource(alojamiento.getGalleryImages().get(0)).toExternalForm()));
        }
    }
}

package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AccommodationItemController {

    @FXML
    private ImageView imgAlojamiento;
    @FXML
    private Label lblNombreAlojamiento;
    @FXML
    private Label lblCiudad;

    public void setAlojamiento(Alojamiento alojamiento) {
        lblNombreAlojamiento.setText(alojamiento.getNombre());
        lblCiudad.setText(alojamiento.getCiudad().toString());
        imgAlojamiento.setImage(new Image(getClass().getResource(alojamiento.getImagenURL()).toExternalForm()));
    }
}

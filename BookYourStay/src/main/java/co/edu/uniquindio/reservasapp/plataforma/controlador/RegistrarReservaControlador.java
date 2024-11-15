package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrarReservaControlador implements Initializable {

    @FXML private Label lblNombre;
    @FXML private Label lblUbicacion;
    @FXML private TextArea txtDescripcion;
    @FXML private ImageView imgPrincipal;
    @FXML private TextField txtNombreHospedaje;
    @FXML private TextField txtCedulaReservante;
    @FXML private TextField txtCosto;
    @FXML private TextField txtNumeroHuespedes;
    @FXML private TableView<?> tablaAlojamientos;

    private List<String> imagenes;
    private int currentImageIndex = 0;
    private Alojamiento currentAlojamiento;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Any initialization logic can go here
    }

    /**
     * Sets the current accommodation and updates the UI.
     *
     * @param alojamiento The selected accommodation.
     */
    public void setAlojamiento(Alojamiento alojamiento) {
        this.currentAlojamiento = alojamiento;

        // Update UI with accommodation details
        lblNombre.setText(alojamiento.getNombre());
        lblUbicacion.setText(alojamiento.getCiudad().name());
        txtDescripcion.setText(alojamiento.getDescripcion());

        // Load primary image
        if (alojamiento.getImagenes() != null && !alojamiento.getImagenes().isEmpty()) {
            loadImage(alojamiento.getImagenes().get(0));
        }
    }

    /**
     * Loads an image into the primary image view.
     *
     * @param imagePath The path of the image to load.
     */
    private void loadImage(String imagePath) {
        try {
            String adjustedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            InputStream imageStream = getClass().getResourceAsStream(adjustedPath);
            if (imageStream != null) {
                imgPrincipal.setImage(new Image(imageStream));
            } else {
                System.out.println("Image not found: " + adjustedPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the previous image in the gallery.
     */
    @FXML
    private void showPreviousImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenes.size()) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    /**
     * Shows the next image in the gallery.
     */
    @FXML
    private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    /**
     * Handles item click to navigate to the detailed accommodation scene.
     */
    @FXML
    private void handleItemClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservacion.fxml"));
            Parent root = loader.load();

            // Pass the selected accommodation to the next controller
            RegistrarReservaControlador controller = loader.getController();
            controller.setAlojamiento(currentAlojamiento);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservar Alojamiento");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

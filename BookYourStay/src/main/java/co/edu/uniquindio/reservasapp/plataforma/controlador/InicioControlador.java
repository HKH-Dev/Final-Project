package co.edu.uniquindio.reservasapp.plataforma.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InicioControlador implements Initializable {

    @FXML
    private ImageView imgCarousel;
    private int currentImageIndex = 0; // To track the current image in the carousel
    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private final List<String> imagenesHome = List.of("images/HOME/HOME1.jpg", "images/HOME/HOME2.jpg", "images/HOME/HOME3.jpg", "images/HOME/HOME4.jpg", "images/HOME/HOME5.jpg", "images/HOME/HOME6.jpg", "images/HOME/HOME7.jpg", "images/HOME/HOME8.jpg", "images/HOME/HOME9.jpg", "images/HOME/HOME10.jpg","images/HOME/HOME11.jpg","images/HOME/HOME12jpgp");

    public InicioControlador() {
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    @FXML
    public void initialize() {
        // Load the first image at startup
        if (!imagenesHome.isEmpty()) {
            loadImage(imagenesHome.get(0));
        }
    }

    public void irIniciarSesion(ActionEvent actionEvent) {
        controladorPrincipal.navegarVentana("/login.fxml", "Iniciar Sesión");
        controladorPrincipal.cerrarVentana(imgCarousel);
    }

    public void irRegistroCliente(ActionEvent actionEvent) {
        controladorPrincipal.navegarVentana("/registro.fxml", "Registro Persona");
    }

    @FXML
    private void showNextImage() {
        if (!imagenesHome.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenesHome.size(); // Move to the next image
            loadImage(imagenesHome.get(currentImageIndex));
        }
    }

    @FXML
    private void showPreviousImage() {
        if (!imagenesHome.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenesHome.size()) % imagenesHome.size(); // Move to the previous image
            loadImage(imagenesHome.get(currentImageIndex));
        }
    }

    private void loadImage(String imagePath) {
        try {
            // Correct the resource path with a leading slash
            String adjustedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            System.out.println("Attempting to load image: " + adjustedPath); // Debugging path
            InputStream imageStream = getClass().getResourceAsStream(adjustedPath);
            if (imageStream != null) {
                imgCarousel.setImage(new Image(imageStream));
                System.out.println("Image loaded successfully: " + adjustedPath);
            } else {
                System.out.println("Image not found: " + adjustedPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load the first image at startup
        if (!imagenesHome.isEmpty()) {
            loadImage(imagenesHome.get(0));
        }
    }
}
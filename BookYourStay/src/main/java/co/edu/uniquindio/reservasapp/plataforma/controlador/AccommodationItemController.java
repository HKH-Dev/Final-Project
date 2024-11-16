package co.edu.uniquindio.reservasapp.plataforma.controlador;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Persona;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
@Getter

public class AccommodationItemController {
    @FXML private Label lblNombreAlojamiento;
    @FXML private Label lblUbicacion;
    @FXML private Text txtDescripcion;
    @FXML private ImageView imgCarousel;

    private List<String> imagenes;
    private int currentImageIndex = 0;

    ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private Alojamiento currentAlojamiento;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numeroHuespedes;
    private Persona usuarioActual;

    public void setAlojamiento(Alojamiento alojamiento, LocalDate startDate, LocalDate endDate, int numeroHuespedes, Persona usuarioActual) {
        this.currentAlojamiento = alojamiento;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numeroHuespedes = numeroHuespedes;
        this.usuarioActual = usuarioActual;

        lblNombreAlojamiento.setText(alojamiento.getNombre());
        lblUbicacion.setText(alojamiento.getCiudad().name());
        txtDescripcion.setText(alojamiento.getDescripcion());

        this.imagenes = alojamiento.getImagenes();
        currentImageIndex = 0;
        if (imagenes != null && !imagenes.isEmpty()) {
            loadImage(imagenes.get(currentImageIndex));
        }

        // Add click event for detailed view
        lblNombreAlojamiento.setOnMouseClicked(event -> handleItemClick());
        imgCarousel.setOnMouseClicked(event -> handleItemClick());
    }

    private void loadImage(String imagePath) {
        try {
            String adjustedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            InputStream imageStream = getClass().getResourceAsStream(adjustedPath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                imgCarousel.setImage(image);
            } else {
                System.out.println("Image not found: " + adjustedPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void handleItemClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservacion.fxml"));
            controladorPrincipal.cerrarVentana(lblNombreAlojamiento);
            Parent root = loader.load();

            // Pass the selected accommodation and other data to the next controller
            RegistrarReservaControlador controller = loader.getController();
            controller.setAlojamiento(currentAlojamiento, startDate, endDate, numeroHuespedes, usuarioActual);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservar Alojamiento");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void showPreviousImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenes.size()) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    @FXML private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }
}

/*
public class AccommodationItemController {
    @FXML private Label lblNombreAlojamiento;
    @FXML private Label lblUbicacion;
    @FXML private Text txtDescripcion;
    @FXML private ImageView imgCarousel;

    private static AccommodationItemController instance;
    private final ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private List<String> imagenes;
    private int currentImageIndex = 0;
//
//    public void setAlojamiento(Alojamiento alojamiento) {
//        lblNombreAlojamiento.setText(alojamiento.getNombre());
//        lblUbicacion.setText(alojamiento.getCiudad().name());
//        txtDescripcion.setText(alojamiento.getDescripcion());
//
//        this.imagenes = alojamiento.getImagenes();
//        currentImageIndex = 0;
//        if (imagenes != null && !imagenes.isEmpty()) {
//            loadImage(imagenes.get(currentImageIndex));
//        }
//    }

    private Alojamiento currentAlojamiento;

    public static AccommodationItemController getInstance() {
        if (instance == null) {
            instance = new AccommodationItemController();
        }
        return instance;
    }
    public void setAlojamiento(Alojamiento alojamiento) {
        this.currentAlojamiento = alojamiento;

        lblNombreAlojamiento.setText(alojamiento.getNombre());
        lblUbicacion.setText(alojamiento.getCiudad().name());
        txtDescripcion.setText(alojamiento.getDescripcion());

        this.imagenes = alojamiento.getImagenes();
        currentImageIndex = 0;
        if (imagenes != null && !imagenes.isEmpty()) {
            loadImage(imagenes.get(currentImageIndex));
        }

//        if (imagenes != null && !imagenes.isEmpty()) {
//            loadImage(imagenes.get(0));
//        }

        // Add click event for detailed view
        lblNombreAlojamiento.setOnMouseClicked(event -> handleItemClick());
        imgCarousel.setOnMouseClicked(event -> handleItemClick());
    }

    private void loadImage(String imagePath) {
        try {
            // Ensure the path starts with a slash
            String adjustedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            System.out.println("Attempting to load image: " + adjustedPath);
            InputStream imageStream = getClass().getResourceAsStream(adjustedPath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                imgCarousel.setImage(image);
            } else {
                System.out.println("Image not found: " + adjustedPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPreviousImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenes.size()) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    @FXML
    private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------------
/*public class AccommodationItemController {
    @FXML private Label lblNombreAlojamiento;
    @FXML private Label lblUbicacion;
    @FXML private Text txtDescripcion;
    @FXML private ImageView imgCarousel;
    // Other UI components...

    private List<String> imagenes;
    private int currentImageIndex = 0;

    public void setAlojamiento(Alojamiento alojamiento) {
        lblNombreAlojamiento.setText(alojamiento.getNombre());
        lblUbicacion.setText(alojamiento.getCiudad().name());
        txtDescripcion.setText(alojamiento.getDescripcion());
        this.imagenes = alojamiento.getImagenes();

        if (imagenes != null && !imagenes.isEmpty()) {
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    private void loadImage(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imgCarousel.setImage(image);
    }

    @FXML
    private void showPreviousImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + imagenes.size()) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }

    @FXML
    private void showNextImage() {
        if (imagenes != null && !imagenes.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagenes.size();
            loadImage(imagenes.get(currentImageIndex));
        }
    }
}*/

//        public void setAccommodationData(Alojamiento alojamiento) {
//        lblNombreAlojamiento.setText(alojamiento.getNombre());
//        lblUbicacion.setText(alojamiento.getCiudad().name());
//        txtDescripcion.setText(alojamiento.getDescripcion());
//        images = alojamiento.getGalleryImages();
//        showImage();
//    }

//    public void setAlojamiento(Alojamiento alojamiento) {
//        lblNombreAlojamiento.setText(alojamiento.getNombre());
//        lblUbicacion.setText(alojamiento.getCiudad().name());
//        txtDescripcion.setText(alojamiento.getDescripcion());
//
//        if (!alojamiento.getGalleryImages().isEmpty()) {
//            imgAlojamiento.setImage(new Image(getClass().getResource(alojamiento.getGalleryImages().get(0)).toExternalForm()));
//        }
//    }
//----------------------------------------------------------------------------------------------------------------------------
/*
public class AccommodationItemController {
    @FXML private Button btnPreviousImage;
    @FXML private Button btnNextImage;
    @FXML private ImageView imgAlojamiento;
    @FXML private Label lblNombreAlojamiento;
    @FXML private Label lblUbicacion;
    @FXML private Text txtDescripcion;
    @FXML private ImageView imgCarousel;
    @FXML private DatePicker datePickerStart;
    @FXML private DatePicker datePickerEnd;
    @FXML private VBox vboxContainer;
    @FXML private VBox root;

    private List<String> images;
    private int currentImageIndex = 0;

    @FXML
    private void initialize() {
        if (datePickerStart != null && datePickerEnd != null) {
            datePickerStart.valueProperty().addListener((obs, oldDate, newDate) -> highlightDateRange(datePickerStart.getValue(), datePickerEnd.getValue()));
            datePickerEnd.valueProperty().addListener((obs, oldDate, newDate) -> highlightDateRange(datePickerStart.getValue(), datePickerEnd.getValue()));
        }
    }

    @FXML
    private void showPreviousImage() {
        if (images != null && !images.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
            showImage();
        }
    }

    @FXML
    private void showNextImage() {
        if (images != null && !images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            showImage();
        }
    }

    private void showImage() {
        if (images != null && !images.isEmpty() && imgCarousel != null) {
            String imagePath = images.get(currentImageIndex);
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            imgCarousel.setImage(image);
        }
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        if (alojamiento != null) {
            lblNombreAlojamiento.setText(alojamiento.getNombre());
            lblUbicacion.setText(alojamiento.getCiudad().name());
            txtDescripcion.setText(alojamiento.getDescripcion());
            images = alojamiento.getGalleryImages();
            showImage(); // Display the first image
        }
    }

    private void highlightDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            System.out.println("Highlighting date range from " + startDate + " to " + endDate);
        }
    }

    @FXML
    private void onSearchClicked() {
        List<Alojamiento> accommodations = fetchAccommodationsBasedOnFilter();
        vboxContainer.getChildren().clear();

        if (accommodations != null && !accommodations.isEmpty()) {
            for (Alojamiento accommodation : accommodations) {
                AccommodationItemController itemController = new AccommodationItemController();
                itemController.setAlojamiento(accommodation);
                vboxContainer.getChildren().add(itemController.getRoot());
            }
        }
    }

    private List<Alojamiento> fetchAccommodationsBasedOnFilter() {
        return List.of(); // Return a filtered list of accommodations
    }

    public VBox getRoot() {
        return root;
    }
}
*/


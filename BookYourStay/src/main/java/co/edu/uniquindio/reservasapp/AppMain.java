package co.edu.uniquindio.reservasapp;

import co.edu.uniquindio.reservasapp.plataforma.AppReservasPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.AlojamientoPrincipal;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.AlojamientoFactory;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto.Apartamento;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto.ApartamentoBuilder;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa.Casa;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa.CasaBuilder;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.hotel.Hotel;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.hotel.HotelBuilder;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.servicio.ServicioAlojamiento;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        cargarHospedajes();
        cargarDatosAppReserva();
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("/inicio.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Reserva App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void cargarHospedajes() {
        AlojamientoFactory factory = new AlojamientoFactory();
        AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();

        // **Accommodations for San Andres**
        // Hotel
        List<String> marAzulImages = List.of(
                "images/SAI/Hotel/MarAzul/MarAzul1.jpeg",
                "images/SAI/Hotel/MarAzul/MarAzul2.jpeg",
                "images/SAI/Hotel/MarAzul/MarAzul3.jpeg",
                "images/SAI/Hotel/MarAzul/MarAzul4.jpeg"
        );

        Hotel hotelMarAzul = new HotelBuilder()
                .nombre("Hotel MarAzul")
                .ciudad(Ciudad.SAN_ANDRES)
                .descripcion("Resort de lujo con vista al mar")
                .imagenes(marAzulImages)
                .precioNoche(600000)
                .capacidadMaxima(150)
                .habitacionNumero("H-1001")
                .precioHabitacion(350000)
                .build();

        ServicioAlojamiento serviciosMarAzul = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .aireAcondicionado(true)
                .build();

        hotelMarAzul.agregarServicios(serviciosMarAzul);
        appReservasPrincipal.agregarAlojamiento(hotelMarAzul);

        List<String> casaIslenaImages = List.of(
                "images/SAI/Casa/CasaIslena/CasaIslena1.jpg",
                "images/SAI/Casa/CasaIslena/CasaIslena2.jpg",
                "images/SAI/Casa/CasaIslena/CasaIslena3.jpg",
                "images/SAI/Casa/CasaIslena/CasaIslena4.jpg"
        );

        Casa casaIslena = new CasaBuilder()
                .nombre("Casa Isleña")
                .ciudad(Ciudad.SAN_ANDRES)
                .descripcion("Casa en la playa en San Andrés")
                .imagenes(casaIslenaImages)
                .precioNoche(500000)
                .capacidadMaxima(8)
                .tieneCocina(true)
                .tieneJardin(true)
                .build();

        ServicioAlojamiento serviciosCasaIslena = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();

        casaIslena.agregarServicios(serviciosCasaIslena);
        appReservasPrincipal.agregarAlojamiento(casaIslena);

        // Apartment
        List<String> apartamentoSAIImages = List.of(
                "images/SAI/Apartamento/AptoSAI1.jpg",
                "images/SAI/Apartamento/AptoSAI2.jpg",
                "images/SAI/Apartamento/AptoSAI3.jpg",
                "images/SAI/Apartamento/AptoSAI4.jpg"
        );
        Apartamento apartamentoSAI = new ApartamentoBuilder()
                .nombre("Apartamento San Andres")
                .ciudad(Ciudad.SAN_ANDRES)
                .descripcion("Apartamento con vista al mar")
                .imagenes(apartamentoSAIImages)
                .precioNoche(400000)
                .capacidadMaxima(5)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();

        ServicioAlojamiento serviciosAptoSAI = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();

        apartamentoSAI.agregarServicios(serviciosAptoSAI);
        appReservasPrincipal.agregarAlojamiento(apartamentoSAI);

        // **Accommodations for Medellin**
        // Repeat similar steps for Medellin and other cities, ensuring each accommodation has its own images.
    }

    private static void cargarDatosAppReserva() {
        try {
            AppReservasPrincipal AppReserva = AppReservasPrincipal.getInstance();
            AppReserva.registrarPersona("123", "Ana Maria", "Lopez Perez", "ana@email.com", "123");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

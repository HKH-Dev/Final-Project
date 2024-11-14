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

        DatePicker datePicker = new DatePicker();
        datePicker.setOnShowing(

                event -> {
            if (datePicker.getEditor() != null) {
                System.out.println("DatePicker is showing!");
            }
        });

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("/inicio.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Reserva App");
        stage.setScene(scene);
        stage.setResizable(false);
        //stage.setMaximized(true);
        stage.show();
    }
    public static List<Alojamiento> alojamientos = new ArrayList<>();

    public static void cargarHospedajes() {
        AlojamientoFactory factory = new AlojamientoFactory();
        AlojamientoPrincipal catalogo = new AlojamientoPrincipal();

        List<String> hotelImages = List.of(
                "@img/C:\\Users\\H\\Dev\\FinalProject\\BookYourStay\\src\\main\\resources\\images\\SAI\\Hotel\\MarAzul\\test1.jpeg",
                "./resources/images/SAI/Hotel/MarAzul/test2.jpg",
                "./resources/images/SAI/Hotel/MarAzul/test3.jpg",
                "./resources/images/SAI/Hotel/MarAzul/test4.jpg",
                "./resources/images/SAI/Hotel/MarAzul/test5.jpg"
        );
        System.out.println("Loading image: " + hotelImages.get(0));
        // Crear un Hotel usando la fábrica y el patrón Builder
        Hotel hotelMarAzul = (Hotel) factory.crearAlojamiento("Hotel");  // Crear una instancia de Hotel
        hotelMarAzul = new HotelBuilder()  // Usar el builder para personalizar los atributos del Hotel
                .nombre("Hotel MarAzul")
                .ciudad(Ciudad.SAN_ANDRES)
                .descripcion("Resort de lujo con vista al mar")
//                .imagen(List<String> hotelImages)
                .imagen("hotel.jpg")
                .precioNoche(600000)
                .capacidadMaxima(150)
                .habitacionNumero("H-1001")
                .precioHabitacion(350000)
                .build();
        //Agregar servicios al hotel
        ServicioAlojamiento serviciosHotelMarAzul = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .gimnasio(true)
                .aireAcondicionado(true)
                .build();
        alojamientos.add(hotelMarAzul);
        hotelMarAzul.agregarServicios(serviciosHotelMarAzul);
        catalogo.agregarAlojamiento(hotelMarAzul);
//        ---------------------------------------------------------
        Casa casaPaisa = (Casa) factory.crearAlojamiento("Casa");
        casaPaisa = new CasaBuilder()
                .nombre("Casa Paisa")
                .ciudad(Ciudad.MEDELLIN)
                .descripcion("Casa campestre en Medellín")
                .imagen("casa.jpg")
                .precioNoche(300000)
                .capacidadMaxima(10)
                .tieneCocina(true)
                .tieneJardin(true)
                .build();
        //Agregar servicios a la casa
        ServicioAlojamiento serviciosCasaPaisa = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .build();

        casaPaisa.agregarServicios(serviciosCasaPaisa);
        catalogo.agregarAlojamiento(casaPaisa);
//        ---------------------------------------------------------
        Apartamento apartamentoCali = (Apartamento) factory.crearAlojamiento("Apartamento");
        apartamentoCali = new ApartamentoBuilder()
                .nombre("Torre Primavera")
                .ciudad(Ciudad.CALI)
                .descripcion("Apartamento en el centro de Cali")
                .imagen("apartamento.jpg")
                .precioNoche(200000)
                .capacidadMaxima(4)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();
        ServicioAlojamiento serviciosApartamentoCali = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .gimnasio(true)
                .aireAcondicionado(true)
                .build();
        apartamentoCali.agregarServicios(serviciosApartamentoCali);
        catalogo.agregarAlojamiento(apartamentoCali);
//        ---------------------------------------------------------
        Casa casaSanAndres = (Casa) factory.crearAlojamiento("Casa");
        casaSanAndres = new CasaBuilder()
                .nombre("Casa Islena")
                .ciudad(Ciudad.SAN_ANDRES)
                .descripcion("Casa en la playa en San Andrés")
                .imagen("casa.jpg")
                .precioNoche(200000)
                .capacidadMaxima(8)
                .tieneCocina(true)
                .tieneJardin(true)
                .build();
        ServicioAlojamiento serviciosCasaSanAndres = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .aireAcondicionado(true)
                .build();
        casaSanAndres.agregarServicios(serviciosCasaSanAndres);
        catalogo.agregarAlojamiento(casaSanAndres);
        //        ---------------------------------------------------------
        Casa casaSantaMarta = (Casa) factory.crearAlojamiento("Casa");
        casaSantaMarta = new CasaBuilder()
                .nombre("Casa Playa Bonita")
                .descripcion("Casa cerca del mar")
                .ciudad(Ciudad.SANTA_MARTA)
                .imagen("imagenCasa")
                .precioNoche(500000)
                .capacidadMaxima(8)
                .tieneCocina(true)
                .tieneJardin(true)
                .build();
        ServicioAlojamiento servicioCasaPlayaBonita = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .desayuno(true)
                .aireAcondicionado(true)
                .build();
        casaSantaMarta.agregarServicios(servicioCasaPlayaBonita);
        catalogo.agregarAlojamiento(casaSantaMarta);
//        ---------------------------------------------------------
        Apartamento apartamentoPereira = (Apartamento) factory.crearAlojamiento("Apartamento");
        apartamentoPereira = new ApartamentoBuilder()
                .nombre("Torre Rincon Verde")
                .descripcion("Apartamento con vista a las montañas")
                .ciudad(Ciudad.SAN_ANDRES)
                .imagen("imagenApto")
                .precioNoche(400000)
                .capacidadMaxima(4)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();
        ServicioAlojamiento servicioAptoRiconVerde = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .gimnasio(true)
                .aireAcondicionado(true)
                .build();
        apartamentoPereira.agregarServicios(servicioAptoRiconVerde);
        catalogo.agregarAlojamiento(apartamentoPereira);
//        ---------------------------------------------------------
        Casa casaArmenia = (Casa) factory.crearAlojamiento("Casa");
        casaArmenia = new CasaBuilder()
                .nombre("Casa del Arriero").ciudad(Ciudad.ARMENIA).descripcion("Casa en Campestre en Armenia").imagen("imagen").precioNoche(100000).capacidadMaxima(10).tieneCocina(true).tieneJardin(true).build();

        ServicioAlojamiento serviciosCasaArriero = ServicioAlojamiento.builder()
                .wifi(true).piscina(true).parqueadero(true).tv(false).desayuno(true).gimnasio(false).aireAcondicionado(true).build();
        casaArmenia.agregarServicios(serviciosCasaArriero);
        catalogo.agregarAlojamiento(casaArmenia);
//        ---------------------------------------------------------

        // Mostrar todos los alojamientos
        System.out.println("Todos los alojamientos Casa: \n" + catalogo.obtenerAlojamientosPorTipo("Casa")+ "\n");
    }

    private static void cargarDatosAppReserva() {
        try {
            AppReservasPrincipal AppReserva = AppReservasPrincipal.getInstance();
            // Agregar personas por defecto
            AppReserva.registrarPersona("123", "Ana Maria", "Lopez Perez", "ana@email.com", "1234");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
    //    -------------------------------------------------
}
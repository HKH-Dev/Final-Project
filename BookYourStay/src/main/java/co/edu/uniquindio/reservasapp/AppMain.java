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
import co.edu.uniquindio.reservasapp.plataforma.controlador.InicioControlador;
import co.edu.uniquindio.reservasapp.plataforma.modelo.Administrador;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        cargarHospedajes();
        cargarUsuariosAppReserva();
        cargarBaseDatosReserva();
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("/inicio.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Reserva App");
        stage.setScene(scene);
        stage.setResizable(true);
//        stage.setMaximized(true);
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

        // House
        List<String> casaIslenaImages = List.of(
                "images/SAI/Casa/CasaIslena/CasaIselana1.png",
                "images/SAI/Casa/CasaIslena/CasaIselana2.png"
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
                "images/SAI/Apartamento/AptoCentro/AptoCentro1.png",
                "images/SAI/Apartamento/AptoCentro/AptoCentro2.png",
                "images/SAI/Apartamento/AptoCentro/AptoCentro3.png",
                "images/SAI/Apartamento/AptoCentro/AptoCentro4.png"
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

        List <String> ApartamentoIslaImages =  List.of(
                "images/SAI/Apartamento/AptoIsla/AptoIsla1.png",
                "images/SAI/Apartamento/AptoIsla/AptoIsla2.png",
                "images/SAI/Apartamento/AptoIsla/AptoIsla3.png",
                "images/SAI/Apartamento/AptoIsla/AptoIsla4.png"
        );
        Apartamento apartamentoIsla = new ApartamentoBuilder()
                .nombre("Apartamento Isla")
                .ciudad(Ciudad.SAN_ANDRES)
                .descripcion("Apartamento en el centro de San Andrés")
                .imagenes(ApartamentoIslaImages)
                .precioNoche(300000)
                .capacidadMaxima(4)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();
        ServicioAlojamiento serviciosaAptoIsla = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        apartamentoIsla.agregarServicios(serviciosaAptoIsla);
        appReservasPrincipal.agregarAlojamiento(apartamentoIsla);

        // **Accommodations for Medellin**
        // Hotel
        List<String> hotelMedellinImages = List.of(
                "images/MED/Hotel/HotelLaArepa/HotelLaArepa1.png",
                "images/MED/Hotel/HotelLaArepa/HotelLaArepa2.png",
                "images/MED/Hotel/HotelLaArepa/HotelLaArepa3.png",
                "images/MED/Hotel/HotelLaArepa/HotelLaArepa4.png"
        );
        Hotel HotelLaArepa = new HotelBuilder()
                .nombre("Hotel La Arepa")
                .ciudad(Ciudad.MEDELLIN)
                .descripcion("Hotel en el centro de Medellín")
                .imagenes(hotelMedellinImages)
                .precioNoche(300000)
                .capacidadMaxima(100)
                .habitacionNumero("H-2001")
                .precioHabitacion(200000)
                .build();
        ServicioAlojamiento serviciosHotelMedellin = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .aireAcondicionado(true)
                .build();
        HotelLaArepa.agregarServicios(serviciosHotelMedellin);
        appReservasPrincipal.agregarAlojamiento(HotelLaArepa);

        List<String> HotelPalmasImages = List.of(
                "images/MED/Hotel/HotelPalmas/HotelPalmas1.png",
                "images/MED/Hotel/HotelPalmas/HotelPalmas2.png",
                "images/MED/Hotel/HotelPalmas/HotelPalmas3.png",
                "images/MED/Hotel/HotelPalmas/HotelPalmas4.png"
        );
        Hotel HotelPalmas = new HotelBuilder()
                .nombre("Hotel Palmas")
                .ciudad(Ciudad.MEDELLIN)
                .descripcion("Hotel en el Poblado")
                .imagenes(HotelPalmasImages)
                .precioNoche(400000)
                .capacidadMaxima(150)
                .habitacionNumero("H-2002")
                .precioHabitacion(250000)
                .build();
        ServicioAlojamiento serviciosHotelPalmas = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .aireAcondicionado(true)
                .build();
        HotelPalmas.agregarServicios(serviciosHotelPalmas);
        appReservasPrincipal.agregarAlojamiento(HotelPalmas);

        // Apartment
        List<String> AptoLomaBernalImages = List.of(
                "images/MED/Apartamento/AptoLomaBernal/AptoLomaBernal1.png",
                "images/MED/Apartamento/AptoLomaBernal/AptoLomaBernal2.png",
                "images/MED/Apartamento/AptoLomaBernal/AptoLomaBernal3.png",
                "images/MED/Apartamento/AptoLomaBernal/AptoLomaBernal4.png"
        );
        Apartamento AptoLomaBernal = new ApartamentoBuilder()
                .nombre("Apartamento Medellin")
                .ciudad(Ciudad.MEDELLIN)
                .descripcion("Apartamento en el Poblado")
                .imagenes(AptoLomaBernalImages)
                .precioNoche(250000)
                .capacidadMaxima(4)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();
        ServicioAlojamiento serviciosAptoMedellin = ServicioAlojamiento.builder()
                .wifi(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        AptoLomaBernal.agregarServicios(serviciosAptoMedellin);
        appReservasPrincipal.agregarAlojamiento(AptoLomaBernal);

        List<String> AptoValleAburra = List.of(
                "images/MED/Apartamento/AptoValleAburra/AptoValleAburra1.png",
                "images/MED/Apartamento/AptoValleAburra/AptoValleAburra2.png",
                "images/MED/Apartamento/AptoValleAburra/AptoValleAburra3.png",
                "images/MED/Apartamento/AptoValleAburra/AptoValleAburra4.png"
        );
        Apartamento apartamentoValleAburra = new ApartamentoBuilder()
                .nombre("Apartamento Valle de Aburra")
                .ciudad(Ciudad.MEDELLIN)
                .descripcion("Apartamento en el centro de Medellín")
                .imagenes(AptoValleAburra)
                .precioNoche(200000)
                .capacidadMaxima(3)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();
        ServicioAlojamiento serviciosAptoValleAburra = ServicioAlojamiento.builder()
                .wifi(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        apartamentoValleAburra.agregarServicios(serviciosAptoValleAburra);
        appReservasPrincipal.agregarAlojamiento(apartamentoValleAburra);

        // House
        List<String> CasaAntoquiaImages = List.of(
                "images/MED/Casa/CasaAntoquia/CasaAntoquia1.png",
                "images/MED/Casa/CasaAntoquia/CasaAntoquia2.png",
                "images/MED/Casa/CasaAntoquia/CasaAntoquia3.png",
                "images/MED/Casa/CasaAntoquia/CasaAntoquia4.png"
        );
        Casa casaMedellin = new CasaBuilder()
                .nombre("Casa Medellin")
                .ciudad(Ciudad.MEDELLIN)
                .descripcion("Casa en Envigado")
                .imagenes(CasaAntoquiaImages)
                .precioNoche(350000)
                .capacidadMaxima(6)
                .tieneJardin(true)
                .build();
        ServicioAlojamiento serviciosCasaMedellin = ServicioAlojamiento.builder()
                .wifi(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        casaMedellin.agregarServicios(serviciosCasaMedellin);
        appReservasPrincipal.agregarAlojamiento(casaMedellin);

        List<String> CasaPaisaImages = List.of(
                "images/MED/Casa/CasaPaisa/CasaPaisa1.png",
                "images/MED/Casa/CasaPaisa/CasaPaisa2.png",
                "images/MED/Casa/CasaPaisa/CasaPaisa3.png",
                "images/MED/Casa/CasaPaisa/CasaPaisa4.png"
        );
        Casa casaPaisa = new CasaBuilder()
                .nombre("Casa Paisa")
                .ciudad(Ciudad.MEDELLIN)
                .descripcion("Casa en el Poblado")
                .imagenes(CasaPaisaImages)
                .precioNoche(400000)
                .capacidadMaxima(8)
                .tieneJardin(true)
                .build();
        ServicioAlojamiento serviciosCasaPaisa = ServicioAlojamiento.builder()
                .wifi(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        casaPaisa.agregarServicios(serviciosCasaPaisa);
        appReservasPrincipal.agregarAlojamiento(casaPaisa);

        // **Accommodations for Armenia**
        // Hotel

       //House
        List<String> CasaCafeteraImages = List.of(
                "images/AXM/Casa/CasaCafetera/CasaCafetera1.png",
                "images/AXM/Casa/CasaCafetera/CasaCafetera2.png",
                "images/AXM/Casa/CasaCafetera/CasaCafetera3.png",
                "images/AXM/Casa/CasaCafetera/CasaCafetera4.png"
        );
        Casa casaCafetera = new CasaBuilder()
                .nombre("Casa Cafetera")
                .ciudad(Ciudad.ARMENIA)
                .descripcion("Casa en el Quindio")
                .imagenes(CasaCafeteraImages)
                .precioNoche(300000)
                .capacidadMaxima(6)
                .tieneJardin(true)
                .build();
        ServicioAlojamiento serviciosCasaCafetera = ServicioAlojamiento.builder()
                .wifi(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        casaCafetera.agregarServicios(serviciosCasaCafetera);
        appReservasPrincipal.agregarAlojamiento(casaCafetera);



        // **Accommodations for Bogota**
        // Hotel
        //House
        List<String> CasaCedritosImages = List.of(
                "images/BOG/Casa/CasaCedritos/CasaCedritos1.png",
                "images/BOG/Casa/CasaCedritos/CasaCedritos2.png",
                "images/BOG/Casa/CasaCedritos/CasaCedritos3.png",
                "images/BOG/Casa/CasaCedritos/CasaCedritos4.png"
        );
        Casa casaCedritos = new CasaBuilder()
                .nombre("Casa Cedritos")
                .ciudad(Ciudad.BOGOTA)
                .descripcion("Casa en el norte de Bogotá")
                .imagenes(CasaCedritosImages)
                .precioNoche(400000)
                .capacidadMaxima(8)
                .tieneJardin(true)
                .build();
        ServicioAlojamiento serviciosCasaCedritos = ServicioAlojamiento.builder()
                .wifi(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        casaCedritos.agregarServicios(serviciosCasaCedritos);
        appReservasPrincipal.agregarAlojamiento(casaCedritos);

        // Apartment
        List<String> AptoRosalesImages = List.of(
                "images/BOG/Apartamento/AptoRosales/AptoRosales1.png",
                "images/BOG/Apartamento/AptoRosales/AptoRosales2.png",
                "images/BOG/Apartamento/AptoRosales/AptoRosales3.png",
                "images/BOG/Apartamento/AptoRosales/AptoRosales4.png"
        );
        Apartamento apartamentoRosales = new ApartamentoBuilder()
                .nombre("Apartamento Rosales")
                .ciudad(Ciudad.BOGOTA)
                .descripcion("Apartamento en el norte de Bogotá")
                .imagenes(AptoRosalesImages)
                .precioNoche(300000)
                .capacidadMaxima(4)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();
        ServicioAlojamiento serviciosAptoRosales = ServicioAlojamiento.builder()
                .wifi(true)
                .parqueadero(true)
                .tv(true)
                .aireAcondicionado(true)
                .build();
        apartamentoRosales.agregarServicios(serviciosAptoRosales);
        appReservasPrincipal.agregarAlojamiento(apartamentoRosales);

        }

    private static void cargarUsuariosAppReserva() {
        try {
            AppReservasPrincipal AppReserva = AppReservasPrincipal.getInstance();
            AppReserva.registrarPersona("123", "Ana Maria", "Lopez Perez", "ana@email.com", "123");
            AppReserva.registrarPersona("456", "Juan", "Perez", "juan@email", "456");
            AppReserva.registrarPersona("1006775047", "Sebas", "Lesmes", "gdg4l4x1156@gmail.com", "12345");
            AppReserva.getListaClientes().add(Administrador.getInstancia());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargarBaseDatosReserva() {
        AppReservasPrincipal appReservasPrincipal = AppReservasPrincipal.getInstance();
        try {
            appReservasPrincipal.crearReserva(UUID.randomUUID().toString(),"Armenia", "Casa Cafetera", "123", LocalDate.of(2025, 1, 10), LocalDate.of(2025, 2, 2), "12:00", "08:00", 600000, 2);
            appReservasPrincipal.crearReserva(UUID.randomUUID().toString(), "Bogota", "Casa Cedritos", "456", LocalDate.of(2024, 11, 29), LocalDate.of(2024, 12, 15), "12:00", "08:00", 600000, 2);
            appReservasPrincipal.crearReserva(UUID.randomUUID().toString(), "Medellin", "Hotel La Arepa", "123", LocalDate.of(2025, 4, 29), LocalDate.of(2025, 5, 15), "12:00", "08:00", 600000, 2);
            appReservasPrincipal.crearReserva(UUID.randomUUID().toString(), "San Andres", "Hotel MarAzul", "123", LocalDate.of(2025, 6, 29), LocalDate.of(2025, 7, 15), "12:00", "08:00", 600000, 2);
            // Add additional reservations similarly
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

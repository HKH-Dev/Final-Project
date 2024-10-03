package co.edu.uniquindio;

import co.edu.uniquindio.plataforma.alojamiento.ciudades.SanAndres;
import co.edu.uniquindio.plataforma.alojamiento.model.Ciudad;
import co.edu.uniquindio.plataforma.alojamiento.model.apto.*;
import co.edu.uniquindio.plataforma.alojamiento.model.casa.*;
import co.edu.uniquindio.plataforma.alojamiento.model.hotel.*;
import co.edu.uniquindio.plataforma.alojamiento.model.servicio.ServicioAlojamiento;


public class Main {
    public static void main(String[] args) {
        SanAndres sanAndres = new SanAndres();
        // Creación de un hotel usando el HotelBuilder
        Hotel hotelSanAndres = new HotelBuilder()
                .nombre("Hotel MarAzul")
                .descripcion("Resort Campestre en San Andrés")
                .ciudad(Ciudad.SAN_ANDRES)
                .imagen("imagen")
                .precioNoche(600000)
                .capacidadMaxima(120)
                .habitacionNumero("57")  // Ahora esto está disponible en HotelBuilder
                .precioHabitacion(350000)
                .build();
        // Creación de los servicios del hotel
        ServicioAlojamiento serviciosHotelSanAndres = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .gimnasio(true)
                .aireAcondicionado(true)
                .build();

        // Asignamos los servicios al hotel
        hotelSanAndres.agregarServicios(serviciosHotelSanAndres);
//
        sanAndres.agregarHotel(hotelSanAndres);

        // ---------------------------------------------------------
        // Crear una casa
        Casa casaSanAndres = new CasaBuilder()
                .nombre("Casa Playa Bonita")
                .descripcion("Casa cerca del mar")
                .ciudad(Ciudad.SAN_ANDRES)
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

        sanAndres.agregarCasa(casaSanAndres);

//        ---------------------------------------------------------

        // Crear un apartamento
        Apartamento apartamentoSanAndres = new ApartamentoBuilder()
                .nombre("Torre AquaMar")
                .descripcion("Apartamento con vista al mar")
                .ciudad(Ciudad.SAN_ANDRES)
                .imagen("imagenApto")
                .precioNoche(400000)
                .capacidadMaxima(4)
                .tieneBalcon(true)
                .tieneCocina(true)
                .build();

        ServicioAlojamiento servicioCasaAquaMar = ServicioAlojamiento.builder()
                .wifi(true)
                .piscina(true)
                .parqueadero(true)
                .tv(true)
                .desayuno(true)
                .gimnasio(true)
                .aireAcondicionado(true)
                .build();

        sanAndres.agregarApartamento(apartamentoSanAndres);

//        ---------------------------------------------------------
        Casa casaArmenia = new CasaBuilder()
                .nombre("Casa del Arriero").ciudad(Ciudad.ARMENIA).descripcion("Casa en Campestre en Armenia").imagen("imagen").precioNoche(100000).capacidadMaxima(10).tieneCocina(true).tieneJardin(true).build();

        ServicioAlojamiento serviciosCasaArriero = ServicioAlojamiento.builder()
                .wifi(true).piscina(true).parqueadero(true).tv(false).desayuno(true).gimnasio(false).aireAcondicionado(true).build();


// ---------------------------------------------------------
        Apartamento apartamentoCali = new ApartamentoBuilder()
                .nombre("torre de Cali").ciudad(Ciudad.CALI).descripcion("Apartamento en el centro de Cali").imagen("imagen").precioNoche(80000).capacidadMaxima(4).tieneBalcon(true).tieneCocina(true).build();

        ServicioAlojamiento serviciosApartamentoCali = ServicioAlojamiento.builder()
                .wifi(true).piscina(false).parqueadero(true).tv(true).desayuno(false).gimnasio(true).aireAcondicionado(true).build();


// ---------------------------------------------------------
        Casa casaIslena = new CasaBuilder()
                .nombre("Casa Islena").ciudad(Ciudad.SAN_ANDRES).descripcion("Casa en la playa en San Andres").imagen("imagen").precioNoche(200000).capacidadMaxima(8).tieneCocina(true).tieneJardin(true).build();

        ServicioAlojamiento serviciosCasaIslena = ServicioAlojamiento.builder()
                .wifi(true).piscina(true).parqueadero(true).tv(true).desayuno(true).gimnasio(false).aireAcondicionado(true).build();

        sanAndres.agregarCasa(casaIslena);


        // Mostrar todos los alojamientos
        System.out.println("Todos los alojamientos en San Andrés:");
        sanAndres.mostrarTodosLosAlojamientos();

//        // Mostrar solo los hoteles
//        System.out.println("\nHoteles en San Andrés:");
//        sanAndres.mostrarHoteles();
//
//        // Mostrar solo las casas
//        System.out.println("\nCasas en San Andrés:");
//        sanAndres.mostrarCasas();


    }
}
package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.apto.ApartamentoBuilder;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa.CasaBuilder;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.hotel.HotelBuilder;

public class AlojamientoFactory {
    public Alojamiento crearAlojamiento(String tipo) {
        return switch (tipo) {
            case "Hotel" -> new HotelBuilder().build();  // Se completará en el main
            case "Casa" -> new CasaBuilder().build();
            case "Apartamento" -> new ApartamentoBuilder().build();
            default -> throw new IllegalArgumentException("Tipo de alojamiento no soportado");
        };

//        switch (tipo) {
//            case "Hotel":
//                return new HotelBuilder().build();  // Se completará en el main
//            case "Casa":
//                return new CasaBuilder().build();
//            case "Apartamento":
//                return new ApartamentoBuilder().build();
//            default:
//                throw new IllegalArgumentException("Tipo de alojamiento no soportado");
//        }
    }
}

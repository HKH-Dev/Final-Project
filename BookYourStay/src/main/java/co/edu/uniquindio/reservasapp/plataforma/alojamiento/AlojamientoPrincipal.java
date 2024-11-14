package co.edu.uniquindio.reservasapp.plataforma.alojamiento;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlojamientoPrincipal {
    final private Map<String, Map<String, List<Alojamiento>>> alojamientosPorCiudadYTipo = new HashMap<>();

    public void agregarAlojamiento(Alojamiento alojamiento) {
        String ciudad = alojamiento.getCiudad().name();
        String tipo = alojamiento.getClass().getSimpleName();

        alojamientosPorCiudadYTipo.putIfAbsent(ciudad, new HashMap<>());

        alojamientosPorCiudadYTipo.get(ciudad).putIfAbsent(tipo, new ArrayList<>());
        alojamientosPorCiudadYTipo.get(ciudad).get(tipo).add(alojamiento);
    }

    public List<Alojamiento> obtenerAlojamientosPorCiudad(String ciudad) {
        if (alojamientosPorCiudadYTipo.containsKey(ciudad)) {
            return alojamientosPorCiudadYTipo.get(ciudad).values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();  // Si no hay alojamientos en la ciudad
        }
    }

    public List<Alojamiento> obtenerAlojamientosPorTipo(String tipo) {
        return alojamientosPorCiudadYTipo.values().stream()
                .flatMap(map -> map.getOrDefault(tipo, new ArrayList<>()).stream())
                .collect(Collectors.toList());
    }

    public List<Alojamiento> obtenerAlojamientosPorCiudadYTipo(String ciudad, String tipo) {
        if (alojamientosPorCiudadYTipo.containsKey(ciudad) &&
                alojamientosPorCiudadYTipo.get(ciudad).containsKey(tipo)) {
            return alojamientosPorCiudadYTipo.get(ciudad).get(tipo);
        } else {
            return new ArrayList<>();  // Si no hay alojamientos que coincidan
        }
    }

}

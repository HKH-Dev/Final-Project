package co.edu.uniquindio.plataforma.alojamiento.ciudades;

import co.edu.uniquindio.plataforma.alojamiento.model.Alojamiento;
import co.edu.uniquindio.plataforma.alojamiento.model.apto.Apartamento;
import co.edu.uniquindio.plataforma.alojamiento.model.casa.Casa;
import co.edu.uniquindio.plataforma.alojamiento.model.hotel.Hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SanAndres {
    public List<Casa> listaCasasSanAndres = new ArrayList<>();
    public List<Apartamento> listaApartamentosSanAndres = new ArrayList<>();
    public List<Hotel> listaHotelesSanAndres = new ArrayList<>();
    public List<Alojamiento> listaAlojamientosSanAndres = new ArrayList<>();

    public void agregarCasa(Casa casa){
        listaCasasSanAndres.add(casa);
        listaAlojamientosSanAndres.add(casa);
    }

    public void agregarApartamento(Apartamento apartamento) {
        listaApartamentosSanAndres.add(apartamento);
        listaAlojamientosSanAndres.add(apartamento); // También agregamos a la lista general
    }

    public void agregarHotel(Hotel hotel) {
        listaHotelesSanAndres.add(hotel);
        listaAlojamientosSanAndres.add(hotel); // También agregamos a la lista general
    }

    // Método para mostrar todos los alojamientos
    public void mostrarTodosLosAlojamientos() {
        for (Alojamiento alojamiento : listaAlojamientosSanAndres) {
            System.out.println(alojamiento);
        }
    }

    // Método para mostrar solo hoteles
    public void mostrarHoteles() {
        for (Hotel hotel : listaHotelesSanAndres) {
            System.out.println(hotel);
        }
    }
    public void mostrarCasas() {
        for (Casa casa : listaCasasSanAndres) {
            System.out.println(casa);
        }
    }


}
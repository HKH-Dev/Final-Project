package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CiudadObservable {

    public static ObservableList<Ciudad> obtenerCiudades() {
        // Retorna los valores del enum Ciudad como ObservableList
        return FXCollections.observableArrayList(Ciudad.values());
    }
}
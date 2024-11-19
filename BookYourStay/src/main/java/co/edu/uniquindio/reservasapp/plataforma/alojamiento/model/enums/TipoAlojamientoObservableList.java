package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TipoAlojamientoObservableList {
    public static ObservableList<TipoAlojamiento> obtenerListaAlojamientos() {
        return FXCollections.observableArrayList(TipoAlojamiento.values());
    }
}
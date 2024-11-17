package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class Horario {

    private List<String> horarios;

    public Horario() {
        horarios = generarHorarios();
    }

    public List<String> generarHorarios() {
        List<String> horarios = new ArrayList<>();
        for (int i = 8; i < 18; i++) {
            if (i < 10) {
                horarios.add("0" + i + ":00");
                horarios.add("0" + i + ":30");
            } else {
                horarios.add(i + ":00");
                horarios.add(i + ":30");
            }
        }
        return horarios;
    }

    public ObservableList<String> getHorarios() {
        List<String> horariosDisponible = horarios;
        return FXCollections.observableArrayList(horarios);
    }
}

package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model;

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

    // Metodo para obtener los horarios
    public List<String> getHorarios() {
        return horarios;
    }



//    public List<String> generarHorarios;
//    private String horarios;
//
//    public Horario(String horarios) {
//        generarHorarios = new ArrayList<>();
//        this.horarios = horarios;
//    }
//
//    public List<String> generarHorarios(){
//        List<String> horarios = new ArrayList<>();
//
//        for (int i = 8; i < 18; i++) {
//        if (i < 10) {
//            horarios.add("0" + i + ":00");
//            horarios.add("0" + i + ":30");
//        } else {
//            horarios.add(i + ":00");
//            horarios.add(i + ":30");
//            }
//        }
////        return List.of();
//        return horarios;
//    }
//
//    public List<String> getHorarios() {
//        return generarHorarios();
//    }

}

module BookYourStay {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jbcrypt;
    requires static lombok;
    requires org.simplejavamail;
    requires org.simplejavamail.core;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.desktop;
    requires jakarta.mail;

    opens co.edu.uniquindio.reservasapp to javafx.fxml;
    exports co.edu.uniquindio.reservasapp;
    opens co.edu.uniquindio.reservasapp.plataforma.controlador to javafx.fxml;
    exports co.edu.uniquindio.reservasapp.plataforma.controlador;
    opens co.edu.uniquindio.reservasapp.plataforma.modelo to javafx.fxml;
    exports co.edu.uniquindio.reservasapp.plataforma.modelo;
    exports co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion;
    exports co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums;


}
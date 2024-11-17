package co.edu.uniquindio.reservasapp.plataforma.modelo;

//La reserva debe almacenar la información del cliente, alojamiento, fechas, número de huéspedes, y generar una factura. La factura debe contener el subtotal, el total, una fecha y un código generado aleatoriamente (UUID).

import co.edu.uniquindio.reservasapp.plataforma.modelo.cliente.utils.EnvioEmail;
import co.edu.uniquindio.reservasapp.plataforma.modelo.resevacion.Reserva;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

//Cada vez que se realice una reserva, se debe generar un código QR con el código de la factura y se debe enviar al cliente por correo electrónico con los detalles de la reserva.
@Getter
@Setter
@Builder
public class Factura {
    private String codigo;
    private String fechaComienzo;
    private String fechaTerminacion;
    private double subtotal;
    private double total;


//    public void generarFactura(Reserva reserva) throws Exception {
//        // Generate a factura based on the reserva details
//        Factura factura = Factura.builder()
//                .codigo(UUID.randomUUID().toString())
//                .fechaComienzo(reserva.getDiaInicioReserva().toString())
//                .fechaTerminacion(reserva.getDiaFinReserva().toString())
//                .subtotal(reserva.getCosto() / ChronoUnit.DAYS.between(reserva.getDiaInicioReserva(), reserva.getDiaFinReserva()))
//                .total(reserva.getCosto())
//                .build();


//        // Generate QR code with the invoice code
//        BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImage(factura.getCodigo());
//
//        // Send email to the client with the QR code and reservation details
//        Persona cliente = obtenerPersona(reserva.getCedulaReservante())
//                .orElseThrow(() -> new Exception("Cliente no encontrado"));
//
//        EnvioEmail.sendEmailWithQRCode(cliente.getEmail(), factura, reserva, qrCodeImage);
//
//        // Optionally, store or log the factura
        // ...
}

package co.edu.uniquindio.reservasapp.plataforma.modelo.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Billetera {
    private double saldo = 5000000;

    public Billetera(double saldo) {
        this.saldo = saldo;
    }

    public void consignar(double valor) {
        saldo += valor;
    }


//    public double getSaldo() {return saldo;}
//    public void setSaldo(double saldo) {this.saldo = saldo;}
}

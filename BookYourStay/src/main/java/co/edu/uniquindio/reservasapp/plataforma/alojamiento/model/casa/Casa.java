package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.casa;

import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.enums.Ciudad;
import co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.Alojamiento;
//
//@ToString

public class Casa extends Alojamiento implements CasaFactory{
    private boolean tieneCocina;
    private boolean tieneJardin;
//    private Casa(CasaBuilder builder) {
//        super(builder.nombre, builder.descripcion, builder.imagen, builder.precioNoche, builder.capacidadMaxima);
//    }
//    @Override
//    public void tipoAlojamiento() {
//        System.out.println("Tipo de alojamiento: Casa");
//    }
//    public static class CasaBuilder extends AlojamientoBuilder<CasaBuilder>{
//        private boolean tieneCocina;
//        private boolean tieneJardin;
//
//        public CasaBuilder alojamientoTieneCocina(boolean tieneCocina){
//            this.tieneCocina = tieneCocina;
//            return self();
//        }
//        public CasaBuilder alojamientoTieneJardin(boolean tieneJardin){
//            this.tieneJardin = tieneJardin;
//            return self();
//        }
//        @Override
//        protected CasaBuilder self(){
//            return this;
//        }
//        @Override
//        public Casa build(){
//            return new Casa(this);
//        }
//    }
    public Casa(String nombre, Ciudad ciudad, String descripcion, String imagen, double precioNoche, int capacidadMaxima, boolean tieneCocina, boolean tieneJardin) {
        super(nombre, ciudad, descripcion, imagen, precioNoche, capacidadMaxima);
        this.tieneCocina = tieneCocina;
        this.tieneJardin = tieneJardin;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Casa{");
        sb.append("\n\t nombre = '").append(nombre).append('\'');
        sb.append("\n\t ciudad = ").append(ciudad);
        sb.append("\n\t descripcion = '").append(descripcion).append('\'');
        sb.append("\n\t tieneCocina = ").append(tieneCocina);
        sb.append("\n\t tieneJardin = ").append(tieneJardin);
        sb.append("\n\t imagen = '").append(imagen).append('\'');
        sb.append("\n\t precioNoche = ").append(precioNoche);
        sb.append("\n\t capacidadMaxima = ").append(capacidadMaxima);
        sb.append("\n\t\t servicios =").append(servicios);
        sb.append('}');
        sb.append("\n");
        return sb.toString();
    }
}

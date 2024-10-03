package co.edu.uniquindio.plataforma.alojamiento.model.servicio;


public class ServicioAlojamiento {
    private boolean wifi;
    private boolean piscina;
    private boolean parqueadero;
    private boolean tv;
    private boolean desayuno;
    private boolean gimnasio;
    private boolean aireAcondicionado;

    public ServicioAlojamiento(boolean wifi, boolean piscina, boolean parqueadero, boolean tv, boolean desayuno, boolean gimnasio, boolean aireAcondicionado) {
        this.wifi = wifi;
        this.piscina = piscina;
        this.parqueadero = parqueadero;
        this.tv = tv;
        this.desayuno = desayuno;
        this.gimnasio = gimnasio;
        this.aireAcondicionado = aireAcondicionado;
    }

    public static ServicioAlojamientoBuilder builder() {
        return new ServicioAlojamientoBuilder();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(" Servicio disponibles en Alojamiento{");
        sb.append("\n\t\t wifi = ").append(wifi);
        sb.append("\n\t\t piscina = ").append(piscina);
        sb.append("\n\t\t parqueadero = ").append(parqueadero);
        sb.append("\n\t\t tv = ").append(tv);
        sb.append("\n\t\t desayuno = ").append(desayuno);
        sb.append("\n\t\t gimnasio = ").append(gimnasio);
        sb.append("\n\t\t aireAcondicionado = ").append(aireAcondicionado);
        sb.append('}');
        return sb.toString();
    }
}

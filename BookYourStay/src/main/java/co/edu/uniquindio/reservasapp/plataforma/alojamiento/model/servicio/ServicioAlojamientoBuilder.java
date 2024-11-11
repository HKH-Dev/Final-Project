package co.edu.uniquindio.reservasapp.plataforma.alojamiento.model.servicio;


public class ServicioAlojamientoBuilder {
    private boolean wifi;
    private boolean piscina;
    private boolean parqueadero;
    private boolean tv;
    private boolean desayuno;
    private boolean gimnasio;
    private boolean aireAcondicionado;

    public ServicioAlojamientoBuilder() {
    }

    public ServicioAlojamientoBuilder wifi(Boolean wifi){
        this.wifi = wifi;
        return this;
    }
    public ServicioAlojamientoBuilder piscina(Boolean piscina){
        this.piscina = piscina;
        return this;
    }
    public ServicioAlojamientoBuilder parqueadero(Boolean parqueadero){
        this.parqueadero = parqueadero;
        return this;
    }
    public ServicioAlojamientoBuilder tv(Boolean tv){
        this.tv = tv;
        return this;
    }
    public ServicioAlojamientoBuilder desayuno(Boolean desayuno){
        this.desayuno = desayuno;
        return this;
    }
    public ServicioAlojamientoBuilder gimnasio(Boolean gimnasio){
        this.gimnasio = gimnasio;
        return this;
    }
    public ServicioAlojamientoBuilder aireAcondicionado(Boolean aireAcondicionado){
        this.aireAcondicionado = aireAcondicionado;
        return this;
    }

    public ServicioAlojamiento build() {
        return new ServicioAlojamiento(wifi, piscina, parqueadero, tv, desayuno, gimnasio, aireAcondicionado);
    }
}

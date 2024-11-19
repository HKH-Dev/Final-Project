package co.edu.uniquindio.reservasapp.ObserverPatern;

public interface Observable {
    void addObserver(Observer o);
    void deleteObserver(Observer o);
    void notifyObserver();
}

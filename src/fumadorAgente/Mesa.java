package fumadorAgente;

import java.util.*;

/**
 * Consite de un agente, los ingredientes que coloca el agente y los fumadores.
 * Cuando el agente coloca nuevos ingredientes la mesa avisara al fumador que es capaz de fumar con los ingredientes de la mesa.
 */
public class Mesa implements Runnable {
    private final Agente agente;
    private final List<Fumador> listaFumadores;
    private EnumSet<Ingrediente> ingredientesMesa = EnumSet.allOf(Ingrediente.class);

    public Mesa() {
        agente = new Agente(this);
        listaFumadores = new ArrayList<>();

        // Para cada ingrediente de la enumeracion hay un fumador.
        for (Ingrediente ingrediente : Ingrediente.values()) {
            listaFumadores.add(new Fumador(this, ingrediente));
        }
    }

    public void setIngredientesMesa(EnumSet<Ingrediente> ingredientes) {
        ingredientesMesa = ingredientes;
    }

    public EnumSet<Ingrediente> getIngredientesMesa() {
        return ingredientesMesa;
    }

    public void eliminarIngredientesMesa() {
        ingredientesMesa.clear();
    }

    public void avisarAgente() {
        synchronized (agente) {
            agente.notify();
        }
    }

    /**
     * Averigua cual es el hilo del fumador que debe ser avisado.
     */
    public void averiguarFumador() {
        // Crea un conjunto que solo contiene el ingrediente que no esta en el conjunto de la mesa.
        EnumSet<Ingrediente> ingredientes = EnumSet.complementOf(getIngredientesMesa());

        // Sabiendo cual es el ingrediente que falta, se avisa al fumador correspondiente.
        System.out.println("Ha sido avisado el fumador que tiene " + ingredientes.iterator().next() + ".");
        avisarFumador(ingredientes.iterator().next());
    }

    /**
     * Avisa el fumador con el ingrediente proporcionado.
     *
     * @param ingrediente Del fumador que se quiere avisar.
     */
    public void avisarFumador(Ingrediente ingrediente) {
        // De los fumadores, el que tenga este ingrediente que sea avisado.
        for (Fumador fumador : listaFumadores) {
            synchronized (fumador) {
                if (fumador.tieneEsteIngrediente(ingrediente))
                    fumador.notify();
            }
        }
    }

    /**
     * Inicio de los todos los hilos.
     */
    public void run() {
        new Thread(agente).start();
        for (Fumador fumador : listaFumadores)
            new Thread(fumador).start();
    }
}

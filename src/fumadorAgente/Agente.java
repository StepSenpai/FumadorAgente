package fumadorAgente;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * El agente genera 2 Ingredientes aleatorios cada vez que ha sido avisado.
 */
public class Agente implements Runnable {

    private final Mesa mesaFumadores;

    public Agente(Mesa mesaFumadores) {
        this.mesaFumadores = mesaFumadores;
    }

    /**
     * Devuelve un conjunto con 2 Ingredientes aleatorios.
     *
     * @return
     */
    public void escogerLosSiguientesIngredientes() {
        // Coge todos los ingredientes.
        EnumSet<Ingrediente> ingredientes = EnumSet.allOf(Ingrediente.class);

        // Elimina uno de los ingredientes aleatoriamente.
        // Para eliminar aleatoriamente es necesario pasar el conjunto a un array para poder elegir una posicion.
        Ingrediente[] array = ingredientes.toArray(new Ingrediente[0]);
        int i = ThreadLocalRandom.current().nextInt(array.length);
        ingredientes.remove(array[i]);

        // Coloca los ingredientes en la mesa.
        mesaFumadores.setIngredientesMesa(ingredientes);
        System.out.println("El agente ha colocado " + ingredientes + " en la mesa.");

        mesaFumadores.averiguarFumador();
    }

    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    escogerLosSiguientesIngredientes();
                    wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
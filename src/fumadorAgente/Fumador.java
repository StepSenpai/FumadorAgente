package fumadorAgente;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Cada fumador tiene un ingrediente para si mismo en cantidades ilimitadas.
 * El fymador intentara fumar con los ingredientes que se encuentran en la mesa, si no puede fumar se espera hasta ser avisado.
 * Cuando el fumador haya terminado de fumar avisara al agente para que coloque nuevos ingredientes.
 */
class Fumador implements Runnable {

    private final Mesa mesaFumadores;
    private final Ingrediente ingredienteDelFumador;

    public Fumador(Mesa mesaFumadores, Ingrediente ingredienteDelFumador) {
        this.mesaFumadores = mesaFumadores;
        this.ingredienteDelFumador = ingredienteDelFumador;
    }

    public boolean tieneEsteIngrediente(Ingrediente ingredienteComprobar) {
        return this.ingredienteDelFumador == ingredienteComprobar;
    }


    /**
     * El fumador fumara con su ingrediente y los proporcionados por la mesa.
     *
     * @throws InterruptedException
     */
    public void fumar() throws InterruptedException {
        // Para mensajes en color.
        String texto = "\u001B[";
        if (ingredienteDelFumador == Ingrediente.FUEGO)
            texto += "31m";
        if (ingredienteDelFumador == Ingrediente.TABACO)
            texto += "32m";
        if (ingredienteDelFumador == Ingrediente.PAPEL)
            texto += "33m";

        // No hace falta un control si el fumador puede fumar realmente, debido a que siempre se avisara al fumador correcto mediante el metodo en la mesa.
        // Aun asi esta este control aqui por motivos de pruebas para ver si realmente es el fumador correcto.

        // Crea un conjunto de enumeracion de los ingredientes que estan en la mesa.
        EnumSet<Ingrediente> ingredientesEnDisposicion = mesaFumadores.getIngredientesMesa();
        // Añade el ingrediente del fumador.
        ingredientesEnDisposicion.add(ingredienteDelFumador);

        // Si tiene todos los ingredientes necesarios puede fumar.
        if (ingredientesEnDisposicion.containsAll(List.of(Ingrediente.values()))) {
            int segundosAFumar = ThreadLocalRandom.current().nextInt(2, 4);
            System.out.println(texto + "Fumador que tiene " + ingredienteDelFumador + " se pone a fumar durante " + segundosAFumar + " segundos.\u001B[0m");
            Thread.sleep(segundosAFumar * 1000L);

            // Cuando a terminado de fumar los ingredientes an sido utilizados. Y avisa al agente para pedir nuevos ingredientes.
            mesaFumadores.eliminarIngredientesMesa();
            mesaFumadores.avisarAgente();

        } else {
            System.out.println(texto + "Fumador que tiene " + ingredienteDelFumador + " no puede fumar y se espera...\u001B[0m");
        }
    }

    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    wait();
                    fumar();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
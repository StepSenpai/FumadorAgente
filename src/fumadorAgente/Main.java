package fumadorAgente;

/**
 * Lanza un Hilo de Mesa que inicia el programa.
 */
class Main {
    public static void main(String[] args) {
        new Thread(new Mesa()).start();
    }
}
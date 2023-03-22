import java.io.IOException;

/**
 *
 * Para no complicarnos mucho en el método main, crearé una clase a parte para probar los hilos
 * Esta clase llamará a la clase principal mediante un hilo.
 *
 * @author AlexRG
 */

public class FTPThreads {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Se van a generar 5 hilos de cliente FTP");
        System.out.println("Asegúrese de que los credenciales de conexión son correctos...");
        Thread.sleep(2000);
        //Crearemos 5 hilos
        for(int i = 0; i < 5; i++) {
            Thread hilo = new Thread(new MainWithThreads());
            hilo.start();
            try {
                //Haremos que cada hilo creado se ejecute y espere al siguiente,
                //así no tendremos manejo de datos múltiple
                //Si quitamos este try que tiene el join podremos crear conexiones simultáneas tal
                //y como pide en el enunciado
                hilo.join();
            } catch (InterruptedException e) {
            }
        }
    }
}


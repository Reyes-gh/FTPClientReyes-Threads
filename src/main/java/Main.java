import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

/**
 * Clase Main donde instanciaremos el cliente FTP con sus credenciales
 * @author AlexRG
 */
public class Main {

    public static void momentoFTP() throws IOException {

        String a;
        String b;

        //Conexión a servidor FTP alojado en Android
        FTP ftp = new FTP("android", "android", "172.26.100.15", 2221);
        ftp.conectar();

        Boolean isAsking = true;
        Scanner sc = new Scanner(System.in);

        while(isAsking) {
            System.out.println("Qué operación deseas hacer?" +
                                       "\n1. Listar ficheros de un directorio" +
                                       "\n2. Descargar un fichero" +
                                       "\n3. Descargar un directorio" +
                                       "\n4. Subir un fichero" +
                                       "\n5. Salir.");
            int opc = sc.nextInt();

            switch (opc) {
                case 1:
                    System.out.println("Introduce la ruta del directorio (/path/to/dir)");
                    a = sc.next();
                    ftp.listFiles(a);
                    break;
                case 2:
                    System.out.println("Introduce la ruta al fichero junto con su nombre y extensión (/path/file.extension)");
                    a = sc.next();
                    System.out.println("Introduce el path donde se guardará el archivo(/path/to/save");
                    b = sc.next();
                    ftp.descargarFile(a, b);
                    break;
                case 3:
                    System.out.println("Introduce la ruta al directorio (/path)");
                    a = sc.next();
                    System.out.println("Introduce el path donde se guardarán los archivos(/path/to/save)");
                    b = sc.next();
                    ftp.descargarFile(a, b);
                    break;
                case 4:
                    System.out.println("Introduce el path al fichero local junto con su nombre y extensión (/path/file.extension)");
                    a = sc.next();
                    System.out.println("Introduce el path al directorio donde vas a subir el fichero, añadiendo también" +
                                               "\nsu nombre y extensión");
                    b = sc.next();
                    ftp.subirFile(a, b);
                    break;
                case 5:
                    ftp.desconectar();
                    isAsking = false;
                    break;

                default:
                    System.out.println("Por favor introduce una opción valida\n--------------");

            }
        }



        // Métodos explicados en detalle:

        /**
         * Conectamos el cliente al servidor
         *
         * @method conectar
         */
        //ftp.conectar();

        /**
         * Lista todos los ficheros del directorio introducido.
         * Ejemplo: /Download
         *
         * @method listFiles
         */
        //ftp.listFiles("/path/to/dir");

        /**
         * Descarga un fichero de un directorio.
         * Ejemplo: /Downloads/songs/fichero.extension  -  /home/alex/DOWNLOADS_ANDROID/fichero.extension
         *
         * @method descargarFile
         */
        //ftp.descargarFile("/path/to/file/file.extension", "/path/to/save/file.extension");

        /**
         * Descarga todas las carpetas y subcarpetas con sus respectivos
         * ficheros de un directorio
         * Ejemplo: Downloads
         *
         * @method descargarDir
         */
        //ftp.descargarDir("/path/to/dir", "/path/to/save/file");

        /**
         * Sube un archivo desde el cliente al servidor FTP, en mi caso el
         * teléfono.
         *
         * @method subirFile
         */
        //ftp.subirFile("/path/to/file/file.extension", "/path/file.extension");

        /**
         * Desconectamos el cliente
         *
         * @method desconectar
         */
        //ftp.desconectar();

    }

}

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Clase FTP donde crearemos el cliente y alojaremos los métodos que utilizará
 * el usuario.
 *
 * @author AlexRG
 */

public class FTP {
    private final String USER;
    private final String PASS;
    private final String SERVER;
    private final int PORT;
    private final FTPClient FTPCLIENT = new FTPClient();

    //constructor
    public FTP(String user, String pass, String server, int port) {
        this.USER = user;
        this.PASS = pass;
        this.SERVER = server;
        this.PORT = port;
    }

    /**
     * Este método devuelve el código de respuesta del servidor para
     * la operación realizada.
     * @param ftpClient
     */
    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    /**
     * Método para conectar el cliente FTP al servidor
     */
    public void conectar(){
        try {
            FTPCLIENT.connect(SERVER, PORT);
            FTPCLIENT.setDataTimeout(99999);
            showServerReply(FTPCLIENT);
            int replyCode = FTPCLIENT.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operación fallida. Server replyCode: " + replyCode);
                return;
            }
            boolean success = FTPCLIENT.login(USER, PASS);
            showServerReply(FTPCLIENT);
            if (!success) {
                System.out.println("No se ha podido iniciar sesión en el servidor");
            } else {
                System.out.println("\n--------\n SESIÓN INICIADA \n--------\n");
            }
        } catch (IOException ex) {
            System.out.println("Oops! Algo ha ido mal.");
            ex.printStackTrace();
        }
    }
    //desconectar

    /**
     * Método para desconectar el cliente del servidor.
     */
    public void desconectar(){
        try {
            FTPCLIENT.logout();
            FTPCLIENT.disconnect();
            if(!FTPCLIENT.isConnected()){
                System.out.println("\n-------\n SERVER: DESCONECTADO \n-------\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para listar los ficheros y subdirectorios de un directorio
     * @param path
     */
    public void listFiles(String path){
        FTPFile[] files;
        try {
            files = FTPCLIENT.listFiles(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Itera sobre los archivos printeando sus detalles.
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

        for (FTPFile file : files) {
            String details = file.getName();
            if (file.isDirectory()) {
                details = "/" + details + "...";
            }
            details += "\t\t" + file.getSize();
            details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
            System.out.println(details);
        }
        System.out.println("\n-----------------\n");
    }

    /**
     * Busca si el string dado por parámetro "busca" en el path "path"
     * contiene algo en el nombre de algun archivo.
     * Si lo encuentra devuelve el nombre de los archivos y su extensión.
     * @param busca
     * @param path
     */
    public void buscar(String busca,String path){
        FTPFileFilter filter = ftpFile -> (ftpFile.isFile() && ftpFile.getName().contains(busca));
        FTPFile[] result;
        try {
            result = FTPCLIENT.listFiles(path,filter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (result != null && result.length > 0) {
            System.out.println("SEARCH RESULT:");
            for (FTPFile aFile : result) {
                System.out.println(aFile.getName());
            }
        }
    }
    public void descargarDir(String androidDir, String localDir) throws IOException {

        FTPCLIENT.enterLocalPassiveMode();

        System.out.println("Connected");
        System.out.println("Descargando directorio...");
        FTPUtil.downloadDirectory(FTPCLIENT, androidDir, "", localDir);
        System.out.println("Descargado!");


    }

    /**
     * Método que descarga un archivo a la carpeta pasada por parámetro.
     * @param filepath
     * @param saveDirPath
     */
    public void descargarFile(String filepath, String saveDirPath) {
        try {

            //Usamos passive para superar el firewall
            FTPCLIENT.enterLocalPassiveMode();
            System.out.println("Connected");

            // Carpeta propia donde se guardará el archivo
            System.out.println("Descargando archivo...");
            if (FTPUtil.downloadSingleFile(FTPCLIENT, filepath,  saveDirPath + "/" + filepath)) {
                System.out.println("Archivo descargado!");
            } else {
                System.out.println("Error al descargar archivo!");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para subir archivo al servidor FTP
     * @param localFile
     * @param androidFile
     */
    public void subirFile(String localFile, String androidFile) {
        try {
            FTPCLIENT.enterLocalPassiveMode();

            FTPUtil.uploadSingleFile(FTPCLIENT, localFile, androidFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.io.*;

/**
 * Clase FTPUtil donde se alojarán las funciones internas del Cliente
 * FTP, esta clase contiene métodos que debe llamar la clase FTP.
 * @author AlexRG
 */

public class FTPUtil {

    /**
     * Método que descarga un único archivo utilizando retrieveFile.
     * Si no existen las carpetas que lo contienen se crean.
     *
     * @param ftpClient
     * @param remoteFilePath
     * @param savePath
     * @return Boolean
     * @throws IOException
     */
    public static boolean downloadSingleFile(FTPClient ftpClient,
                                             String remoteFilePath, String savePath) throws IOException {

        File downloadFile = new File(savePath);
        File parentDir = downloadFile.getParentFile();

        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        //Los métodos que tienen la opción de ser cerrados con .close se
        //cierran automáticamente si los ponemos en un try.
        try (OutputStream outputStream = new FileOutputStream(downloadFile)){
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean resultado;
           resultado = ftpClient.retrieveFile(remoteFilePath, outputStream);
           return resultado;
       } catch (IOException ex) {
            throw ex;
        }

    }

    /**
     * Se encarga de listar los directorios, almacenarlos en un array y descargar
     * todos los archivos que contenga, este método utiliza al método
     * downloadSingleFile
     *
     * @param ftpClient
     * @param parentDir
     * @param currentDir
     * @param saveDir
     * @throws IOException
     */
    public static void downloadDirectory(FTPClient ftpClient, String parentDir,
                                         String currentDir, String saveDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = ftpClient.listFiles(dirToList);

        if (subFiles != null) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                                          + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }

                String newDirPath = saveDir + parentDir + File.separator
                                            + currentDir + File.separator + currentFileName;
                if (currentDir.equals("")) {
                    newDirPath = saveDir + parentDir + File.separator
                                         + currentFileName;
                }

                if (aFile.isDirectory()) {
                    // create the directory in saveDir
                    File newDir = new File(newDirPath);
                    boolean created = newDir.mkdirs();
                    if (created) {
                        System.out.println("Creado el directorio: " + newDirPath);
                    } else {
                        System.out.println("Ya existe / No se pudo crear: " + newDirPath);
                    }

                    // download the sub directory
                    downloadDirectory(ftpClient, dirToList, currentFileName,
                            saveDir);
                } else {
                    // download the file
                    boolean success = downloadSingleFile(ftpClient, filePath,
                            newDirPath);
                    if (success) {
                        System.out.println("Descargado el archivo: " + filePath);
                    } else {
                        System.out.println("No se pudo descargar el archivo: "
                                                   + filePath);
                    }
                }
            }
        }
    }

    /**
     * Método que se encarga de subir un único archivo al servidor FTP.
     *
     * @param ftpClient
     * @param localFilePath
     * @param remoteFilePath
     * @return Boolean
     * @throws IOException
     */
    public static boolean uploadSingleFile(FTPClient ftpClient,
                                           String localFilePath, String remoteFilePath) throws IOException {

        File localFile = new File(localFilePath);

        try (InputStream inputStream = new FileInputStream(localFile)){
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.storeFile(remoteFilePath, inputStream);
        }
    }   

}
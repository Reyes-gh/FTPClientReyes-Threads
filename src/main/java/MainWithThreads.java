import java.io.IOException;

class MainWithThreads implements Runnable {
    public void run() {
        try {
            Main.momentoFTP();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

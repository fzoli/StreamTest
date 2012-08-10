package streamtest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zoli
 */
abstract class AbstractMJPEGServer {

    /**
     * A kapcsolatok megteremtéséhez szerver oldali socket.
     */
    private final ServerSocket ss;
    
    /**
     * A kapcsolatok azonosítójának a generálására kell.
     */
    private int id = 0;
    
    /**
     * MJPEG socket szerver.
     * @param port a szerver portja
     */
    public AbstractMJPEGServer(int port) throws Exception {
        ss = createServerSocket(port);
    }
    
    /**
     * A szerver elindítása.
     */
    public void start() throws Exception {
        while (!ss.isClosed()) { // amíg nincs a server socket bezárva
            Socket s = ss.accept(); // várakozás kliens kapcsolódásra
            process(s); // feldolgozás
        }
    }
    
    /**
     * A szerver leállítása.
     */
    public void stop() throws IOException {
        ss.close();
    }
    
    /**
     * A kapcsolódott kliens kapcsolatának feldolgozása új szálban.
     */
    protected void process(Socket s) throws Exception {
        new Thread(new MJPEGServerProcess(s, ++id)).start(); // szerver oldali feldolgozás indítása új szálban és ID növelése
    }
    
    /**
     * Server socket létrehozása.
     * @param port szerver portja
     */
    protected abstract ServerSocket createServerSocket(int port) throws Exception;
    
}
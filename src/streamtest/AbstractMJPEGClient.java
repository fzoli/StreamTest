package streamtest;

import java.net.Socket;

/**
 * @author zoli
 */
abstract class AbstractMJPEGClient {
    
    /**
     * A kapcsolat megteremtéséhez kliens oldali socket.
     */
    private final Socket s;
    
    /**
     * MJPEG socket kliens.
     * @param port a szerver portja
     */
    public AbstractMJPEGClient(int port) throws Exception {
        s = createSocket(port);
    }
    
    /**
     * A socket kliens futtatása.
     */
    public void run() {
        new Thread(new MJPEGClientProcess(s)).start(); // feldolgozás új szálban
    }
    
    /**
     * Socket létrehozása és kapcsolódás a szerverhez.
     * @param port szerver portja
     */
    protected abstract Socket createSocket(int port) throws Exception;
    
}
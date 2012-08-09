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
    
    public void run() {
        new Thread(new MJPEGClientProcess(s)).start(); // feldolgozás új szálban
    }
    
    protected abstract Socket createSocket(int port) throws Exception;
    
}
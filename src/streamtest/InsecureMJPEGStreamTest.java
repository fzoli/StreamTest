package streamtest;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * A titkosítatlan szerver.
 */
class MJPEGServer extends AbstractMJPEGServer {

    /**
     * Titkosítatlan MJPEG socket szerver.
     * @param port a szerver portja
     */
    public MJPEGServer(int port) throws Exception {
        super(port);
    }

    /**
     * Titkosítatlan Server socket létrehozása.
     * @param port szerver portja
     */
    @Override
    protected ServerSocket createServerSocket(int port) throws Exception {
        return new ServerSocket(12345);
    }
    
}

/**
 * A titkosítatlan kliens.
 */
class MJPEGClient extends AbstractMJPEGClient {

    /**
     * MJPEG socket kliens.
     * @param port a szerver portja
     */
    public MJPEGClient(int port) throws Exception {
        super(port);
    }

    /**
     * Titkosítatlan Socket létrehozása és kapcsolódás a szerverhez.
     * @param port szerver portja
     */
    @Override
    protected Socket createSocket(int port) throws Exception {
        return new Socket("localhost", 12345);
    }
    
}

/**
 * Titkosítatlan MJPEG socket stream tesztelése.
 * @author zoli
 */
public class InsecureMJPEGStreamTest implements MJPEGStreamTest {

    /**
     * Szerver elindítása.
     */
    @Override
    public void startServer() throws Exception {
        new MJPEGServer(12345).start();
    }
    
    /**
     * Kliens elindítása.
     */
    @Override
    public void startClient() throws Exception {
        new MJPEGClient(12345).run();
    }
    
}
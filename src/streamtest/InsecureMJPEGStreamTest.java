package streamtest;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * A szerver.
 */
class MJPEGServer extends AbstractMJPEGServer {

    public MJPEGServer(int port) throws Exception {
        super(port);
    }

    @Override
    protected ServerSocket createServerSocket(int port) throws Exception {
        return new ServerSocket(12345);
    }
    
}

/**
 * A kliens.
 */
class MJPEGClient extends AbstractMJPEGClient {

    public MJPEGClient(int port) throws Exception {
        super(port);
    }

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
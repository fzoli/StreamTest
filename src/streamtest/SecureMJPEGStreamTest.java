package streamtest;

import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.ssl.KeyMaterial;
import org.apache.commons.ssl.SSLClient;
import org.apache.commons.ssl.SSLServer;
import org.apache.commons.ssl.TrustMaterial;

/**
 * Az SSL szerver.
 */
class SSLMJPEGServer extends AbstractMJPEGServer {

    /**
     * SSL MJPEG socket szerver.
     * @param port a szerver portja
     */
    public SSLMJPEGServer(int port) throws Exception {
        super(port);
    }

    /**
     * SSL Server socket létrehozása.
     * @param port szerver portja
     */
    @Override
    protected ServerSocket createServerSocket(int port) throws Exception {
        SSLServer server = new SSLServer(); // SSL szerver socket létrehozására kell
        server.setKeyMaterial(new KeyMaterial(SecureMJPEGStreamTest.DIR + "testserver.crt", SecureMJPEGStreamTest.DIR + "testserver.key", new char[]{})); //publikus és privát kulcs megadása a kapcsolathoz, a jelszó üres
        server.setCheckCRL(true); // a hostname kivételével minden más ellenőrzése, amikor a kliens kapcsolódik
        server.setCheckExpiry(true);
        server.setCheckHostname(false);
        server.setTrustMaterial(new TrustMaterial(SecureMJPEGStreamTest.DIR + "ca.crt")); // a saját CA (és az ő általa kiállított tanusítványok) legyen megbízható csak (testserver és testclient tanusítványok)
        return server.createServerSocket(12345); // server socket létrehozása
    }
    
}



/**
 * Az SSL kliens.
 */
class SSLMJPEGClient extends AbstractMJPEGClient {

    /**
     * SSL MJPEG socket kliens.
     * @param port a szerver portja
     */
    public SSLMJPEGClient(int port) throws Exception {
        super(port);
    }

    /**
     * SSL Socket létrehozása és kapcsolódás a szerverhez.
     * @param port szerver portja
     */
    @Override
    protected Socket createSocket(int port) throws Exception {
        SSLClient client = new SSLClient(); // SSL kliens socket létrehozására kell
        client.setKeyMaterial(new KeyMaterial(SecureMJPEGStreamTest.DIR + "testclient.crt", SecureMJPEGStreamTest.DIR + "testclient.key", new char[]{})); //publikus és privát kulcs megadása a kapcsolathoz, a jelszó üres
        client.setCheckCRL(true); // hostname ellenőrzés kikapcsolása, minden más engedélyezése
        client.setCheckExpiry(true);
        client.setCheckHostname(false);
        client.setTrustMaterial(new TrustMaterial(SecureMJPEGStreamTest.DIR + "ca.crt")); // a saját CA (és az ő általa kiállított tanusítványok) legyen megbízható csak (testserver és testclient tanusítványok)
        return client.createSocket("localhost", 12345); // kliens socket létrehozása
    }
    
}

/**
 * Titkosított MJPEG socket stream tesztelése.
 * @author zoli
 */
public class SecureMJPEGStreamTest implements MJPEGStreamTest {

    /**
     * Az a könyvtár, amiben a tanusítványok találhatóak.
     * (publikus és privát egyaránt szerverhez és klienshez)
     */
    public static final String DIR = "/home/zoli/easy-rsa/2.0/keys/";
    
    /**
     * SSL szerver elindítása.
     */
    @Override
    public void startServer() throws Exception {
        new SSLMJPEGServer(12346).start();
    }
    
    /**
     * SSL kliens elindítása.
     */
    @Override
    public void startClient() throws Exception {
        new SSLMJPEGClient(12346).run();
    }
    
}
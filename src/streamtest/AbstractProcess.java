package streamtest;

import java.net.Socket;

/**
 * Ez az osztály paraméterül kap egy Socketet, amit fel kell dolgoznia egy külön szálban.
 * Mind a kliens, mind a szerver ezt az osztályt használja adat fogadására és küldésére.
 */
abstract class AbstractProcess implements Runnable {
    
    /**
     * A kommunikációt lebonyolító socket.
     */
    private final Socket s;
    
    public AbstractProcess(Socket s) {
        this.s = s;
    }

    /**
     * @return kommunikációt lebonyolító socket
     */
    protected Socket getSocket() {
        return s;
    }

}
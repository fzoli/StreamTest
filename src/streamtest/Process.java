/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package streamtest;

import java.net.Socket;

/**
 * Ez az osztály paraméterül kap egy Socketet, amit fel kell dolgoznia egy külön szálban.
 * Mind a kliens, mind a szerver ezt az osztályt használja adat fogadására és küldésére.
 */
public abstract class Process implements Runnable {
    
    /**
     * A kommunikációt lebonyolító socket.
     */
    private final Socket s;
    
    public Process(Socket s) {
        this.s = s;
    }

    protected Socket getSocket() {
        return s;
    }

}
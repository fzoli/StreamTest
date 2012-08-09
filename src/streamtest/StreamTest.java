package streamtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * A szerver.
 */
class Server {
    
    /**
     * A kapcsolatok azonosítójának a generálására kell.
     */
    private int id = 0;
    
    /**
     * A szerver kapcsolata.
     * A konstruktorban dobhat IOException kivételt ha már foglalt az 12345 port.
     */
    private final ServerSocket ss = new ServerSocket(12345);
    
    /**
     * A szerver oldali socket feldolgozó.
     * Két dátumot küld a kliensnek 1 másodperc késéssel a kettő között, aztán fogad 1 dátumot.
     */
    private class ServerProcess extends AbstractProcess {

        private final int pid;
        
        public ServerProcess(Socket s, int id) {
            super(s);
            this.pid = id;
        }
        
        @Override
        public void run() {
            try {
                OutputStream out = getSocket().getOutputStream(); // kimenet megszerzése
                out.write(pid); // ID elküldése a kliensnek, hogy tudja meg, mi a dolga
                
                ObjectOutputStream oout = new ObjectOutputStream(out); // objektumos kimenet megszerzése
                ObjectInputStream in = new ObjectInputStream(getSocket().getInputStream()); // objektumos bemenet megszerzése
                
                oout.writeObject(new Date()); // jelen dátum elküldése
                Thread.sleep(1000); // 1 másodperc várakozás
                oout.writeObject(new Date()); // jelen dátum elküldése
                
                if (pid % 2 == 0) { // ha az ID páros szám, akkor dátumot is fogadni kell
                    System.out.println(pid + " " + in.readObject()); // dátum fogadása és megjelenítése
                }
                
                in.close(); // kapcsolatok bezárása
                out.close();
            }
            catch (Exception ex) {
                ex.printStackTrace(); // sok hiba lehetséges, ami most nem érdekel, de jó lehet tudni
            }
        }
        
    }
    
    public Server() throws IOException {
        while (!ss.isClosed()) { // végtelen ciklus (mert isClosed false mindig a teszt alatt)
            Socket s = ss.accept(); // várakozás kliens kapcsolódásra
            new Thread(new ServerProcess(s, ++id)).start(); // szerver oldali feldolgozás indítása új szálban és ID növelése
        }
    }
    
}

/**
 * A kliens.
 */
class Client {
    
    /**
     * A kliens oldali socket feldolgozó.
     * Két dátumot fogad a szervertől, amit ki is ír a konzolra ID-vel együtt.
     */
    private class ClientProcess extends AbstractProcess {

        public ClientProcess(Socket s) {
            super(s);
        }

        @Override
        public void run() {
            try {
                InputStream in = getSocket().getInputStream(); // bemenet megszerzése
                int pid = in.read(); // ID megszerzése a szervertől
                
                ObjectInputStream oin = new ObjectInputStream(in); // objektumos bemenet megszerzése
                ObjectOutputStream out = new ObjectOutputStream(getSocket().getOutputStream()); // objektumos kimenet megszerzése
                
                System.out.println(pid + " "+ oin.readObject()); // két dátum kiírása
                System.out.println(pid + " "+ oin.readObject());
                
                if (pid % 2 == 0) { // ha páros szám az ID, akkor küldeni is kell
                    out.writeObject(new Date());
                }
                
                in.close(); // rendes vagyok, lezárom
                out.close(); 
            }
            catch (Exception ex) {
                ex.printStackTrace(); // megint csak sok sok hiba lehetséges, de nem érdekel most
            }
        }
        
    }
    
    public Client() throws UnknownHostException, IOException {
        for (int i = 0; i < 3; i++) { // három kapcsolat indítása szinte egy időben
            Socket s = new Socket("localhost", 12345); // új kapcsolódás
            new Thread(new ClientProcess(s)).start(); // feldolgozás új szálban
        }
    }
    
}

/**
 *
 * @author zoli
 */
public class StreamTest {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            if (args[0].equals("server")) new Server();
            else new Client();
        }
        else {
            new Client();
//            new Server();
        }
    }
    
}
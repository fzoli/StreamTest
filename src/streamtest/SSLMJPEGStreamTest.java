package streamtest;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.sf.jipcam.axis.MjpegFrame;
import net.sf.jipcam.axis.MjpegInputStream;
import org.apache.commons.ssl.KeyMaterial;
import org.apache.commons.ssl.SSLClient;
import org.apache.commons.ssl.SSLServer;
import org.apache.commons.ssl.TrustMaterial;
import sun.misc.BASE64Encoder;

/**
 * A szerver.
 */
class SSLMJPEGServer {
    
    /**
     * A kapcsolatok azonosítójának a generálására kell.
     */
    private int id = 0;
    
    /**
     * A szerver kapcsolata.
     * A konstruktorban dobhat IOException kivételt ha már foglalt az 12345 port.
     */
    private final ServerSocket ss;
    
    /**
     * A szerver oldali socket feldolgozó.
     * Két dátumot küld a kliensnek 1 másodperc késéssel a kettő között, aztán fogad 1 dátumot.
     */
    private class SSLServerProcess extends Process {

        private final int pid;
        
        public SSLServerProcess(Socket s, int id) {
            super(s);
            this.pid = id;
        }
        
        private JLabel lb = new JLabel(new ImageIcon());
        
        @Override
        public void run() {
            try {
                OutputStream out = getSocket().getOutputStream(); // kimenet megszerzése
                out.write(pid); // ID elküldése a kliensnek, hogy tudja meg, mi a dolga
                
                final InputStream in = getSocket().getInputStream(); // bemenet megszerzése
                
                new JFrame() {
                    {
                        setTitle("#" + pid);
                        add(lb);
                        getContentPane().setPreferredSize(new Dimension(320, 240));
                        pack();
                        setLocationRelativeTo(null);
                        setDefaultCloseOperation(HIDE_ON_CLOSE);
                        addComponentListener(new ComponentAdapter() {

                            @Override
                            public void componentHidden(ComponentEvent e) {
                                try {
                                    in.close();
                                }
                                catch (IOException ex) {
                                }
                            }
                        });
                    }
                }.setVisible(true);
                
                MjpegInputStream mjpegin = new MjpegInputStream(in);
                MjpegFrame fr;
                try {
                    while((fr = mjpegin.readMjpegFrame()) != null) {
                        lb.setIcon(new ImageIcon(fr.getImage()));
                    }
                }
                catch (Exception ex) {
                }
                
                in.close(); // kapcsolatok bezárása
                out.close();
            }
            catch (Exception ex) {
                ex.printStackTrace(); // sok hiba lehetséges, ami most nem érdekel, de jó lehet tudni
            }
        }
        
    }
    
    public SSLMJPEGServer() throws IOException, GeneralSecurityException {
        SSLServer server = new SSLServer();
        server.setKeyMaterial(new KeyMaterial("/home/zoli/easy-rsa/2.0/keys/testserver.crt", "/home/zoli/easy-rsa/2.0/keys/testserver.key", new char[]{}));
        server.setCheckCRL(true);
        server.setCheckExpiry(true);
        server.setCheckHostname(false);
        server.setTrustMaterial(new TrustMaterial("/home/zoli/easy-rsa/2.0/keys/ca.crt"));
        
        ss = server.createServerSocket(12345);
        while (!ss.isClosed()) { // végtelen ciklus (mert isClosed false mindig a teszt alatt)
            Socket s = ss.accept(); // várakozás kliens kapcsolódásra
            new Thread(new SSLServerProcess(s, ++id)).start(); // szerver oldali feldolgozás indítása új szálban és ID növelése
        }
    }
    
}

/**
 * A kliens.
 */
class SSLMJPEGClient {
    
    /**
     * A kliens oldali socket feldolgozó.
     * Két dátumot fogad a szervertől, amit ki is ír a konzolra ID-vel együtt.
     */
    private class SSLClientProcess extends Process {

        public SSLClientProcess(Socket s) {
            super(s);
        }

        @Override
        public void run() {
            try {
                InputStream in = getSocket().getInputStream(); // bemenet megszerzése
                int pid = in.read(); // ID megszerzése a szervertől
                System.out.println("Connection ID: " + pid);
                
                OutputStream out = getSocket().getOutputStream(); // kimenet megszerzése
                HttpURLConnection conn = (HttpURLConnection) new URL("http://192.168.10.5:8080/videofeed").openConnection();
                conn.setRequestMethod("GET");
                
                String userpass = "fzoli" + ":" + "asdfgh";
                BASE64Encoder encoder = new BASE64Encoder();
                String basicAuth = "Basic " + encoder.encode(userpass.getBytes()); //new String(new Base64().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);

                conn.connect();
                InputStream urlin = conn.getInputStream();
                
                try {
                    int i;
                    while((i = urlin.read()) != -1) {
                        out.write(i);
                    }
                }
                catch (Exception ex) {
                }
                
                in.close(); // rendes vagyok, lezárom
                out.close(); 
            }
            catch (Exception ex) {
                ex.printStackTrace(); // megint csak sok sok hiba lehetséges, de nem érdekel most
            }
        }
        
    }
    
    public SSLMJPEGClient() throws UnknownHostException, IOException, GeneralSecurityException {
        
        SSLClient client = new SSLClient();
        client.setKeyMaterial(new KeyMaterial("/home/zoli/easy-rsa/2.0/keys/testclient.crt", "/home/zoli/easy-rsa/2.0/keys/testclient.key", new char[]{}));
        client.setCheckCRL(true);
        client.setCheckExpiry(true);
        client.setCheckHostname(false);
        client.setTrustMaterial(new TrustMaterial("/home/zoli/easy-rsa/2.0/keys/ca.crt"));
        
        Socket s = client.createSocket("localhost", 12345);
        new Thread(new SSLClientProcess(s)).start(); // feldolgozás új szálban
    }
    
}

/**
 *
 * @author zoli
 */
public class SSLMJPEGStreamTest {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        if (args.length > 0) {
            if (args[0].equals("server")) new SSLMJPEGServer();
            else new SSLMJPEGClient();
        }
        else {
//            new SSLMJPEGServer();
            new SSLMJPEGClient();
        }
    }
    
}
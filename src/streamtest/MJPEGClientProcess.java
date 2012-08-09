package streamtest;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import sun.misc.BASE64Encoder;

/**
 *
 * @author zoli
 */
/**
 * A kliens oldali socket feldolgozó.
 * A szerverhez kapcsolódás után elkéri a kapcsolatazonosítót és konzolra kijelzi.
 * Ez után kapcsolódik egy jelszóvédett HTTP szerverhez, ami localhoston fut és továbbítja az MJPEG streamet a szerverhez.
 */
class MJPEGClientProcess extends AbstractProcess {

    public MJPEGClientProcess(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            InputStream in = getSocket().getInputStream(); // bemenet megszerzése
            int pid = in.read(); // kapcsolatazonosító megszerzése a szervertől
            System.out.println("Connection ID: " + pid);
            OutputStream out = getSocket().getOutputStream(); // kimenet megszerzése
            
            HttpURLConnection conn = (HttpURLConnection) new URL("http://192.168.10.5:8080/videofeed").openConnection(); //kapcsolat objektum létrehozása
            conn.setRequestMethod("GET"); // GET metódus beállítása

            // név és jelszó beállítása
            String userpass = "fzoli" + ":" + "asdfgh";
            BASE64Encoder encoder = new BASE64Encoder();
            String basicAuth = "Basic " + encoder.encode(userpass.getBytes()); //new String(new Base64().encode(userpass.getBytes()));
            conn.setRequestProperty ("Authorization", basicAuth);

            // most, hogy minden be van állítva, kapcsolódás
            conn.connect();
            InputStream urlin = conn.getInputStream(); // mjpeg stream megszerzése

            try {
                int length;
                byte[] buffer = new byte[2048]; // 2 kilóbájt buffer gyors FPS érdekében
                while((length = urlin.read(buffer)) != -1) { // addig amíg van adat, olvasás
                    out.write(buffer, 0, length); // a teljes bufferelt adat elküldése
                }
            }
            catch (Exception ex) { // bármi hiba van az MJPEG stream közben, nem érdekel
            }

            in.close(); // kapcsolat lezárása
            out.close(); 
        }
        catch (Exception ex) {
            ex.printStackTrace(); // megint csak sok sok hiba lehetséges, amiről már jó tudni
        }
    }

}
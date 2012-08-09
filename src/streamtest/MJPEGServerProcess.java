package streamtest;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.sf.jipcam.axis.MjpegFrame;
import net.sf.jipcam.axis.MjpegInputStream;

/**
 *
 * @author zoli
 */
/**
 * A szerver oldali socket feldolgozó.
 * Minden kapcsolathoz megjelenít egy ablakot, amiben az MJPEG streamet megjeleníti. Az ablak bezárása után a kapcsolat bezáródik.
 * A kapcsolat létrejötte után a kliensnek elküldi a kapcsolat azonosítót és elkezdi olvasni a klienstől kapott MJPEG streamet.
 */
class MJPEGServerProcess extends AbstractProcess {

    /**
     * Kapcsolatazonosító.
     */
    private final int pid;

    /**
     * Az MJPEG képkockákat megjelenítő címke.
     */
    private JLabel lb = new JLabel(new ImageIcon());
    
    public MJPEGServerProcess(Socket s, int id) {
        super(s);
        this.pid = id;
    }
    
    @Override
    public void run() {
        try {
            OutputStream out = getSocket().getOutputStream(); // kimenet megszerzése
            out.write(pid); // kapcsolatazonosító közlése a klienssel

            final InputStream in = getSocket().getInputStream(); // bemenet megszerzése

            new JFrame() { // ablak létrehozása
                {
                    setTitle("#" + pid); // a kapcsolatazonosító beállítása a címsorba
                    add(lb); // a képet megjelenítő címke hozzáadása az ablakhoz
                    getContentPane().setPreferredSize(new Dimension(320, 240)); // várt képméret beállítása
                    pack(); // ablakméret igazítása a képmérethez
                    setLocationRelativeTo(null); // ablak középre igazítása
                    setDefaultCloseOperation(HIDE_ON_CLOSE); // bezáráskor hide esemény meghívódás legyen
                    addComponentListener(new ComponentAdapter() {

                        @Override
                        public void componentHidden(ComponentEvent e) { // hide esemény esetén bemenet bezárása és ablak megölése
                            try {
                                in.close();
                                dispose();
                            }
                            catch (IOException ex) {
                            }
                        }
                    });
                }
            }.setVisible(true); // ablak létrejötte után jelenjen meg

            MjpegInputStream mjpegin = new MjpegInputStream(in); // a bemenetet mostantól MJPEG formátumban olvassa
            MjpegFrame fr; // az aktuális képkockát tartalmazó változó
            try {
                while((fr = mjpegin.readMjpegFrame()) != null) { // amíg van új képkocka
                    lb.setIcon(new ImageIcon(fr.getImage())); // képkockától a kép elkérése és megjelenítése a felületen
                }
            }
            catch (Exception ex) { // ha bérmi hiba történik (kapcsolatot bezárják, inputstream bezárul) nem kell tenni semmit
            }

            in.close(); // kapcsolatok bezárása
            out.close();
        }
        catch (Exception ex) {
            ex.printStackTrace(); // sok hiba lehetséges, ami most nem érdekel, de jó lehet tudni
        }
    }

}
package streamtest;

/**
 * Az MJPEG stream teszt elindítója.
 * 
 * Paraméterek legegyszerűbb használata:
 * - ssl szerver indítása: server ssl
 * - nem ssl szerver indítása: server
 * - ssl kliens indítása: ssl
 * - nem ssl kliens indítása: 
 * 
 * @author zoli
 */
public class MJPEGStreamTester {
    
    public static void main(String[] args) throws Exception {
        MJPEGStreamTest test; // tesztelő osztály
        if (args.length > 0) { // ha paraméterrel hívják
            if (args.length >= 2) { // ha min. 2 paraméter van
                if (args[1].equals("ssl")) test = new SecureMJPEGStreamTest(); // ha a 2. paraméter 'ssl', akkor biztonságos socket tesztelés
                else test = new InsecureMJPEGStreamTest(); // egyébként nem biztonságos socket tesztelés
            }
            else { // ha 1 paraméter van, nem biztonságos socket tesztelés
                if (args[0].equals("ssl")) test = new SecureMJPEGStreamTest(); // és a paraméter 'ssl', akkor biztonságos socket tesztelése
                else test = new InsecureMJPEGStreamTest(); // egyébként nem biztonságos socket tesztelése
            }
            if (args[0].equals("server")) test.startServer(); // ha az első paraméter 'server', akkor szerver indítása
            else test.startClient(); // egyébként a kliens indítása
        }
        else { // ha paraméter nélkül hívják
            test = new SecureMJPEGStreamTest(); // titkosított kapcsolat tesztelésére
//            test.startServer(); // szerver indítása
            test.startClient(); // kliens indítása
        }
    }
    
}
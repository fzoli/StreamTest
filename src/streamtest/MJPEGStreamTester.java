package streamtest;

/**
 * Az MJPEG stream teszt elindítója.
 * @author zoli
 */
public class MJPEGStreamTester {
    
    private static final MJPEGStreamTest test = new SecureMJPEGStreamTest(); // titkosított kapcsolat tesztelésére
    
    public static void main(String[] args) throws Exception {
        if (args.length > 0) { // ha paraméterrel hívják
            if (args[0].equals("server")) test.startServer(); // és az első paraméter 'server' akkor szervert indít
            else test.startClient(); // egyébként a kliens indítása
        }
        else { // ha paraméter nélkül hívják
//            test.startServer(); // szerver indítása
            test.startClient(); // kliens indítása
        }
    }
    
}
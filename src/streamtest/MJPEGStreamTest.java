package streamtest;

/**
 * A server ill kliens elindítására szolgáló interfész.
 * @author zoli
 */
interface MJPEGStreamTest {
    
    /**
     * Szerver elindítása.
     */
    void startServer() throws Exception;
    
    /**
     * Kliens elindítása.
     */
    void startClient() throws Exception;
    
}
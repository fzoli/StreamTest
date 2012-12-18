import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    
    private static boolean show_msg = false;
    private static int sock_port = 4444;
    private static String bejeaz = "96152";
    private static String embazon = "51312";
    
    public static void main(String[] args) throws IOException {
        HttpLafoxka lafoxka = new HttpLafoxka(bejeaz, embazon);
        /*List<String> ss = lafoxka.send("vége");
        System.out.println(ss);
        if (true) return;*/
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(sock_port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + sock_port);
            System.exit(1);
        }
 
        while (true) {

            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Socket accept failed.");
                continue;
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    clientSocket.getInputStream()));

            String s;
            while((s = in.readLine()) != null && !s.equals("<![[END]]>")) {
                if (show_msg) System.out.println("Te: " + s);
                List<String> answers = lafoxka.send(s);
                for (String answer : answers) {
                    out.print(answer+"\n");
                    if (show_msg) System.out.println("Lafoxka: " + answer);
                }
            }

            out.flush();

            in.close();
            out.close();

            clientSocket.close();

        }
        //serverSocket.close();
    }
    
}
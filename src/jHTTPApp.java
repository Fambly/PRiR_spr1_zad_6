import java.net.*;
import java.io.*;
import java.util.*;
class jHTTPSMulti extends Thread {
    File adresy = new File("adresy.txt");
    FileWriter fw = new FileWriter(adresy, true);
    private Socket socket = null;
    String getAnswer() throws IOException {
        InetAddress adres;
        String name = "";
        String ip = "";
        try {
            adres = InetAddress.getLocalHost();
            name = adres.getHostName();
            ip = adres.getHostAddress();
        }
        catch (UnknownHostException ex) { System.err.println(ex); }
        String document = "<html>\r\n" +
                "<body><br>\r\n" +
                "<h2><font color=blue>GRATULACJE!!!\r\n" +
                "</font></h2>\r\n" +
                "<h3>Wygrałeś iPhone! Podaj adres i numer karty :)</h3><hr>\r\n" +
                "Jesteś 1000 osobą która odwiedziła tę stronę w dniu: <b>" + new Date() + "</b><br>\r\n" +
                "Czy nazwa tego komputera to nie przypadkiem: <b>" + name + "</b>?<br>\r\n" +
                "A do tego adres: <b>" + ip + "</b>???<br>\r\n" +
                "<hr>\r\n" +
                "**Uwaga nie wysyłaj żadnych wrażliwych danych przez internet, zwłaszcza kiedy ponoć coś wygrałeś!**<br>" +
                "Kampania realizowana w celu uświadomienia ludzi korzystających z internetu o ryzyku,<br> jakie za sobą niesie nieograniczony dostęp do sieci.:)" +
                "</body>\r\n" +
                "</html>\r\n";
        String header = "HTTP/1.1 200 OK\r\n" +
                "Server: jHTTPServer ver 1.1\r\n" +
                "Last-Modified: Fri, 28 Jul 2000 07:58:55 GMT\r\n" +
                "Content-Length: " + document.length() + "\r\n" +
                "Connection: close\r\n" +
                "Content-Type: text/html; charset=utf-8";
        fw.write(ip+"\n");
        return header + "\r\n\r\n" + document;
    }
    public jHTTPSMulti(Socket socket) throws IOException {
        System.out.println("Nowy obiekt jHTTPSMulti...");
        this.socket = socket;
        start();
    }
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            System.out.println("---------------- Pierwsza linia zapytania ----------------");
            System.out.println(in.readLine());
            System.out.println("---------------- Wysylam odpowiedz -----------------------");
            System.out.println(getAnswer());
            System.out.println("---------------- Koniec odpowiedzi -----------------------");
            out.println(getAnswer());
            out.flush();
            fw.close();
        } catch (IOException e) {
            System.out.println("Blad IO danych!");
        }
        finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Blad zamkniecia gniazda!");
            }
        } // finally
    }
}
public class jHTTPApp {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(80);
        try {
            while (true) {
                Socket socket = server.accept();
                new jHTTPSMulti(socket);
            } // while
        } // try
        finally { server.close();}
    } // main
}
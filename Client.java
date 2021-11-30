import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientThread extends Thread {
    Socket socket;
    BufferedReader br;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = br.readLine();
                if(!message.isEmpty()){
                    System.out.println(message);
                    System.out.print("mail user@ip :port text or mail: ");
                }                
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }
}

public class Client {

    public static void main(String[] args) {
        String serverIP = "192.168.92.1";
        int serverPort = 4242;
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            socket = new Socket(serverIP, serverPort);
            pw = new PrintWriter(socket.getOutputStream());            
            System.out.print("Enter signin + name: ");
            String message = br.readLine();
            pw.println(message);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        while (true) {
            try {
                new ClientThread(socket).start();
                String message = br.readLine();
                pw.println(message);
                pw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
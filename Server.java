import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

class ServerThread extends Thread {
    Socket socket;
    String[] split;
    PrintWriter pw = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static List<ClientModel> clients = new ArrayList<>();
    ClientModel client;
    Socket email_socket;
    PrintWriter email_pw;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());
            while (true) {
                String message = br.readLine();
                split = message.split(" ");
                String name="";

                if (split[0].equals("signin")) { //client signin
                    name = split[1];
                    client = new ClientModel(getAddress(),socket.getPort(),name);
                    clients.add(client);
                    pw.println("you signed in successfully\n");

                }else if (message.contains("@")) { //client send email
                    String receiver_name = split[1].split("@")[0];
                    String receiver_ip = split[1].split("@")[1].split(":")[0];
                    String receiver_port = split[2].split(":")[1];
                    String receiver_message = split[3];
                    
                    try {
                        email_socket = new Socket(receiver_ip, Integer.parseInt(receiver_port));
                        email_pw = new PrintWriter(email_socket.getOutputStream());
                        email_pw.println("mail "+receiver_name+" "+client.getName()+" "+receiver_message);
                        email_pw.flush();
                    } catch (Exception e) {
                        System.out.println("Provide valid ip and port");
                    }
                    
                    pw.println("ok\n");

                }else if(split.length == 1 && split[0].equals("mail")){ //get client emails
                    pw.println(client.getEmails());
                
                }else if(!message.contains("@") && split[0].equals("mail")){//server send to server
                    String receiver_name = split[1];
                    String sender_name = split[2];
                    String receiver_message = split[3];

                    for(ClientModel client:clients){
                        if(client.getName().equals(receiver_name)){
                            client.newEmail(sender_name, receiver_message);
                        }
                    }
                    pw.println("ok\n");
                }

                else if (split[0].equals("signout")) {
                    socket.close();

                } else {
                    pw.println("Enter a valid command");
                }
                pw.flush();
            }
        } catch (IOException e) {
            System.out.println("Something Went Wrong");
        }
    }

    private String getAddress() {
        InetSocketAddress sockaddr = (InetSocketAddress) socket.getRemoteSocketAddress();
        InetAddress inaddr = sockaddr.getAddress();
        String address = inaddr.getHostAddress().toString();
        return address;
    }

}

public class Server {

    public static void main(String args[]) {
        ServerSocket serverSocket = null;

        try {            
            System.out.println("Server Listening...");
            serverSocket = new ServerSocket(4242);
        } catch (Exception ex) {
            System.out.println("Something Went Wrong");
        }

        while (true) {
            Socket clientSocket = null;
            try {
                if (serverSocket != null) {
                    clientSocket = serverSocket.accept();
                    new ServerThread(clientSocket).start();
                    System.out.print("new user accepted\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

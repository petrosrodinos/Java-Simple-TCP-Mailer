import java.util.HashMap;
import java.util.Map.Entry;

public class ClientModel {
    private String ip;
    private int port;
    private String name;
    private static HashMap<String, String> emails = new HashMap<String, String>();
    String emails_string="";
    
    public ClientModel(String ip, int port, String name) {
        this.ip = ip;
        this.name = name;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getEmails() {
        for (Entry<String, String> mail : emails.entrySet()) {
            String key = mail.getKey();
            String value = mail.getValue();
            emails_string+=key+":"+value+" , ";
            
        }
        System.out.println(emails_string);
        return emails_string;
    }

     public void newEmail(String name,String text) {
        emails.put(name,text);
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

}
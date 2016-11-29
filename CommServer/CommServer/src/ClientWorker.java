
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientWorker implements Runnable {
    boolean newMessageAvail = false;
    String newMessage = "";
    Socket mClient = null;
    String userName = "";
    boolean done = false;
    boolean disconnect = false;
    BufferedReader in = null;
    PrintWriter out = null;
    
    public ClientWorker(Socket client)
    {
       mClient = client;
       userName = null;
       try
        {
            in = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
            out = new PrintWriter(mClient.getOutputStream(), true);
        } catch (IOException ex) {}
    }
    @Override
    public void run()
    {
        try
        {       
            String junk = in.readLine();
            out.println("Enter username");
            userName = in.readLine();
        } catch (IOException ex) {}
        
        System.out.println("Client: " + userName + " started");

        while(!done)
        {
            try
            {
                String temp = in.readLine();
                if(temp.equals("CLIENT_GOODBYE"))
                {
                    done = true;
                }
                else
                {
                    if(newMessage.equals(""))
                    {
                        newMessage = temp;
                    }
                    else
                    {
                        newMessage += "~"+temp;
                    }
                }
                
                newMessageAvail = true;
            } catch (IOException ex) {
                Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while(!disconnect)
        {}
        return;
    }
    
    public String getNewMessage()
    {
        String temp = newMessage;
        newMessage = "";
        newMessageAvail = false;
        return temp;
    }
    
    public void sendMessage(String message)
    {
        out.println(message);
    }
    
    public void cleanUp() throws IOException
    {
        disconnect = true;
        mClient.close();
    }
}

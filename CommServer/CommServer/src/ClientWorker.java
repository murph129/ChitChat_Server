
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Steve
 */
public class ClientWorker implements Runnable {
    boolean newMessageAvail = false;
    String newMessage = null;
    Socket mClient = null;
    String userName = null;
    
    BufferedReader in = null;
    PrintWriter out = null;
    
    public ClientWorker(Socket client, String name)
    {
       mClient = client;
       userName = name;
    }
    public void run()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
            out = new PrintWriter(mClient.getOutputStream(), true);
        } catch (IOException ex) {}
        
        try
        {
            String temp = in.readLine();
            if(newMessage.equals(null))
            {
                newMessage = temp;
            }
            else
            {
                newMessage += "~"+temp;
            }
            newMessageAvail = true;
            
        } catch (IOException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getNewMessage()
    {
        String temp = newMessage;
        newMessage = null;
        newMessageAvail = false;
        return temp;
    }
}

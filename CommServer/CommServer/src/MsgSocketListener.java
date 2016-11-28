
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
public class MsgSocketListener implements Runnable {
    ArrayList clients = new ArrayList();
    boolean done = false;
    
    public void run()
    {
        ServerSocket socket = null;
        Socket s = null;
        System.out.println("Msg Socket Listener Started");
        try 
        {
            socket = new ServerSocket(8190);
            socket.setSoTimeout(1000);
        } catch (IOException ex) {}
        
        while(!done)
        {
            ClientWorker cw = null;
            try 
            {
                cw = new ClientWorker(socket.accept());
                Thread t = new Thread(cw);
                t.start();
                clients.add(cw);
            } catch (IOException ex) {}
            
            try
            {
                for(Object o : clients)
                {
                    ClientWorker tempClient = (ClientWorker)o;
                    if(tempClient.newMessageAvail)
                    {
                        String sender = tempClient.userName;
                        String temp = tempClient.getNewMessage();
                        for(Object x : clients)
                        {
                            ClientWorker tempMess = (ClientWorker) x;
                            if(sender.equals(tempMess.userName))
                            {}
                            else
                            {
                              tempMess.sendMessage(sender + ": " + temp);  
                            }
                        }
                    }
                }
                if(cw.newMessageAvail)
                {
                    System.out.println(cw.userName + " conneceted");
                }   
            }
            catch(Exception ex)
            {}
        }
    }
}

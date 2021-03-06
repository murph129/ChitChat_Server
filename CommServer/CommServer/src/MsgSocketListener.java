
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MsgSocketListener implements Runnable {
    ArrayList clients = new ArrayList();
    boolean done = false;
    boolean newUser = false;
    
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
                newUser = true;
            } catch (IOException ex) {}
            
            try
            {
                checkConnections();
                relayMessage();
            }
            catch(Exception ex)
            {}
        }
    }
    
    //CHECK TO SEE IF CONNECTIONS ARE STILL ALIVE - IF NOT, REMOVE FROM CLIENTS ARRAY LIST
    public void checkConnections() throws IOException, InterruptedException
    {
        int index = 0;
        for(Object o : clients)
        {
            ClientWorker temp = (ClientWorker)o;
            if(temp.done)
            {
                temp.cleanUp();
                clients.remove(index);
            }
            index++;
        }
    }
    
    //RELAY ANY MESSAGES FROM A CLIENT THREAD TO THE REST
    public void relayMessage()
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
                    tempMess.sendMessage(sender + ": " + temp);  
                }
            }
        }   
    }
}

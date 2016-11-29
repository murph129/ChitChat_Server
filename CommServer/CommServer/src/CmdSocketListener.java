
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class CmdSocketListener implements Runnable {
    boolean done = false;
    BufferedReader in = null;
    PrintWriter out = null;
    
    public void run()
    {
        ServerSocket socket = null;
        Socket s = null;
        System.out.println("Cmd Socket Listener Started");
        
        while(!done)
        {
            try 
            {
                socket = new ServerSocket(8191);
                //socket.setSoTimeout(1000);
                s = socket.accept();
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(), true);
                
                String junk = in.readLine();
                
                String[] command = in.readLine().split(",");
                
                switch(command[0])
                {
                    case "ADD": addUser(command); break;
                    case "UPDT": updateUser(command); break;
                }
                s.close();
            } catch (IOException ex) {}     
        }  
    }
    
    public void addUser(String[] command)
    {
        System.out.println("Reached.");
    }
    
    public void updateUser(String[] command)
    {
        
    }
}

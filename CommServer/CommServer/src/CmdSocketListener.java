
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
                String commands = in.readLine();
                String[] command = commands.split(",");
                
                if(command[0].equals("ADD"))
                {
                    addUser(command);
                }
                else if(command[0].equals("UPDT"))
                {
                    updateUser(command);
                }
                else
                {
                    out.println("Invalid Command");
                }
                s.close();
                socket = null;
                s = null;
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }     
        }  
    }
    
    public void addUser(String[] command) throws IOException
    {
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        String profilePath = path+ "\\profile.csv";
        FileWriter writer = new FileWriter(profilePath,true);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(command[1]+","+command[2]+","+command[3]+","+command[4]+","+command[5]+"\n");
        writer.close();
    }
    
    public void updateUser(String[] command)
    {
        
    }
}

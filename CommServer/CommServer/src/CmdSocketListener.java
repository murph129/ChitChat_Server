
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


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
                out.println("connected");
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
    
    public void updateUser(String[] command) throws IOException
    {
        // Get Profile CSV path
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        String profilePath = path + "\\profile.csv";
        

        // Create a reader to read in the csv file
        BufferedReader reader = new BufferedReader(new FileReader(profilePath));
        // Read the profile CSV into memory
        String line;
        ArrayList data = new ArrayList();
        /*************************
         * FOR DEBUGGING ONLY
         ************************/
        System.out.println("Looking for " + command[1]);
        while((line = reader.readLine()) != null) {
            // Check to see if edits need to be made

            /********************************************
             * FOR DEBUGGING ONLY
             *******************************************/
            System.out.println("Looking at " + line.split(",")[0]);

            if(line.split(",")[0].equals(command[1])) { // Username matches: use new information
                data.add(command[1]+","+command[2]+","+command[3]+","+command[4]+","+command[5]+"\n");
            } else { // Username does not match: use existing information
                data.add(line);
            }
        } // End While (read file)

        // close the reader to open up the write lock
        reader.close();

        // Write new data to file
        FileWriter writer = new FileWriter(profilePath, false); // False, to overwrite
        for(int i = 0; i < data.size(); i++) {
            writer.write(data.get(i).toString()); // ToString needed; data is an arraylist of objects, writer.write only accepts strings & chars
        }
        
        // Explicitly close the writer, because why not
        writer.close();
        
    }
}


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
                    out.println(addUser(command));
                }
                else if(command[0].equals("UPDT"))
                {
                    out.println(updateUser(command));
                }
                else
                {
                    out.println("Invalid Command");
                }
                s.close();
                socket.close(); 
                socket = null;
                s = null;
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }     
        }  
    }
    
    public String addUser(String[] command)
    {

        // Does the profile name already exist?
        if(userExists(command[1])) {
            return "false,User already exists";
        }
        
        BufferedWriter writer; // Used to write to profile.csv and cred.csv

        
        // Write to profile.csv
        String profilePath = getProfilePath();
        File profile = new File(profilePath);
        System.out.println("Profile.csv path: " + profilePath);
        
        // Make sure profile.csv exists
        if(!profile.exists()) { // If profile.csv does not exist already
            try { // Try creating a new file, catches IOException
                profile.createNewFile();
                System.out.println("Created profile.csv");   
            } catch(IOException exc) {
                exc.printStackTrace();
                return "false,There was an error adding your account";
            } // End try creating new file
        } // End If profile.csv does not exist
        
        // Write out to profile.csv
        System.out.println("Writing to "+profilePath);
        try {
            writer = new BufferedWriter(new FileWriter(profilePath, true));
            writer.write(command[1]+","+command[2]+","+command[3]+","+command[4]+","+command[5]+","+command[6]+"\n");
            writer.close();
        } catch(IOException exc) {
            exc.printStackTrace();
            return "false,There was an error adding your account";
        }
        
        
        
        // Write to cred.csv
        String credPath = getCredPath();
        File cred = new File(credPath);
        System.out.println("Cred.csv path: "+credPath);
        
        // Make sure cred.csv exists
        if(!cred.exists()) { // If cred.csv does not exist
            try { // Try creating cred.csv: catches IOException
                cred.createNewFile();
                System.out.println("Created cred.csv");
            } catch(IOException exc) {
                exc.printStackTrace();
                return "false,There was an error adding your account";
            }// End Try creating cred.csv
        } // End If cred.csv does not exist

        try { // Try writing to cred.csv
            writer = new BufferedWriter(new FileWriter(credPath, true));
            System.out.println("Writing to "+credPath);
            writer.write(command[1]+","+command[2]+"\n");            
            writer.close();
        } catch(IOException exc) {
            exc.printStackTrace();
            return "false,There was an error adding your account";
        }
        
        return "true,Account added";
    }
    
    public String updateUser(String[] command)
    {
        // Reader for reading file contents
        BufferedReader reader;
        // Writer for writing to file
        BufferedWriter writer;
        // Arraylist for holding file contents temporarily
        ArrayList data;
        // String for holding each individual line
        String line;
        
        // Get Profile CSV path
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        String profilePath = path + "\\profile.csv";
        String credPath = path + "\\cred.csv";
        
        // Check the existence of the csvs
        if(!new File(profilePath).exists()) {
            System.out.println(profilePath + " does not exist.  Aborting");
            return "false,User does not exist";
        } else if(!new File(credPath).exists()) {
            System.out.println(credPath + " does not exist. Aborting");
            return "false,User does not exist";
        }
        
        
        /**************************
         * UPDATE PROFILE.CSV
         *************************/

        try {
            // Create a reader to read in the csv file
            reader = new BufferedReader(new FileReader(profilePath));
            // Read the profile CSV into memory
            data = new ArrayList();
            System.out.println("Examining PROFILE.CSV");
            System.out.println("Looking for " + command[1]);
            while((line = reader.readLine()) != null) {
                System.out.println("Looking at " + line.split(",")[0]);
                if(line.split(",")[0].equals(command[1])) { // Username matches: use new information
                    data.add(command[1]+","+command[2]+","+command[3]+","+command[4]+","+command[5]+","+command[6]+"\n");
                } else { // Username does not match: use existing information
                    data.add(line+"\n");
                }
            } // End While (read file)
            // close the reader
            reader.close();
        } catch(IOException exc) {
            exc.printStackTrace();
            return "false,Error updating profile";
        }
        

        try {
            // Write new data to file
            writer = new BufferedWriter(new FileWriter(profilePath, false)); // False, to overwrite
            System.out.println("Writing to "+profilePath);
            for(int i = 0; i < data.size(); i++) {
                writer.write(data.get(i).toString()); // ToString needed; data is an arraylist of objects, writer.write only accepts strings & chars
            }

            // Close writer
            writer.close();
        } catch(IOException exc) {
            exc.printStackTrace();
            return "false,Error updating profile";
        }


        
        /**************************
         * UPDATE CRED.CSV
         *************************/
        
        try {
            // Create a reader to read in the csv file
            reader = new BufferedReader(new FileReader(credPath));
            data = new ArrayList(); // Reset data

            // Read the profile CSV into memory
            System.out.println("Examining CRED.CSV");
            System.out.println("Looking for "+command[1]);
            while((line = reader.readLine()) != null) {
                System.out.println("Looking at " + line.split(",")[0]);
                if(line.split(",")[0].equals(command[1])) { // If there is a match of usernames
                    data.add(command[1] + "," + command[2] + "\n");
                } else { // There is no match: use the existing data
                    data.add(line + "\n");
                }
            } // End While (read file)

            // Close the reader to open up the write lock
            reader.close();
        } catch(IOException exc) {
            exc.printStackTrace();
            return "false,Error updating profile";
        }
        
        
        
        try {
            // Create a new writer to write the data to the cred.csv
            writer = new BufferedWriter(new FileWriter(credPath, false)); // False, to overwrite
            System.out.println("Writing to " + credPath);
            for(int i = 0; i < data.size(); i++) {
                writer.write(data.get(i).toString()); // ToString needed; data is an arraylist of objects, writer.write only accepts strings & chars
            }

            // Close writer
            writer.close();
        } catch(IOException exc) {
            exc.printStackTrace();
            return "false,Error updating profile";
        }
        
        return "true,Profile successfully updated";
        
    }
    
    
    public String getProfilePath() {
        
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        return dir.toString() + "\\profile.csv";
    }
    
    public String getCredPath() {
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile().getParentFile().getParentFile().getParentFile();
        return dir.toString() + "\\AuthServer\\build\\cred.csv";
    }
    
    public boolean userExists(String needle) {

        boolean found = false; // We have not yet found the needle
        
        // Does the file exist?
        File f = new File(getProfilePath());
        if(!f.exists()) { // If the file does not exist
            return false; // ....the user doesn't either
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f.getAbsolutePath())); // Open a reader
            String line;
            while((line = reader.readLine()) != null && !found) { // While there are lines in the file, and needle has not been found
                String haystack = line.split(",")[0];
                if(haystack.equals(needle)) {
                    found = true;
                }
            }
        } catch(IOException exc) {
            exc.printStackTrace();
            return false;
        }
        
        return found;
    }
    
}


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    
    String profile = "";
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
        //READ IN THE USERNAME WHO CONNECTED
        try
        {   
            String junk = in.readLine();
            out.println("connected");
            userName = in.readLine();
            profile = getProfileInfo(userName);
            sendMessage(profile);
        } catch (IOException ex) {}
        
        //IF NO PROFILE IS FOUND - CLOSE THREAD AND DISCONNECT USER
        if(profile.equals("false,no profile found for that user"))
        {
            done = true;
        }
        else
        {
            System.out.println("Client: " + userName + " started");
        }
       
        //READ IN LINES FROM CHAT
        while(!done)
        {
            try
            {
                //READ IN CHAT MESSAGE 
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
    
    //GET NEW MESSAGE FROM THIS THREAD AND SEND BACK TO MSGSOCKETLISTENER THREAD
    public String getNewMessage()
    {
        String temp = newMessage;
        newMessage = "";
        newMessageAvail = false;
        return temp;
    }
    
    //SEND MESSAGE TO CONNECTED CLIENT
    public void sendMessage(String message)
    {
        out.println(message);
    }
    
    //CLEANUP AND CLOSE CONNECTION
    public void cleanUp() throws IOException
    {
        disconnect = true;
        mClient.close();
    }
    
    //GET PROFILE INFORMATION FOR THE CONNECTED CLIENT
    public String getProfileInfo(String user) throws FileNotFoundException, IOException
    {
        String result = "";
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        String profilePath = path+ "\\profile.csv";
        
        String[] tempProfile;
        String profile;
        BufferedReader br = null;
        
        try
        {
            br = new BufferedReader(new FileReader(profilePath));
            String info = br.readLine();
            while(info != null)
            {
                tempProfile = null;
                tempProfile = info.split(",");
                if(tempProfile[0].equals(user))
                {
                    result = "true,"+info;
                }
                info = br.readLine();
            }
            if(result.equals(""))
            {
                result = "false,no profile found for that user";
            }
        }
        finally
        {
            br.close();
            return result;
        }
    }
}

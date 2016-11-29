/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Credentials {
    String username;
    String password;
    
    public Credentials()
    {
        
    }
    public Credentials(String user, String pass)
    {
        username = user;
        password = pass;
    }
    
    public String verifyCredentials(String credentialPath, String credIn) throws FileNotFoundException, IOException
    {
        String result = null;
        String[] credentials = credIn.split(",");
        String newUser = credentials[0];
        String newPass = credentials[1];
        String csvUser;
        String csvPass;
        BufferedReader br = null;
        String csvCredPair;
        
        String delimeter = ",";
        
        try
        {
            br = new BufferedReader(new FileReader(credentialPath));
            csvCredPair = br.readLine();
            while(csvCredPair != null)
            {
                credentials = null;
                credentials = csvCredPair.split(delimeter);
                csvUser = credentials[0];
                csvPass = credentials[1];
                if(csvUser.equals(newUser))
                {
                    if(csvPass.equals(newPass))
                    {
                        result = "true,verified";
                    }
                    else
                    {
                        result = "false,incorrect password";
                    }
                }
                csvCredPair = br.readLine();
            }
            if(result.equals(null))
            {
                result = "error,incorrect user";
            }
        }
        catch(FileNotFoundException e)
        {}
        catch(IOException e)
        {}
        finally
        {
            br.close();
            return result;
        }
    }
}

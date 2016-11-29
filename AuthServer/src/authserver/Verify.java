/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class Verify 
{
    String result = "";
    String credentialsFilePath = "null";
    Credentials cred;
    public Verify(String credPath)
    {
        credentialsFilePath = credPath;
        System.out.println("Auth Server Started.");
        
        try
        {
            ServerSocket s = new ServerSocket(8189); //the server socket
            boolean over = false;
            while(!over) //put in a loop that keeps running
            {
                Socket incoming = s.accept(); //accept a connection from a client
                try
                {
                    InputStream inStream = incoming.getInputStream(); // the INPUT stream handler
                    OutputStream outStream = incoming.getOutputStream(); // the OUTPUT stream handler
                    Scanner in = new Scanner(inStream); //setup of input
                    PrintWriter out = new PrintWriter(outStream,true); // sends output
                    String junk = in.nextLine();
                    String credentialPair = in.nextLine();
                    System.out.println("Verify request: " + credentialPair.trim());
                    cred = new Credentials();
                    result = cred.verifyCredentials(credentialsFilePath, credentialPair);
                    System.out.println("Result: " + result);
                    out.println(result);
                }
                catch(Exception exc1)
                {
                    exc1.printStackTrace();
                }
            }
        }
        catch(Exception exc2)
        {
            exc2.printStackTrace();
        }
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server 
{
    
    public Server()
    {
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
                   
                    boolean done = false;
                    System.out.println("Connection made");
                    out.println("SERVER: Connected");
                    
                    while (!done && in.hasNextLine()) //while there are lines to read, for this connection
                    {
                        String lineIn = in.nextLine();
                        System.out.println(lineIn.trim());
                        if (lineIn.trim().equals("BYE")) //to kill the server, enter "BYE" from the client
                        {
                            done = true;
                        }
                    }
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


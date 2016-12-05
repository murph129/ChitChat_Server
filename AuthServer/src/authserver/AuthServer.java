/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authserver;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author Steve
 */
public class AuthServer {

    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        String credPath = path+ "\\cred.csv";
        System.out.println(credPath);
        Verify s = new Verify(credPath);
    }
    
}

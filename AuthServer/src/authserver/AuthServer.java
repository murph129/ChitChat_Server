/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authserver;

import java.util.Scanner;

/**
 *
 * @author Steve
 */
public class AuthServer {

    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter Credential CSV filepath: ");
        String credPath = in.nextLine();
        Verify s = new Verify(credPath);
    }
    
}

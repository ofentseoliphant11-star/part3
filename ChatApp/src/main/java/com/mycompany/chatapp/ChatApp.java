package com.mycompany.chatapp;

import java.util.Scanner;

public class ChatApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("**********************************************");
        System.out.println("*WELCOME TO THE LOGIN AND REGISTRATION SYSTEM*");
        System.out.println("**********************************************");

        System.out.print("Enter first name: ");
        String firstname = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastname = scanner.nextLine();

        String Username;
        String Password;
        String Cellphone;

        // USERNAME VALIDATION
        while (true) {
            System.out.print("Enter Username: ");
            Username = scanner.nextLine();

            Register temp = new Register(firstname, lastname, Username, "temp", "temp");

            if (temp.checkUsername()) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username must contain an underscore and be no more than 10 characters.");
            }
        }

        // PASSWORD VALIDATION
        while (true) {
            System.out.print("Enter Password: ");
            Password = scanner.nextLine();

            Register temp = new Register(firstname, lastname, Username, Password, "temp");

            if (temp.checkPasswordComplexity()) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password must be at least 8 characters, contain a capital letter, a number, and a special character.");
            }
        }

        // CELLPHONE VALIDATION
        while (true) {
            System.out.print("Enter Cellphone (+27XXXXXXXXX): ");
            Cellphone = scanner.nextLine();

            Register temp = new Register(firstname, lastname, Username, Password, Cellphone);

            if (temp.checkCellphoneNumber()) {
                System.out.println("Cellphone number added.");
                break;
            } else {
                System.out.println("Invalid cellphone format. Use +27 followed by 9 digits.");
            }
        }

        Register user = new Register(firstname, lastname, Username, Password, Cellphone);
        String result = user.registerUser();
        System.out.println(result);

        if (!result.equals("User successfully registered!")) {
            System.out.println("Registration failed.");
            return;
        }

        System.out.println("\n******** LOGIN ********");

        Login login = new Login(user);

        int attempts = 0;
        boolean loggedIn = false;

        while (attempts < 3 && !loggedIn) {
            System.out.print("Username: ");
            String u = scanner.nextLine();

            System.out.print("Password: ");
            String p = scanner.nextLine();

            String status = login.returnLoginStatus(u, p);
            System.out.println(status);

            if (status.contains("Welcome")) {
                loggedIn = true;
            } else {
                attempts++;
                if (attempts < 3) {
                    System.out.println("Attempts remaining: " + (3 - attempts));
                }
            }
        }

        if (loggedIn) {
            // REQUIREMENT 1 & 2: Only send messages if logged in, show welcome message
            System.out.println("\n✅ Login successful!");
            System.out.println("Welcome to QuickChat.\n");
            
            // Start the chat system
            WelcomeToQuickChat.startChat();
            
        } else {
            System.out.println("\n❌ Too many failed attempts. Exiting.");
        }

        scanner.close();
    }
}
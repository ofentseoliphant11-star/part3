package com.mycompany.chatapp;

import java.util.Scanner;
import java.util.ArrayList;

public class WelcomeToQuickChat {

    public static void startChat() {

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println("\n=================================");
            System.out.println("         QUICKCHAT MENU");
            System.out.println("=================================");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Stored Messages");
            System.out.println("4. Quit");
            
            System.out.println("=================================");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

        switch (choice) {

    case "1":
        System.out.print("How many messages do you want to send? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(scanner.nextLine());
            if (numMessages <= 0) {
                System.out.println("Please enter a positive number.");
                break;
            }
            sendMessages(scanner, numMessages);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
        break;

    case "2":
        // Show recently sent messages
        if (Message.getSentMessages().isEmpty()) {
            System.out.println("\nNo messages sent yet.");
        } else {
            System.out.println("\n=== RECENTLY SENT MESSAGES ===");
            for (String m : Message.getSentMessages()) {
                System.out.println(m);
            }
        }
        break;

    case "3":
        Message.loadStoredMessages();
        showStoredMessagesMenu(scanner);
        break;

    case "4":
        System.out.println("\nThank you for using QuickChat!");
        running = false;
        break;

    default:
        System.out.println("\nInvalid option. Please choose 1, 2, 3, or 4.");
}
        }
    }

    private static void sendMessages(Scanner scanner, int numMessages) {

        int messagesSent = 0;

        for (int i = 0; i < numMessages; i++) {

            System.out.println("\n=================================");
            System.out.println("        MESSAGE " + (i + 1) + " OF " + numMessages);
            System.out.println("=================================");

            String recipient;

            while (true) {

                System.out.print("Recipient cell number (+27XXXXXXXXX): ");
                recipient = scanner.nextLine();

                if (recipient.startsWith("+") && recipient.length() <= 12) {

                    System.out.println("Cell number accepted.");
                    break;

                } else {

                    System.out.println("Invalid cell number.");
                }
            }

            String messageText;

            while (true) {

                System.out.print("Message (max 250 chars): ");
                messageText = scanner.nextLine();

                if (messageText.length() > 250) {

                    System.out.println("Please enter a message of less than 250 characters.");

                } else {

                    System.out.println("Message received.");
                    break;
                }
            }

            Message msg = new Message(i + 1, recipient, messageText);

            msg.displayMessageDetails();

            boolean validChoice = false;

            while (!validChoice) {

                System.out.println("\nWhat would you like to do?");
                System.out.println("1) Send Message");
                System.out.println("2) Discard Message");
                System.out.println("3) Store Message to send later");
                System.out.print("Your choice: ");

                String action = scanner.nextLine();

                switch (action) {

                    case "1":

                        String result = msg.sendMessage();

                        System.out.println("\n" + result);

                        if (result.equals("Message successfully sent.")) {
                            messagesSent++;
                        }

                        validChoice = true;

                        break;

                    case "2":
                        msg.discard();
                        System.out.println("\nMessage discarded.");
                        validChoice = true;
                        break;

                    case "3":

                        msg.storeToFile();

                        System.out.println("\nMessage successfully stored.");

                        validChoice = true;

                        break;

                    default:

                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        }

        System.out.println("\n=================================");
        System.out.println("         SESSION SUMMARY");
        System.out.println("=================================");
        System.out.println("Total messages sent: " + messagesSent);
        System.out.println("=================================\n");
    }
    
    private static void showStoredMessagesMenu(Scanner scanner) {
    ArrayList<String> stored = Message.getStoredMessages();

    System.out.println("\n=================================");
    System.out.println("      STORED MESSAGES MENU");
    System.out.println("=================================");
    System.out.println("a) Sender & recipient of all stored");
    System.out.println("b) Longest stored message");
    System.out.println("c) Search by Message ID");
    System.out.println("d) Search by recipient");
    System.out.println("e) Delete by message hash");
    System.out.println("f) Full report");
    System.out.print("Choose: ");

    String choice = scanner.nextLine().toLowerCase();

    switch (choice) {
        case "a":
            if (stored.isEmpty()) { System.out.println("No stored messages."); break; }
            for (String m : stored) System.out.println(m);
            break;

        case "b":
            String longest = "";
            for (String m : stored)
                if (m.length() > longest.length()) longest = m;
            System.out.println(longest.isEmpty() ? "No messages." : "Longest: " + longest);
            break;

        case "c":
            System.out.print("Enter Message ID: ");
            String id = scanner.nextLine();
            boolean foundID = false;
            for (String m : stored)
                if (m.contains("ID: " + id)) { System.out.println(m); foundID = true; }
            if (!foundID) System.out.println("Message ID not found.");
            break;

        case "d":
            System.out.print("Enter recipient number: ");
            String recip = scanner.nextLine();
            boolean foundR = false;
            for (String m : stored)
                if (m.contains("To: " + recip)) { System.out.println(m); foundR = true; }
            if (!foundR) System.out.println("No messages for that recipient.");
            break;

           case "e":
    System.out.print("Enter hash to delete: ");
    String hash = scanner.nextLine();
    boolean removed = stored.removeIf(m -> m.contains("Hash: " + hash));
    if (removed) {
        rewriteStoredMessagesFile(stored);
        System.out.println("Message successfully deleted.");
    } else {
        System.out.println("Hash not found.");
    }
    break;

        case "f":
            System.out.println("\n=== FULL STORED MESSAGE REPORT ===");
            if (stored.isEmpty()) { System.out.println("No stored messages."); break; }
            for (String m : stored) System.out.println(m);
            break;

        default:
            System.out.println("Invalid choice.");
    }
}
    
    private static void rewriteStoredMessagesFile(ArrayList<String> stored) {
    try (java.io.FileWriter fw = new java.io.FileWriter("stored_messages.json", false)) {
        for (String entry : stored) {
            // Parse each field back out and rewrite as JSON
            String id      = extractBetween(entry, "ID: ",    " | Hash:");
            String hash    = extractBetween(entry, "Hash: ",  " | To:");
            String to      = extractBetween(entry, "To: ",    " | Msg:");
            String msg     = extractBetween(entry, "Msg: ",   null);
            fw.write("{\n");
            fw.write("  \"messageID\": \"" + id   + "\",\n");
            fw.write("  \"hash\": \""      + hash + "\",\n");
            fw.write("  \"recipient\": \"" + to   + "\",\n");
            fw.write("  \"message\": \""   + msg  + "\"\n");
            fw.write("},\n");
        }
    } catch (Exception e) {
        System.out.println("Could not update file: " + e.getMessage());
    }
}

private static String extractBetween(String text, String from, String to) {
    int start = text.indexOf(from);
    if (start == -1) return "";
    start += from.length();
    if (to == null) return text.substring(start).trim();
    int end = text.indexOf(to, start);
    return end == -1 ? text.substring(start).trim() : text.substring(start, end).trim();
}
}
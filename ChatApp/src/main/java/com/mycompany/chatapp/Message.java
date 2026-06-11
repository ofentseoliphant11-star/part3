package com.mycompany.chatapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Message {
    
    // Static variables shared across all messages
    private static int totalMessagesSent = 0;
    
    private static ArrayList<String> allSentMessages = new ArrayList<>();
    private static ArrayList<String> sentMessages = new ArrayList<>();
private static ArrayList<String> disregardedMessages = new ArrayList<>();
private static ArrayList<String> storedMessages = new ArrayList<>();
private static ArrayList<String> messageHashes = new ArrayList<>();
private static ArrayList<String> messageIDs = new ArrayList<>();

    // Instance variables for each message
    private int messageNumber;      // REQUIREMENT: Num messages sent (auto-incremented)
    private String recipient;        // REQUIREMENT: Recipient cell number
    private String messageText;      // REQUIREMENT: Message content
    private String messageID;        // REQUIREMENT: Unique 10-digit tracking ID
    private String messageHash;      // REQUIREMENT: Auto-generated hash
    private String timestamp;
    
    // Constructor
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.messageID = generateID();  // REQUIREMENT: Random 10-digit number
        this.messageHash = createMessageHash();  // REQUIREMENT: Auto-generate hash
    }
    
    // REQUIREMENT: Generate random 10-digit number for tracking
    private String generateID() {
        long id = (long)(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }
    
    // REQUIREMENT: Auto-generate Message Hash
    // Format: first 2 digits of ID : message number : first word + last word (ALL CAPS)
    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) {
            return messageID.substring(0, 2) + ":" + messageNumber + ":EMPTY";
        }
        
        String idStart = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        
        // Example: "48:1:HITHANKS" in ALL CAPS
        return (idStart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }
    
    // Validate recipient cell number
    public boolean checkRecipientCell() {
        return recipient != null && recipient.startsWith("+") && recipient.length() <= 12;
    }
    
    // REQUIREMENT: Send message with validation
    public String sendMessage() {
        // Validate recipient
        if (!checkRecipientCell()) {
            return "Invalid cell number. Must start with + and be no more than 12 characters.";
        }
        
        // Validate message length
        if (messageText == null || messageText.length() > 250) {
            return "Please enter a message of less than 250 characters.";
        }
        
        // REQUIREMENT: Increment counter and add to list
        totalMessagesSent++;
        sentMessages.add("To: " + recipient + " | " + messageText);
messageHashes.add(messageHash);
messageIDs.add(messageID);

        allSentMessages.add(
            "ID: " + messageID + 
            " | Hash: " + messageHash + 
            " | To: " + recipient + 
            " | Msg: " + messageText
        );
        
        return "Message successfully sent.";  // REQUIREMENT: Success message
    }
    
    // REQUIREMENT: Store message in JSON file
    public void storeToFile() { 
        storedMessages.add("ID: " + messageID + " | Hash: " + messageHash + 
                   " | To: " + recipient + " | Msg: " + messageText);
messageHashes.add(messageHash);
messageIDs.add(messageID);

        String json = "{\n" +
            "  \"messageID\": \"" + messageID + "\",\n" +
            "  \"messageNumber\": " + messageNumber + ",\n" +
            "  \"recipient\": \"" + recipient + "\",\n" +
            "  \"message\": \"" + messageText + "\",\n" +
            "  \"hash\": \"" + messageHash + "\",\n" +
            "  \"timestamp\": \"" + timestamp + "\"\n" +
            "},\n";
        
        try (FileWriter fw = new FileWriter("stored_messages.json", true)) {
            fw.write(json);
        } catch (IOException e) {
            System.out.println("Could not save: " + e.getMessage());
        }
    }
    public void discard() {
    disregardedMessages.add("To: " + recipient + " | " + messageText);
}
    
    
    // REQUIREMENT 7: Display all message details in specified order
    public void displayMessageDetails() {
        System.out.println("\n--- MESSAGE DETAILS ---");
        System.out.println("Message ID   : " + messageID);      // 1st: Message ID
        System.out.println("Message Hash : " + messageHash);    // 2nd: Message Hash
        System.out.println("Recipient    : " + recipient);      // 3rd: Recipient
        System.out.println("Message      : " + messageText);    // 4th: Message
    }
    
    // Get total messages sent (REQUIREMENT 6)
    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }
    
    // Getters
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    
    public static void loadStoredMessages() {
    storedMessages.clear();
    try {
        java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.FileReader("stored_messages.json"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        String[] blocks = sb.toString().split("},");
        for (String block : blocks) {
            if (block.trim().isEmpty()) continue;
            String rec = extractField(block, "recipient");
            String msg = extractField(block, "message");
            String hsh = extractField(block, "hash");
            String id  = extractField(block, "messageID");
            if (rec != null)
                storedMessages.add("ID: " + id + " | Hash: " + hsh +
                                   " | To: " + rec + " | Msg: " + msg);
        }
    } catch (Exception e) {
        System.out.println("No stored messages file found.");
    }
}

private static String extractField(String block, String key) {
    String search = "\"" + key + "\": \"";
    int start = block.indexOf(search);
    if (start == -1) return null;
    start += search.length();
    int end = block.indexOf("\"", start);
    return end == -1 ? null : block.substring(start, end);
}

public static ArrayList<String> getSentMessages()        { return sentMessages; }
public static ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
public static ArrayList<String> getStoredMessages()      { return storedMessages; }
public static ArrayList<String> getMessageHashes()       { return messageHashes; }
public static ArrayList<String> getMessageIDs()          { return messageIDs; }


public static void resetArrays() {
    sentMessages.clear();
    disregardedMessages.clear();
    storedMessages.clear();
    messageHashes.clear();
    messageIDs.clear();
}
}


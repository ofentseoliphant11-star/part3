package com.mycompany.chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    Message msg1, msg2, msg3, msg4, msg5;

    @BeforeEach                   // ← this runs BEFORE every single @Test
    public void setUp() {
        
        // STEP 1: Clear all arrays so tests don't affect each other
        Message.resetArrays();

        // STEP 2: Create the 5 test messages from the rubric
        msg1 = new Message(1, "+27834557896", "Did you get the cake?");
        msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg3 = new Message(3, "+27834484567", "Yohoooo, I am at your gate.");
        msg4 = new Message(4, "0838884567",   "It is dinner time!");
        msg5 = new Message(5, "+27838884567", "Ok, I am leaving without you.");

        // STEP 3: Apply the flags from the rubric
        msg1.sendMessage();   // Sent
        msg2.storeToFile();   // Stored
        msg3.discard();       // Disregarded
        msg4.sendMessage();   // Sent (will fail - invalid recipient)
        msg5.storeToFile();   // Stored
    }

    // ── TEST 1: Sent array populated ──────────────────────────────────────
    @Test
    public void testSentArrayContainsMsg1() {
        assertTrue(Message.getSentMessages().stream()
            .anyMatch(m -> m.contains("Did you get the cake?")));
    }

    @Test
    public void testInvalidRecipientNotInSentArray() {
        assertFalse(Message.getSentMessages().stream()
            .anyMatch(m -> m.contains("0838884567")));
    }

    // ── TEST 2: Longest message ───────────────────────────────────────────
    @Test
    public void testLongestMessage() {
        String longest = "";
        for (String m : Message.getStoredMessages()) {
            if (m.length() > longest.length()) longest = m;
        }
        assertTrue(longest.contains("Where are you? You are late!"));
    }

    // ── TEST 3: Search by Message ID ─────────────────────────────────────
    @Test
    public void testMessageIDNotNull() {
        assertNotNull(msg4.getMessageID());
    }

    @Test
    public void testMessageIDIs10Digits() {
        assertEquals(10, msg4.getMessageID().length());
    }

    // ── TEST 4: Search by recipient ───────────────────────────────────────
    @Test
    public void testSearchRecipientFindsMsg2() {
        assertTrue(Message.getStoredMessages().stream()
            .anyMatch(m -> m.contains("+27838884567") &&
                          m.contains("Where are you? You are late!")));
    }

    @Test
    public void testSearchRecipientFindsMsg5() {
        assertTrue(Message.getStoredMessages().stream()
            .anyMatch(m -> m.contains("+27838884567") &&
                          m.contains("Ok, I am leaving without you.")));
    }

    // ── TEST 5: Delete by message hash ────────────────────────────────────
    @Test
    public void testDeleteByHashRemovesMessage() {
        String hash = msg2.getMessageHash();
        boolean removed = Message.getStoredMessages()
            .removeIf(m -> m.contains("Hash: " + hash));
        assertTrue(removed);
    }

    @Test
    public void testDeletedMessageGone() {
        String hash = msg2.getMessageHash();
        Message.getStoredMessages()
            .removeIf(m -> m.contains("Hash: " + hash));
        assertFalse(Message.getStoredMessages().stream()
            .anyMatch(m -> m.contains("Where are you? You are late!")));
    }

    // ── TEST 6: Report contains correct fields ────────────────────────────
    @Test
    public void testReportHasHash() {
        for (String m : Message.getStoredMessages())
            assertTrue(m.contains("Hash:"));
    }

    @Test
    public void testReportHasRecipient() {
        for (String m : Message.getStoredMessages())
            assertTrue(m.contains("To:"));
    }

    @Test
    public void testReportHasMessage() {
        for (String m : Message.getStoredMessages())
            assertTrue(m.contains("Msg:"));
    }

    // ── EXTRA: Validation tests ───────────────────────────────────────────
    @Test
    public void testHashHasThreeParts() {
        assertEquals(3, msg1.getMessageHash().split(":").length);
    }

    @Test
    public void testInvalidRecipientFails() {
        assertFalse(msg4.checkRecipientCell());
    }

    @Test
    public void testValidRecipientPasses() {
        assertTrue(msg1.checkRecipientCell());
    }
}
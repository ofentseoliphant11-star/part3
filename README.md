# QuickChat Application

## Project Overview
QuickChat is a Java console-based messaging application developed as part of a Programming course. The application allows users to register, log in, and send, store, or discard messages. It includes full input validation, message tracking, and a stored messages management system.

---

## Developer Information
- **Module:** Applications Development
- **Institution:** The Independent Institute of Education (IIE)
- **Year:** 2026

---

## Features

### Part 1 — Registration & Login
- User registration with validation:
  - Username must contain an underscore and be no more than 10 characters
  - Password must be at least 8 characters, contain a capital letter, a number, and a special character
  - Cellphone number must follow the format `+27XXXXXXXXX`
- Login system with a maximum of 3 attempts
- Singleton `User` class to store session credentials

### Part 2 — Messaging
- Send messages to recipients with cell number validation
- Auto-generated unique 10-digit Message ID
- Auto-generated Message Hash in format `XX:N:FIRSTWORDLASTWORD`
- Three message actions:
  - **Send** — sends the message and adds it to the sent array
  - **Discard** — discards the message and adds it to the disregarded array
  - **Store** — saves the message to a JSON file and stored array
- Session summary showing total messages sent

### Part 3 — Store Data & Display Report
- Five populated arrays:
  - Sent Messages
  - Disregarded Messages
  - Stored Messages (loaded from JSON file)
  - Message Hashes
  - Message IDs
- Stored Messages Menu with the following options:
  - Display sender and recipient of all stored messages
  - Display the longest stored message
  - Search for a message by Message ID
  - Search all messages for a particular recipient
  - Delete a message using its hash (removes from memory and JSON file)
  - Display a full report of all stored messages

---

## Project Structure

```
ChatApp/
├── src/
│   ├── main/
│   │   └── java/com/mycompany/chatapp/
│   │       ├── ChatApp.java              # Entry point, registration & login
│   │       ├── Register.java             # Registration logic & validation
│   │       ├── Login.java                # Login logic
│   │       ├── User.java                 # Singleton user session
│   │       ├── Message.java              # Message creation, sending, storing
│   │       └── WelcomeToQuickChat.java   # Main chat menu & stored messages menu
│   └── test/
│       └── java/com/mycompany/chatapp/
│           ├── ChatAppTest.java          # General app tests
│           └── MessageTest.java          # Full message unit tests
├── .github/
│   └── workflows/
│       └── TestJava.yml                  # GitHub Actions CI pipeline
├── stored_messages.json                  # Persistent message storage
├── pom.xml                               # Maven build configuration
└── README.md
```

---

## How to Run

### Prerequisites
- Java JDK 17 or higher
- Apache Maven
- NetBeans IDE (recommended) or any Java IDE

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/ofentseoliphant11/ChatApp.git
   ```
2. Open the project in NetBeans
3. Right-click the project → **Run**

Or via terminal:
```bash
mvn clean install
java -cp target/ChatApp-1.0-SNAPSHOT.jar com.mycompany.chatapp.ChatApp
```

---

## Running Tests

```bash
mvn test
```

Or in NetBeans, right-click `MessageTest.java` → **Run File**

---

## Test Data Used

| # | Recipient | Message | Flag |
|---|---|---|---|
| 1 | +27834557896 | Did you get the cake? | Sent |
| 2 | +27838884567 | Where are you? You are late! I have asked you to be on time. | Stored |
| 3 | +27834484567 | Yohoooo, I am at your gate. | Disregarded |
| 4 | 0838884567 | It is dinner time! | Sent (invalid recipient) |
| 5 | +27838884567 | Ok, I am leaving without you. | Stored |

---

## Unit Tests Coverage

| Test | What it verifies |
|---|---|
| `testSentArrayContainsMsg1` | Sent array is correctly populated |
| `testInvalidRecipientNotInSentArray` | Invalid recipients are rejected |
| `testLongestMessage` | Correctly identifies the longest message |
| `testMessageIDIs10Digits` | Message ID is exactly 10 digits |
| `testSearchRecipientFindsMsg2` | Recipient search returns correct messages |
| `testSearchRecipientFindsMsg5` | Recipient search finds all matches |
| `testDeleteByHashRemovesMessage` | Delete by hash works correctly |
| `testDeletedMessageGone` | Deleted message no longer exists in array |
| `testReportHasHash` | Report contains message hash |
| `testReportHasRecipient` | Report contains recipient |
| `testReportHasMessage` | Report contains message text |
| `testHashHasThreeParts` | Hash format is correct (XX:N:WORD) |
| `testInvalidRecipientFails` | Invalid cell number fails validation |
| `testValidRecipientPasses` | Valid cell number passes validation |

---

## CI/CD Pipeline

This project uses **GitHub Actions** to automatically build and test on every push.

The workflow file is located at `.github/workflows/TestJava.yml` and runs:
- `mvn clean install` on `ubuntu-latest`
- Uses Java 17 (Temurin distribution)

---

## Dependencies

| Dependency | Version | Purpose |
|---|---|---|
| `junit-jupiter-api` | 5.10.0 | Unit testing framework |
| `junit-jupiter-engine` | 5.10.0 | JUnit 5 test runner |
| `maven-surefire-plugin` | 3.1.2 | Maven test execution |

---

## Message Hash Format

Hashes are auto-generated in the following format:
```
XX:N:FIRSTWORDLASTWORD
```
- `XX` — first 2 digits of the Message ID
- `N` — message number
- `FIRSTWORDLASTWORD` — first and last word of the message in uppercase

**Example:** For message "Did you get the cake?" with ID starting `48`, hash number 1:
```
48:1:DIDCAKE?
```

---

## Known Limitations
- Stored messages JSON file uses manual parsing (no external JSON library)
- Application is console-based only (no GUI)
- Data is not encrypted

---

## License
This project was created for educational purposes at The IIE. Not for commercial use.

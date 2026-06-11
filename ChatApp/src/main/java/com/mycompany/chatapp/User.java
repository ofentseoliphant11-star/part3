package com.mycompany.chatapp;

public class User {
    
    private static User instance;
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private User() {}

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean checkCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
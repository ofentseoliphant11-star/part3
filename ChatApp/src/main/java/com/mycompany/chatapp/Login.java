package com.mycompany.chatapp;

public class Login {

    private Register registeredUser;

    public Login(Register registeredUser) {
        this.registeredUser = registeredUser;
    }

    public boolean loginUser(String username, String password) {
        return registeredUser.getUsername().equals(username) &&
               registeredUser.getPassword().equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + registeredUser.getFirstName() + " " +
                   registeredUser.getLastName() + "!";
        } else {
            return "Username or password incorrect.";
        }
    }
}
package com.mycompany.chatapp;

public class Register {
    
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellphone;
    
    public Register(String firstName, String lastName, String username, String password, String cellphone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellphone = cellphone;
    }
    
    public boolean checkUsername() {
        return username.contains("_") && username.length() <= 10;
    }
    
    public boolean checkPasswordComplexity() {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }
    
    public boolean checkCellphoneNumber() {
        return cellphone != null && cellphone.matches("^\\+27\\d{9}$");
    }
    
    public String registerUser() {
        if (checkUsername() && checkPasswordComplexity() && checkCellphoneNumber()) {
            User.getInstance().setCredentials(username, password);
            User.getInstance().setFirstName(firstName);
            User.getInstance().setLastName(lastName);
            return "User successfully registered!";
        } else {
            return "Registration failed. Please check your details.";
        }
    }
    
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCellphone() { return cellphone; }
}
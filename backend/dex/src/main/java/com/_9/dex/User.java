package com._9.dex;

import org.springframework.security.core.parameters.P;

public class User {
    private Integer id = null;
    private String username;
    private String password;
    private String email;
    private String role;
    private String securityQuestion;
    private String securityAnswer;

    public User(){

    }
    // Password must be hashed when creating a new user object.
    public User(String username, String password, String email, String role, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    public User(int id, String username, String password, String email, String role, String securityQuestion, String securityAnswer) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;

    }

    public int getID() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getRole() {
        return this.role;
    }

    public String getSecurityQuestion() {
        return this.securityQuestion;
    }

    public boolean validateSecurityAnswer(String answer) {
        if (answer.equals(this.securityAnswer)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validatePassword(String password) {
        PasswordHasher passwordHasher = new PasswordHasher(password);
        String hashedPassword = passwordHasher.hash();

        if (this.password.equals(hashedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getSecurityAnswer() {
        return this.securityAnswer;
    }
}

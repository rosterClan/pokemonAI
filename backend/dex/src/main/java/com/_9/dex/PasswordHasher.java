package com._9.dex;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    private String password;

    public PasswordHasher(String password) {
        this.password = password;
    }

    public String hash() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(this.password);
    }

    public boolean validate(String hash) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(this.password, hash);
    }
}

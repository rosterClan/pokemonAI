package com._9.dex;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

  @Test
  public void testUserParameterizedConstructor() {
    // Smoke test for the User constructor with parameters
    User user = new User("testUsername", "testPassword", "test@domain.com", "1", "What is your pet's name?", "Fluffy");
  }

  @Test
  public void testUserDefaultConstructor() {
    // Smoke test for the default User constructor
    User user = new User();
  }

  @Test
  public void testValidatePassword() {
    // Smoke test for validatePassword method
    User user = new User("testUsername", "testPassword", "test@domain.com", "1", "What is your pet's name?", "Fluffy");
    user.validatePassword("testPassword");  // Valid password
    user.validatePassword("wrongPassword");  // Invalid password
  }

  @Test
  public void testValidateSecurityAnswer() {
    // Smoke test for validateSecurityAnswer method
    User user = new User("testUsername", "testPassword", "test@domain.com", "1", "What is your pet's name?", "Fluffy");
    user.validateSecurityAnswer("Fluffy");  // Correct security answer
    user.validateSecurityAnswer("WrongAnswer");  // Incorrect security answer
  }
}

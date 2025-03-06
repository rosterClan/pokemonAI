package com._9.dex;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class TokenTest {

  @Test
  public void testTokenConstructor() {
    // Smoke test for the Token constructor
    Token token = new Token(1, 1, "session-token", new Date(), true);
  }

  @Test
  public void testGetSessionID() {
    // Smoke test for getSessionID method
    Token token = new Token(1, 1, "session-token", new Date(), true);
    token.getSessionID();
  }

  @Test
  public void testGetUserID() {
    // Smoke test for getUserID method
    Token token = new Token(1, 1, "session-token", new Date(), true);
    token.getUserID();
  }

  @Test
  public void testGetSessionToken() {
    // Smoke test for getSessionToken method
    Token token = new Token(1, 1, "session-token", new Date(), true);
    token.getSessionToken();
  }

  @Test
  public void testGetIsActive() {
    // Smoke test for getIsActive method
    Token token = new Token(1, 1, "session-token", new Date(), true);
    token.getIsActive();
  }

  @Test
  public void testGetExpiryDate() {
    // Smoke test for getExpiryDate method
    Token token = new Token(1, 1, "session-token", new Date(), true);
    token.getExpiryDate();
  }
}

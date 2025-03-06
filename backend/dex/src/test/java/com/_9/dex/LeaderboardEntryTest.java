package com._9.dex;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LeaderboardEntryTest {

  @Test
  public void testLeaderboardEntryConstructorWithSighting() {
    // Smoke test for the LeaderboardEntry constructor with parameters
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 50.0, new java.sql.Date(System.currentTimeMillis()));
    LeaderboardEntry entry = new LeaderboardEntry("User1", 10, sighting);
  }

  @Test
  public void testLeaderboardEntryConstructorWithUserName() {
    // Smoke test for the LeaderboardEntry constructor with just the user name
    LeaderboardEntry entry = new LeaderboardEntry("User1");
  }

  @Test
  public void testSetUserName() {
    // Smoke test for the setUserName method
    LeaderboardEntry entry = new LeaderboardEntry("User1");
    entry.setUserName("NewUser");
  }

  @Test
  public void testSetNumSightings() {
    // Smoke test for the setNumSightings method
    LeaderboardEntry entry = new LeaderboardEntry("User1");
    entry.setNumSightings(5);
  }

  @Test
  public void testSetMostRecentSighting() {
    // Smoke test for the setMostRecentSighting method
    LeaderboardEntry entry = new LeaderboardEntry("User1");
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 50.0, new java.sql.Date(System.currentTimeMillis()));
    entry.setMostRecentSighting(sighting);
  }

  @Test
  public void testGetUserName() {
    // Smoke test for the getUserName method
    LeaderboardEntry entry = new LeaderboardEntry("User1");
    entry.getUserName();
  }

  @Test
  public void testGetNumSightings() {
    // Smoke test for the getNumSightings method
    LeaderboardEntry entry = new LeaderboardEntry("User1");
    entry.getNumSightings();
  }

  @Test
  public void testGetMostRecentSighting() {
    // Smoke test for the getMostRecentSighting method
    LeaderboardEntry entry = new LeaderboardEntry("User1");
    entry.getMostRecentSighting();
  }
}

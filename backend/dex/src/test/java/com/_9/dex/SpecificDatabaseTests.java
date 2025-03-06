package com._9.dex;

import com._9.dex.Database;
import com._9.dex.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpecificDatabaseTests {

  @Value("${database.filePath}")
  private String databaseFilePath;

  @Test
  public void testGetDexByUserID() {
    // Smoke test for getDexByUserID method
    Database database = new Database(databaseFilePath);
    database.getDexByUserID(1);  // Test for user with ID 1
    database.getDexByUserID(0);  // Test for edge case: non-existent user
  }

  @Test
  public void testGetSightingByID() {
    // Smoke test for getSightingByID method
    Database database = new Database(databaseFilePath);
    database.getSightingByID(1);  // Test for sighting with ID 1
    database.getSightingByID(0);  // Edge case: non-existent sighting
  }

  @Test
  public void testInsertDefaultValues() {
    // Smoke test for insertDefaultValues method
    Database database = new Database(databaseFilePath);
    database.insertDefaultValues();  // Insert default values into the database
  }

  @Test
  public void testGetPokemonWithIDAndName() {
    // Smoke test for getPokemon(Integer, String) method
    Database database = new Database(databaseFilePath);
    database.getPokemon(1, "Bulbasaur");  // Retrieve Pokémon by ID and name
    database.getPokemon(null, "Ivysaur");  // Test when only name is provided
    database.getPokemon(1, null);  // Test when only ID is provided
  }

  @Test
  public void testGetNewSession() {
    // Smoke test for getNewSession method
    Database database = new Database(databaseFilePath);
    database.getNewSession(1);  // Test session creation for User ID 1
  }

  @Test
  public void testUpdatePokemon() {
    // Smoke test for updatePokemon method
    Database database = new Database(databaseFilePath);
    Pokemon pokemon = new Pokemon(1, "Bulbasaur", 1, "Seed", 45, 1, "Tackle", 1, "Defense", "Grass/Poison");
    database.updatePokemon(1, pokemon);  // Update Pokémon with ID 1
  }

  @Test
  public void testValidateToken() {
    // Smoke test for validateToken method
    Database database = new Database(databaseFilePath);
    String token = "sample-token";
    database.validateToken(token);  // Validate the token
  }

  @Test
  public void testGetUserId() {
    // Smoke test for getUserId(String, String) method
    Database database = new Database(databaseFilePath);
    database.getUserId("testUsername", "testPassword");  // Validate credentials
  }

  @Test
  public void testGetUsersPokemonSightings() {
    // Smoke test for getUsersPokemonSightings method
    Database database = new Database(databaseFilePath);
    database.getUsersPokemonSightings(1);  // Get Pokémon sightings for user with ID 1
  }

  @Test
  public void testGetPokemonByIdOrName() {
    // Smoke test for getPokemon(int, String) method
    Database database = new Database(databaseFilePath);
    database.getPokemon(1, "Bulbasaur");  // Test retrieval by both ID and name
    database.getPokemon(2, null);  // Test retrieval by ID only
    database.getPokemon(0, "InvalidName");  // Test retrieval for non-existent Pokémon
  }
}

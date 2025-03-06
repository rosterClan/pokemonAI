package com._9.dex;

import com._9.dex.Database;
import com._9.dex.Pokemon;
import com._9.dex.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.UUID;

@SpringBootTest
public class DatabaseTest {


  private String databaseFilePath = "newdatabase.db";

  @Test
  public void testDatabaseInitialization() {
    // Smoke test for constructor
    Database database = new Database(databaseFilePath);
  }

  @Test
  public void testCreateTables() {
    // Smoke test for createTables method
    Database database = new Database(databaseFilePath);
    database.createTables();
  }

  @Test
  public void testInsertPokemon() {
    // Smoke test for insertPokemon method
    Database database = new Database(databaseFilePath);
    Pokemon pokemon = new Pokemon(1, "Bulbasaur", 1, "Seed", 45, 1, "Tackle", 1, "Defense", "Grass/Poison");
    database.insertPokemon(pokemon);
  }

  @Test
  public void testGetPokemon() {
    // Smoke test for getPokemon method by ID
    Database database = new Database(databaseFilePath);
    database.getPokemon(1, "Bulbasaur");
  }

  @Test
  public void testDeletePokemon() {
    // Smoke test for deletePokemon method
    Database database = new Database(databaseFilePath);
    database.deletePokemon(1);  // Attempt to delete Pokémon with ID 1
  }

  @Test
  public void testGetDexByUserID() {
    // Smoke test for getDexByUserID method
    Database database = new Database(databaseFilePath);
    database.getDexByUserID(1);  // Retrieve dex data for user with ID 1
  }

  @Test
  public void testInsertDefaultValues() {
    // Smoke test for insertDefaultValues method
    Database database = new Database(databaseFilePath);
    database.insertDefaultValues();
  }

  @Test
  public void testGetUserByEmailOrUsername() {
    // Smoke test for getUser method (by username or email)
    Database database = new Database(databaseFilePath);
    database.getUser("test@domain.com");
  }

  @Test
  public void testGetUserById() {
    // Smoke test for getUser method (by ID)
    Database database = new Database(databaseFilePath);
    database.getUser(1);  // Retrieve user with ID 1
  }

  @Test
  public void testValidateToken() {
    // Smoke test for validateToken method
    Database database = new Database(databaseFilePath);
    database.validateToken(UUID.randomUUID().toString());
  }

  @Test
  public void testInsertUser() {
    // Smoke test for insertUser method
    Database database = new Database(databaseFilePath);
    User user = new User(1, "username", "password", "test@domain.com", 1, "question", "answer");
    database.insertUser(user);
  }

  @Test
  public void testUpdateUser() {
    // Smoke test for updateUser method
    Database database = new Database(databaseFilePath);
    User user = new User(1, "usernameUpdated", "passwordUpdated", "updated@domain.com", 1, "questionUpdated", "answerUpdated");
    database.updateUser(1, user);
  }

  @Test
  public void testDeleteUser() {
    // Smoke test for deleteUser method
    Database database = new Database(databaseFilePath);
    database.deleteUser(1);  // Delete user with ID 1
  }

  @Test
  public void testInsertUserFollowing() {
    // Smoke test for insertUserFollowing method
    Database database = new Database(databaseFilePath);
    database.insertUserFollowing(1, 2);  // User 1 follows User 2
  }

  @Test
  public void testUpdatePassword() {
    // Smoke test for updatePassword method
    Database database = new Database(databaseFilePath);
    database.updatePassword(1, "newPassword");  // Update password for User ID 1
  }

  @Test
  public void testInsertPokemonSightingsData() {
    // Smoke test for insertPokemonSightingsData method
    Database database = new Database(databaseFilePath);
    database.insertPokemonSightingsData();
  }

  @Test
  public void testGetLeaderboard() {
    // Smoke test for getLeaderboard method
    Database database = new Database(databaseFilePath);
    database.getLeaderboard();
  }

  @Test
  public void testGetPokemonImageById() {
    // Smoke test for getPokemonImageById method
    Database database = new Database(databaseFilePath);
    database.getPokemonImageById(1);  // Retrieve the image for Pokémon with ID 1
  }

  @Test
  public void testGetSightingByID() {
    // Smoke test for getSightingByID method
    Database database = new Database(databaseFilePath);
    database.getSightingByID(1);  // Get sighting with ID 1
  }

  @Test
  public void testInsertPokemonData() {
    // Smoke test for insertPokemonData method
    Database database = new Database(databaseFilePath);
    database.insertPokemonData();
  }

  @Test
  public void testCloseSession() {
    // Smoke test for closeSession method
    Database database = new Database(databaseFilePath);
    database.closeSession(UUID.randomUUID().toString());  // Close a session with random token
  }

  @Test
  public void testGetConnection() {
    // Smoke test for getConn method
    Database database = new Database(databaseFilePath);
    database.getConn();  // Get the database connection
  }

}

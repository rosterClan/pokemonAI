package com._9.dex;

import com._9.dex.PokemonSighting;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
public class PokemonSightingTest {

  @Test
  public void testPokemonSightingConstructor() {
    // Smoke test for the PokemonSighting constructor
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
  }

  @Test
  public void testGetSightingID() {
    // Smoke test for getSightingID method
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
    sighting.getSightingID();
  }

  @Test
  public void testGetPokemonID() {
    // Smoke test for getPokemonID method
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
    sighting.getPokemonID();
  }

  @Test
  public void testGetUserID() {
    // Smoke test for getUserID method
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
    sighting.getUserID();
  }

  @Test
  public void testGetBiomeID() {
    // Smoke test for getBiomeID method
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
    sighting.getBiomeID();
  }

  @Test
  public void testGetLongitude() {
    // Smoke test for getLongitude method
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
    sighting.getLongitude();
  }

  @Test
  public void testGetLatitude() {
    // Smoke test for getLatitude method
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
    sighting.getLatitude();
  }

  @Test
  public void testGetFoundDate() {
    // Smoke test for getFoundDate method
    PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1, 50.0, 60.0, new Date(System.currentTimeMillis()));
    sighting.getFoundDate();
  }
}

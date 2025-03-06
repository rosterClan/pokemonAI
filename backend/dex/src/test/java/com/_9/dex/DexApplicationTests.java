package com._9.dex;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
class DexApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void createDB() {
		Database db = new Database("/mnt/c/Users/William/Desktop/UniFiles/USYD/2024-Semester2/Java Application Frameworks/Mon-09-12-Lab-Group-3-5/backend/dex/test.db");
		db.createTables();
		db.insertDefaultValues();
	}


//	@Test
//	void testGetUser() {
//		Database db = new Database("test.db");
//		User user = db.getUser("john");
//		assertEquals("john", user.getUsername());
//		assertEquals(1, user.getID());
//		assertEquals("john@example.com", user.getEmail());
//		assertEquals(1, user.getRole());
//		assertEquals("What is your favorite color?", user.getSecurityQuestion());
//		assertEquals("blue", user.getSecurityAnswer());
//		assertEquals("$2a$10$uTl0nSj5nMWrK9cbongEyuoIU005oFaQo7geChciN6IK2hYYNQCWa", user.getPassword());
//		assertEquals(true, user.validateSecurityAnswer("blue"));
//		assertEquals(false, user.validateSecurityAnswer("red"));
//		assertEquals(true, user.validatePassword("password"));
//		assertEquals(false, user.validatePassword("password1"));
//
//		user = db.getUser("john@example.com");
//		assertEquals("john", user.getUsername());
//		assertEquals(1, user.getID());
//		assertEquals("john@example.com", user.getEmail());
//		assertEquals(1, user.getRole());
//		assertEquals("What is your favorite color?", user.getSecurityQuestion());
//		assertEquals("blue", user.getSecurityAnswer());
//		assertEquals("$2a$10$uTl0nSj5nMWrK9cbongEyuoIU005oFaQo7geChciN6IK2hYYNQCWa", user.getPassword());
//		assertEquals(true, user.validateSecurityAnswer("blue"));
//		assertEquals(false, user.validateSecurityAnswer("red"));
//		assertEquals(true, user.validatePassword("password"));
//		assertEquals(false, user.validatePassword("password1"));
//
//		user = db.getUser(1);
//		assertEquals("john", user.getUsername());
//		assertEquals(1, user.getID());
//		assertEquals("john@example.com", user.getEmail());
//		assertEquals(1, user.getRole());
//		assertEquals("What is your favorite color?", user.getSecurityQuestion());
//		assertEquals("blue", user.getSecurityAnswer());
//		assertEquals("$2a$10$uTl0nSj5nMWrK9cbongEyuoIU005oFaQo7geChciN6IK2hYYNQCWa", user.getPassword());
//		assertEquals(true, user.validateSecurityAnswer("blue"));
//		assertEquals(false, user.validateSecurityAnswer("red"));
//		assertEquals(true, user.validatePassword("password"));
//		assertEquals(false, user.validatePassword("password1"));
//
//
//	}
//
//	@Test
//	void testInsertUser() {
//		Database db = new Database("test.db");
//		PasswordHasher hasher = new PasswordHasher("p2");
//		String hash = hasher.hash();
//		User userIn = new User("test", hash,"test@example.com",  1, "Test q", "Test a");
//
//		db.insertUser(userIn);
//
//		User userOut = db.getUser("test");
//		assertEquals("test", userOut.getUsername());
//		assertEquals(2, userOut.getID());
//		assertEquals("test@example.com", userOut.getEmail());
//		assertEquals(1, userOut.getRole());
//		assertEquals("Test q", userOut.getSecurityQuestion());
//		assertEquals("Test a", userOut.getSecurityAnswer());
//		assertEquals(hash, userOut.getPassword());
//		assertEquals(true, userOut.validateSecurityAnswer("Test a"));
//		assertEquals(false, userOut.validateSecurityAnswer("Test b"));
//		assertEquals(true, userOut.validatePassword("p2"));
//		assertEquals(false, userOut.validatePassword("password1"));
//	}
//
//	@Test
//	void testInsertUserFollowing() {
//		Database db = new Database("test.db");
//		db.insertUserFollowing(1,2);
//		assertEquals(true, db.getUserFollowing(1).contains(2));
//		assertEquals(true, db.getUserFollowedBy(2).contains(1));
//
//
//
//	}
//
//	@Test
//	public void testInsertPokemonAndSighting() {
//		Database db = new Database("test.db");
//		Pokemon pokemon = new Pokemon(1, "Bulbasaur", "Grass", "Seed", 45, "Special", "Vine Whip", "Special", "Razor Leaf", "Seed");
//		db.insertPokemon(pokemon);
//
//		Pokemon dbPokemon = db.getPokemon(1,"");
//		assertEquals(1, dbPokemon.getPokemonID());
//		assertEquals("Bulbasaur", dbPokemon.getPokemonName());
//		assertEquals("Grass", dbPokemon.getPlantType());
//		assertEquals("Seed", dbPokemon.getSpeciesName());
//		assertEquals(45, dbPokemon.getHP());
//		assertEquals("Special", dbPokemon.getAttackCategory());
//		assertEquals("Vine Whip", dbPokemon.getAttackDescription());
//		assertEquals("Special", dbPokemon.getDefenseCategory());
//		assertEquals("Razor Leaf", dbPokemon.getDefenseDescription());
//		assertEquals("Seed", dbPokemon.getTaxonomicCategory());
//
//		PokemonSighting sighting = new PokemonSighting(1, 1, 1, 1.0, 1.0, new java.sql.Date(2024, 16,9));
//		db.insertPokemonSighting(sighting);
//
//		List<PokemonSighting> sightings = db.getUsersPokemonSightings(1);
//		assertEquals(1, sightings.size());
//		assertEquals(1, sightings.get(0).getPokemonID());
//		assertEquals(1, sightings.get(0).getUserID());
//		assertEquals(1, sightings.get(0).getBiomeID());
//		assertEquals(1.0, sightings.get(0).getLongitude());
//		assertEquals(1.0, sightings.get(0).getLatitude());
//		assertEquals(new java.sql.Date(2024, 16,9), sightings.get(0).getFoundDate());
//
//
//	}

//	@Test
//	public void testRemoveUserFollowing() {
//		Database db = new Database("test.db");
//		db.removeUserFollowing(1,2);
//		assertEquals(false, db.getUserFollowing(1).contains(2));
//		assertEquals(false, db.getUserFollowedBy(2).contains(1));
//	}


}

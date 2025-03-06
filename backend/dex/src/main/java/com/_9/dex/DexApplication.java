package com._9.dex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.drew.imaging.ImageMetadataReader;



@SpringBootApplication
@RestController
@RequestMapping("/api")

public class DexApplication {
	private final Database database = new Database("test.db");


	public User getUserFromSession(JSONObject body) {
		if (!body.containsKey("token")) {
			return null; 
		}
		return this.database.validateToken((String) body.get("token")); 
	}


	@CrossOrigin(origins = "*")
	@PostMapping("/performScan")
	public ResponseEntity<String> performScan(@RequestBody JSONObject body) throws IOException {
		JSONObject json = new JSONObject();
		try {
			User releventUser = this.getUserFromSession(body); 
			if (releventUser == null) {
				json.put("error","token error");
				return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
			}
	
			ImageProcessing imageProcessing = new ImageProcessing(database);
			String species = ImageProcessing.imageSpeciesName((String) body.get("image"));
	
			int sightingID = -1; 
			int pokemonID = imageProcessing.generatePlantInformation((String) body.get("image"), species);
			if (pokemonID == -1) {
				json.put("error","Scan failed");
				return new ResponseEntity<>(json.toJSONString(), HttpStatus.NOT_FOUND);
			}
	
			//extract coordinates from image metadata
			String base64Image = ((String) body.get("image")).substring(((String) body.get("image")).indexOf(",") + 1);
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
	
			try {
				// Read metadata from image input stream
				Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
	
				// Iterate through metadata directories
				for (Directory directory : metadata.getDirectories()) {
					// Check if the directory contains GPS data
					if (directory instanceof GpsDirectory) {
						GpsDirectory gpsDirectory = (GpsDirectory) directory;
	
						// Extract GPS coordinates (latitude and longitude)
						if (gpsDirectory.getGeoLocation() != null) {
							double latitude = gpsDirectory.getGeoLocation().getLatitude();
							double longitude = gpsDirectory.getGeoLocation().getLongitude();
							PokemonSighting sighting = new PokemonSighting(0, pokemonID, releventUser.getID(), 0, longitude, latitude, new Timestamp(System.currentTimeMillis()));
							sightingID = database.insertPokemonSighting(sighting);
						} else {
							System.out.println("No GPS data found in the image.");
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Error reading metadata: " + e.getMessage());
			} finally {
				json.put("error", "entity generation error");
				inputStream.close();
			}
	
			json.put("sightingID", sightingID);
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Error in catch.");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.NOT_FOUND);
		}
		
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/getUserDex")
	public ResponseEntity<String> getUserDex(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		User releventUser = this.getUserFromSession(body); 
		if (releventUser == null) {
			json.put("error","token error");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}

		JSONArray dexPokemon = null;
		if (releventUser.getRole().equals("1")) {
			dexPokemon = this.database.getDexForAllUsers();
		} else {
			dexPokemon = this.database.getDexByUserID(releventUser.getID());
		}
		return new ResponseEntity<>(dexPokemon.toJSONString(), HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/getExternalUserDex")
	public ResponseEntity<String> getExternalUserDex(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		User releventUser = this.getUserFromSession(body); 
		if (releventUser == null) {
			json.put("error","token error");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}
		if (!body.containsKey("queryID")) {
			json.put("error","No queryID provided.");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}
		
		JSONArray dexPokemon = this.database.getDexByUserID(Integer.parseInt(String.valueOf(body.get("queryID"))));
		return new ResponseEntity<>(dexPokemon.toJSONString(), HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/getUserLog")
	public ResponseEntity<String> getUserLog(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		User releventUser = this.getUserFromSession(body); 
		if (releventUser == null) {
			json.put("error","token error");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}

		JSONArray dexPokemon = this.database.getLogByUserID(releventUser.getID());
		return new ResponseEntity<>(dexPokemon.toJSONString(), HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/getSightingByID")
	public ResponseEntity<String> getSightingByID(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		User releventUser = this.getUserFromSession(body); 
		if (releventUser == null) {
			json.put("error","token error");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}
		if (!body.containsKey("sightingID")) {
			json.put("error","No sightingID was provided.");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}
		JSONObject pokemon = this.database.getSightingByID(Integer.parseInt(((String)body.get("sightingID"))));
		return new ResponseEntity<>(pokemon.toJSONString(), HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();

		String username = (String) body.get("username");
		String password1 = (String) body.get("password1");
		String password2 = (String) body.get("password2");
		String email = (String) body.get("email");
		String securityQuestion = (String) body.get("securityQuestion");
		String securityAnswer = (String) body.get("securityAnswer");

		if (username == null || password1 == null || password2 == null || email == null || securityQuestion == null || securityAnswer == null) {
			json.put("error", "Missing required fields");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.BAD_REQUEST);
		}

		if (!password1.equals(password2)) {
			json.put("error","Passwords do not match");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.BAD_REQUEST);
		}

		User user = new User(username, password1, email, "user", securityQuestion, securityAnswer);

		if (database.getUser(username) != null) {
			json.put("error","Username already exists");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.BAD_REQUEST);
		} else {
			database.insertUser(user);
			User found_user = database.getUser(user.getUsername());
			if (found_user == null) {
				json.put("error","internal server error");
				return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
			}
			String key = database.getNewSession(found_user.getID());
			json.put("token",key);
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
		}
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		String username = (String) body.get("username");
		String password = (String) body.get("password");

		if (username == null || password == null) {
			json.put("error", "Missing required fields");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.BAD_REQUEST);
		}

		User user = database.getUser(username);
		if (user == null) {
			json.put("error", "User not found");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.NOT_FOUND);
		}

		if (!password.equals(user.getPassword())) {
			json.put("error", "Incorrect password");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}

		String sessionkey = database.getNewSession(user.getID());
		HttpCookie cookie = ResponseCookie.from("sessionkey", sessionkey).path("/").build();
		
		json.put("token", sessionkey);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(json.toJSONString());
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		User relevantUser = this.getUserFromSession(body); 
		if (relevantUser == null) {
			json.put("error","token error");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		} else {
			database.closeSession((String) body.get("token"));
			json.put("logout", "success");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
		}
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/leaderboard")
	public ResponseEntity<String> getLeaderboard(@RequestBody JSONObject body) {
		JSONArray convert = new JSONArray();
		List<LeaderboardEntry> test = database.getLeaderboard();

		for (int idx = 0; idx < test.size(); idx++) {
			LeaderboardEntry leaderboardEntry = test.get(idx);
			if (leaderboardEntry.getMostRecentSighting() == null) {
				continue; 
			}

			JSONObject new_obj = new JSONObject();
			new_obj.put("userName", leaderboardEntry.getUserName());
			new_obj.put("numSightings", String.valueOf(leaderboardEntry.getNumSightings()));
			new_obj.put("userID", leaderboardEntry.getUserID());
			convert.add(new_obj);
		}

		return new ResponseEntity<>(convert.toJSONString(), HttpStatus.OK);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/getUser")
	public ResponseEntity<String> getUserbyId(@RequestBody JSONObject body){
		JSONObject json = new JSONObject();
		User relevantUser = this.getUserFromSession(body); 
		if (relevantUser == null) {
			json.put("error","token error");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}

		json.put("userId", relevantUser.getID());
		json.put("userName", relevantUser.getUsername());
		json.put("password", relevantUser.getPassword());
		json.put("email", relevantUser.getEmail());
		json.put("role", relevantUser.getRole());

		return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
	}

	@PostMapping("/createUser")
	public ResponseEntity<String> createUser(@RequestBody User user) {
		try {
			boolean isInserted = database.insertUser(user);
			if (isInserted) {
				return new ResponseEntity<>("User created successfully", HttpStatus.CREATED); // 201 Created
			} else {
				return new ResponseEntity<>("Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/updateUser/{id}")
	public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User user) {
		try {
			boolean isUpdated = database.updateUser(id, user);
			if (isUpdated) {
				return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("User not found or no changes were made", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error updating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*")
	@PutMapping("/updateUser")
	public ResponseEntity<String> updateUser(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		User relevantUser = this.getUserFromSession(body);
		
		if (relevantUser == null) {
			json.put("error", "token error");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.UNAUTHORIZED);
		}

		String email = (String) body.get("email"); 
		String password = (String) body.get("password"); 
		
		if (email == null || password == null) {
			json.put("error", "Missing email or password");
			return new ResponseEntity<>(json.toJSONString(), HttpStatus.BAD_REQUEST);
		}

		// Update password and email
		relevantUser.setEmail(email);
		relevantUser.setPassword(password);

		try {
			boolean isUpdated = database.updateUser(relevantUser.getID(), relevantUser);
			if (isUpdated) {
				System.out.println("User updated successfully");
				return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
			} else {
				System.out.println("User not found or no changes were made");
				return new ResponseEntity<>("User not found or no changes were made", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			System.out.println("Error updating user: " + e.getMessage());
			return new ResponseEntity<>("Error updating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable int id) {
		try {
			boolean isDeleted = database.deleteUser(id);
			if (isDeleted) {
				return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("User not found or could not be deleted", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//Pokemon CRUD
	@PostMapping("/createPokemon")
	public ResponseEntity<String> createPokemon(@RequestBody Pokemon pokemon) {
		boolean isInserted = database.insertPokemon(pokemon);
		if (isInserted) {
			return new ResponseEntity<>("Pokemon created successfully", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Failed to create Pokemon", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> getAttributeCategoriesImage(@RequestBody JSONObject body) {
		JSONObject response = new JSONObject();
		if (!body.containsKey("categoryID")) {
			response.put("error", body);
			return new ResponseEntity<>(response.toJSONString(), HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(response.toJSONString(), HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/getAttributeCategoriesImages")
	public ResponseEntity<String> getAttributeCategoriesImages(@RequestBody JSONObject body) {
		JSONObject response = new JSONObject();
		User releventUser = this.getUserFromSession(body); 
		if (releventUser == null) {
			response.put("error","token error");
			return new ResponseEntity<>(response.toJSONString(), HttpStatus.UNAUTHORIZED);
		}
		if (!body.containsKey("sightingID")) {
			response.put("error", "There was no sightingID.");
			return new ResponseEntity<>(response.toJSONString(), HttpStatus.UNAUTHORIZED);
		}

		int pokemonID = this.database.sightingToPokemonID(Integer.valueOf(String.valueOf(body.get("sightingID"))));
		ArrayList<attributeImage> images = this.database.getAttributeImagesByPokemonID(pokemonID);
		if (images.size() <= 0) {
			response.put("error", "No photo could be found");
			return new ResponseEntity<>(response.toJSONString(), HttpStatus.NOT_FOUND);
		}

		JSONArray image_array = new JSONArray();
		for (int idx = 0; idx < images.size(); idx++) {
			attributeImage oop_image_instance = images.get(idx);
			JSONObject image_instance = new JSONObject();

			image_instance.put("image", Base64.getEncoder().encodeToString(oop_image_instance.getImage()));
			image_array.add(image_instance);
		}

		return new ResponseEntity<>(image_array.toJSONString(), HttpStatus.OK);
	}

	@GetMapping("/getPokemon")
	public ResponseEntity<Object> getPokemon(
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String name) {

		// If neither id nor name is provided, return a 400 Bad Request
		if (id == null && name == null) {
			return new ResponseEntity<>("Either 'id' or 'name' must be provided", HttpStatus.BAD_REQUEST);
		}

		// Call the getPokemon method in the Database handler
		Pokemon pokemon = database.getPokemon(id, name);

		if (pokemon != null) {
			return new ResponseEntity<>(pokemon, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Pokemon not found.",HttpStatus.NOT_FOUND);
		}
	}

	// UPDATE: Update Pokemon
	@PutMapping("/updatePokemon/{id}")
	public ResponseEntity<String> updatePokemon(
			@PathVariable Integer id,
			@RequestBody Pokemon pokemon) {
		// If neither id nor name is provided, return a 400 Bad Request
		boolean isUpdated = database.updatePokemon(id, pokemon);
		if (isUpdated) {
			return new ResponseEntity<>("Pokemon updated successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to update Pokemon", HttpStatus.NOT_FOUND);
		}
	}
	// DELETE: Delete Pokemon

	@DeleteMapping("deletePokemon/{id}")
	public ResponseEntity<String> deletePokemon(@PathVariable int id) {
		boolean isDeleted = database.deletePokemon(id);
		if (isDeleted) {
			return new ResponseEntity<>("Pokemon deleted successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to delete Pokemon", HttpStatus.NOT_FOUND);
		}
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/getPokemonImageById")
	public ResponseEntity<String> getPokemonImageById(@RequestBody JSONObject body) {
		JSONObject response = new JSONObject();
		User releventUser = this.getUserFromSession(body); 
		if (releventUser == null) {
			response.put("error","token error");
			return new ResponseEntity<>(response.toJSONString(), HttpStatus.UNAUTHORIZED);
		}
		
		if (!body.containsKey("SightingID")) {
			response.put("error", "No sightingID has been provided");
			return new ResponseEntity<>(response.toJSONString(), HttpStatus.NOT_FOUND);
		}
		
		String test = String.valueOf(body.get("SightingID"));
		int pokemonID = database.sightingToPokemonID(Integer.parseInt(test));
		PokemonGeneratedImage img = database.getPokemonImageById(pokemonID);

		if (img != null) {
			String base64Encoded = Base64.getEncoder().encodeToString(img.getImage());
			response.put("id", img.getPokemonID());
			response.put("image", base64Encoded);
			return new ResponseEntity<>(response.toString(), HttpStatus.OK);
		}
		else {
			response.put("error","Pokemon Image not found.");
			return new ResponseEntity<>(response.toJSONString(), HttpStatus.NOT_FOUND);
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(DexApplication.class, args);
	}


}
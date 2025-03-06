package com._9.dex;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.io.IOException;
import java.util.Base64;

import com.google.gson.*;

import okhttp3.*;

@Component
public class ImageProcessing {

    @Autowired
    private Database database;

    @Value("${google.api.key}")
    private String googleApiKey;

//    @Value("${openai.api.key}")
//    private String openAiApiKey;

    @Value("${google.api.key}")
    private String apiKey;

    private static final String API_KEY = "";
    private static final String openAiApiKey = "";
//    private static final String openAiApiKey = "123";
    private static final String API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-002:generateContent?key=" + API_KEY;
//    private static final String GOOGLE_API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-002:generateContent?key=";

    public ImageProcessing(Database database) {
        this.database = database;
    }

    public static String imageSpeciesName(String base64Image) throws IOException {
        // Decode the base64 image string to bytes
        base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Upload the image and get the imageID
        String imageID = uploadImage(imageBytes);

        // Get the species information using the imageID
        JsonObject response = getType(imageID);

        // Extract the species name from the response
        String speciesName = extractSpeciesName(response);
        if (speciesName == null) {
            throw new IOException("Species name not found in the response.");
        }
        return speciesName;
    }

    private static String uploadImage(byte[] imageBytes) throws IOException {
        String imageID = null;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("image/jpeg");

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg",
                        RequestBody.create(imageBytes, mediaType))
                .build();

        Request request = new Request.Builder()
                .url("https://api.plantnet.org/v1/images")
                .post(body)
                .addHeader("accept", "*/*")
                .addHeader("user-agent", "YourAppName/1.0")
                // Include other headers as necessary
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                imageID = jsonObject.get("id").getAsString();
            } else {
                throw new IOException("Unexpected response code " + response.code());
            }
        }

        return imageID;
    }

    private static JsonObject getType(String imageID) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Build the JSON data
        JsonObject jsonData = new JsonObject();
        JsonArray imagesArray = new JsonArray();
        JsonObject imageObject = new JsonObject();
        imageObject.addProperty("url", "https://bs.plantnet.org/v1/image/o/" + imageID);
        imagesArray.add(imageObject);
        jsonData.add("images", imagesArray);

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(jsonData.toString(), mediaType);

        HttpUrl url = HttpUrl.parse("https://api.plantnet.org/v1/projects/k-world-flora/queries/identify").newBuilder()
                .addQueryParameter("illustratedOnly", "true")
                .addQueryParameter("clientType", "web")
                .addQueryParameter("clientVersion", "3.0.0")
                .addQueryParameter("lang", "en-au")
                .addQueryParameter("kt", "true")
                .addQueryParameter("mediaSource", "file")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("user-agent", "YourAppName/1.0")
                // Include other headers as necessary
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                return jsonObject;
            } else {
                throw new IOException("Unexpected response code " + response.code());
            }
        }
    }

    private static String extractSpeciesName(JsonObject response) {
        JsonArray results = response.getAsJsonArray("results");
        if (results != null && results.size() > 0) {
            JsonObject firstResult = results.get(0).getAsJsonObject();
            JsonObject species = firstResult.getAsJsonObject("species");
            if (species != null) {
                // Extract the 'name' field
                return species.get("name").getAsString();
            }
        }
        return null;
    }

    public int generatePlantInformation(String base64Image, String speciesName) {
        int pokemonID = -1;
        try {
            JSONObject requestBody = new JSONObject();

            // Contents
            JSONArray contents = new JSONArray();
            JSONObject contentObject = new JSONObject();
            contentObject.put("role", "user");

            JSONArray partsArray = new JSONArray();
            JSONObject partObject = new JSONObject();

            // Use the provided base64Image directly
            base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
            partObject.put("text", speciesName);
            partsArray.put(partObject);

            contentObject.put("parts", partsArray);
            contents.put(contentObject);

            requestBody.put("contents", contents);

            // System Instruction
            JSONObject systemInstruction = new JSONObject();
            systemInstruction.put("role", "user");

            JSONArray systemPartsArray = new JSONArray();
            JSONObject systemPartObject = new JSONObject();
            systemPartObject.put("text", "You will receive a species name of a plant, and your task is to generate a JSON response in the following specific order: SpeciesName (the plant species name provided), PokemonName (a fictional Pokémon-like name based on the species), PlantType (an integer between 1 and 8 representing the plant type, where 1 is Tree, 2 is Shrub, 3 is Grass, 4 is Fern, 5 is Moss, 6 is Algae, 7 is Lichen, and 8 is Fungi), HP (an integer representing health points), AttackCategory (an integer between 1 and 7, where 1 is Cutting, 2 is Braced, 3 is Psychedelic, 4 is Poison, 5 is Medenial, 6 is Kinetic, and 7 is Behavioral), AttackDesc (a brief description of the plant's attack), DefenseCategory (an integer between 1 and 7, using the same mapping as AttackCategory), DefenseDesc (a brief description of the plant's defense), and TaxonomicCategory (a relevant taxonomic classification). Ensure the JSON response is in the exact order requested, and that PlantType, HP, AttackCategory, and DefenseCategory are integers.");
            systemPartsArray.put(systemPartObject);

            systemInstruction.put("parts", systemPartsArray);
            requestBody.put("systemInstruction", systemInstruction);

            // Generation Config
            JSONObject generationConfig = new JSONObject();
            generationConfig.put("temperature", 2);
            generationConfig.put("topK", 40);
            generationConfig.put("topP", 0.95);
            generationConfig.put("maxOutputTokens", 8192);
            generationConfig.put("responseMimeType", "application/json");
            requestBody.put("generationConfig", generationConfig);

            // API endpoint URL
            String apiEndpoint = API_ENDPOINT;

            // Establish a connection to the API
            URL url = new URL(apiEndpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response
            StringBuilder response = new StringBuilder();
            int status = connection.getResponseCode();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            status >= 200 && status < 300 ? connection.getInputStream() : connection.getErrorStream(),
                            StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            } finally {
                connection.disconnect();
            }

            // Call parseAndProcessResponse with the response and image data
            pokemonID = parseAndProcessResponse(response.toString(), base64Image);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return pokemonID;
    }

    private int parseAndProcessResponse(String jsonResponse, String base64Image) {
        try {
            // Decode the base64Image to byte array for storage

            byte[] originalImageData = Base64.getDecoder().decode(base64Image);
//            Base64.getEncoder().encodeToString(imageBytes)
            // Parse the JSON response
            JSONObject responseObj = new JSONObject(jsonResponse);
            JSONArray candidates = responseObj.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            String responseText = parts.getJSONObject(0).getString("text");

            // The responseText contains the JSON string we want to parse
            JSONObject plantAttributes = new JSONObject(responseText);

            // Extract individual fields
            String speciesName = plantAttributes.optString("SpeciesName", null);
            String pokemonName = plantAttributes.optString("PokemonName", null);
            String plantTypeStr = plantAttributes.optString("PlantType", null);
            String hpStr = plantAttributes.optString("HP", null);
            String attackCategoryStr = plantAttributes.optString("AttackCategory", null);
            String attackDesc = plantAttributes.optString("AttackDesc", null);
            String defenseCategoryStr = plantAttributes.optString("DefenseCategory", null);
            String defenseDesc = plantAttributes.optString("DefenseDesc", null);
            String taxonomicCategory = plantAttributes.optString("TaxonomicCategory", null);

            // Validate required fields
            if (speciesName == null || pokemonName == null) {
                System.out.println("Essential fields are missing in the response.");
                return -1;
            }

            // Convert string values to integers
            Integer plantType = parseInteger(plantTypeStr, "PlantType");
            Integer hp = parseInteger(hpStr, "HP");
            Integer attackCategory = parseInteger(attackCategoryStr, "AttackCategory");
            Integer defenseCategory = parseInteger(defenseCategoryStr, "DefenseCategory");

            // Check if the SpeciesName exists in the database
            int pokemonId = database.pokemonSpeciesExists(speciesName);

            if (pokemonId < 0) {
                // Create a new Pokemon object and set its fields
                Pokemon newPokemon = new Pokemon();
                newPokemon.setPokemonName(pokemonName);
                newPokemon.setSpeciesName(speciesName);
                newPokemon.setPlantType(plantType != null ? plantType : 0);
                newPokemon.setHP(hp != null ? hp : 0);
                newPokemon.setAttackCategory(attackCategory != null ? attackCategory : 0);
                newPokemon.setAttackDescription(attackDesc != null ? attackDesc : "");
                newPokemon.setDefenseCategory(defenseCategory != null ? defenseCategory : 0);
                newPokemon.setDefenseDescription(defenseDesc != null ? defenseDesc : "");
                newPokemon.setTaxonomicCategory(taxonomicCategory != null ? taxonomicCategory : "");

                // Insert the new Pokemon into the database and get the new pokemonId
                int newpokemonId = database.insPokemon(newPokemon);

                if (newpokemonId > 0) {
                    System.out.println("New Pokémon inserted into the database with ID: " + pokemonId);

                    // Generate the prompt for OpenAI API
                    String prompt = generatePrompt(speciesName);

                    // Generate the Pokémon image using OpenAI API
                    byte[] generatedImageData = generatePokemonImage(prompt, pokemonId);
                    // Insert the generated image into PokemonGeneratedImage table
                    boolean genImageInserted = database.insertPokemonGeneratedImage(newpokemonId, generatedImageData);

                    if (genImageInserted) {
                        System.out.println("Generated Pokémon image inserted into the database.");
                    } else {
                        System.out.println("Failed to insert generated Pokémon image into the database.");
                    }

                    // Insert the original image into PokemonRealImage table
                    boolean realImageInserted = database.insertPokemonRealImage(pokemonId, originalImageData);

                    if (realImageInserted) {
                        System.out.println("Original Pokémon image inserted into the database.");
                    } else {
                        System.out.println("Failed to insert original Pokémon image into the database.");
                    }
                    return newpokemonId;

                } else {
                    System.out.println("Failed to insert new Pokémon into the database.");
                    return -1;
                }
            } else {
                System.out.println("Pokémon with SpeciesName '" + speciesName + "' already exists in the database.");
                return pokemonId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String generatePrompt(String speciesName) {
        return "Generate a pixel art image of a Pokémon inspired by and that looks like the plant species" + speciesName +
                "The Pokémon should be centered in the image with a plain white background. It should use the same colours and shapes as the real plant species.";

//        return "Generate a pixel art image of a Pokémon inspired by the plant species \"" + speciesName +
//                "\". The Pokémon should be centered in the image with a plain white background. Its design must reflect " +
//                "the colors, shapes, and essence of the plant species, incorporating elements like petals, leaves, vines, or " +
//                "tendrils. The Pokémon should have an abstract, whimsical appearance, avoiding resemblance to real-world animals. " +
//                "Use a pixelated style similar to classic Pokémon sprites, with expressive facial features to give the creature personality. " +
//                "Ensure the final design embodies a magical, imaginative creature that fits within the Pokémon universe.";
    }

    private byte[] generatePokemonImage(String prompt, int pokemonId) throws IOException {
        String apiEndpoint = "https://api.openai.com/v1/images/generations";

        // Build the JSON request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "dall-e-3");
        requestBody.put("prompt", prompt);
        requestBody.put("n", 1);
        requestBody.put("size", "1024x1024");
        requestBody.put("response_format", "b64_json");


        // Create the connection
        URL url = new URL(apiEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + openAiApiKey);
        connection.setDoOutput(true);

        // Send the request
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Read the response
        StringBuilder response = new StringBuilder();
        int status = connection.getResponseCode();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        status >= 200 && status < 300 ? connection.getInputStream() : connection.getErrorStream(),
                        StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } finally {
            connection.disconnect();
        }

        // Parse the response to get the image data
        JSONObject responseJson = new JSONObject(response.toString());
//        if (responseJson.get("error") != null) {
//            database.deletePokemon(pokemonId);
//            throw new IOException("Content policy violation: " + ((JSONObject) responseJson.get("error")).get("code"));
//        }

        JSONArray dataArray = responseJson.getJSONArray("data");
        if (dataArray.length() > 0) {
            JSONObject dataObject = dataArray.getJSONObject(0);
            String b64Image = dataObject.getString("b64_json");
            byte[] imageBytes = Base64.getDecoder().decode(b64Image);
            return imageBytes;
        } else {
            throw new IOException("No image data received from OpenAI API.");
        }
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for " + fieldName + ": " + value);
            return null;
        }
    }
}

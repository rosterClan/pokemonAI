package com._9.dex;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;

import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class Database {

    private String filePath;
    private Connection conn = null;

    public Database(@Value("${database.filePath}") String filePath) {
        this.filePath = filePath;
        //Check if file exists, if not create a new database file.
        try {
            //Initialise the database.
            String url = "jdbc:sqlite:" + filePath;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        List<String> tables = new ArrayList<>();

        String sql1 = "CREATE TABLE IF NOT EXISTS Users ("
            + "	UserID integer PRIMARY KEY,"
            + " Email text NOT NULL,"
            + "	Username text NOT NULL,"
            + "	Password text NOT NULL,"
            + " Security_question text NOT NULL,"
            + " Security_answer text NOT NULL,"
            + " RoleID integer NOT NULL,"
            + " FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)"
            + ");";
        tables.add(sql1);

        String sql2 = "Create Table IF NOT EXISTS userFollowing ("
            + " Id integer PRIMARY KEY,"
            + " UserID integer NOT NULL,"
            + " FollowingUserID integer NOT NULL,"
            + " FOREIGN KEY (UserID) REFERENCES Users(UserID),"
            + " FOREIGN KEY (FollowingUserID) REFERENCES Users(UserID)"
            + ");";
        tables.add(sql2);

        String sql3 = "CREATE TABLE IF NOT EXISTS Roles ("
            + " RoleID integer PRIMARY KEY,"
            + " RoleName text NOT NULL,"
            + " RoleDescription text NOT NULL"
            + ");";
        tables.add(sql3);

        String sql4 = "CREATE TABLE IF NOT EXISTS PlantTypes (" +
            "    PlantTypeID INTEGER PRIMARY KEY," +
            "    PlantTypeName TEXT NOT NULL," +
            "    PlantTypeDesc TEXT" +
            " );";
        tables.add(sql4);

        String sql5 = "CREATE TABLE IF NOT EXISTS AttributeCategories (" +
            "    AttributeCatID INTEGER PRIMARY KEY," +
            "    AttributeCatName TEXT NOT NULL," +
            "    AttributeCatDesc TEXT" +
            ");";
        tables.add(sql5);

        String sql7 = "CREATE TABLE IF NOT EXISTS Biomes (" +
            "    BiomeID INTEGER PRIMARY KEY," +
            "    BiomeName TEXT NOT NULL," +
            "    BiomeDescription TEXT" +
            ");";
        tables.add(sql7);

        String sql8 = "CREATE TABLE IF NOT EXISTS Pokemon (" +
            "    PokemonID INTEGER PRIMARY KEY," +
            "    PokemonName TEXT NOT NULL," +
            "    PlantType INTEGER," +
            "    SpeciesName TEXT NOT NULL," +
            "    HP INTEGER," +
            "    AttackCategory INTEGER," +
            "    AttackDesc TEXT," +
            "    DefenseCategory INTEGER," +
            "    DefenseDesc TEXT," +
            "    TaxonomicCategory TEXT," +
            "    FOREIGN KEY (PlantType) REFERENCES PlantTypes(PlantTypeID)," +
            "    FOREIGN KEY (AttackCategory) REFERENCES AttributeCategories(AttributeCatID)," +
            "    FOREIGN KEY (DefenseCategory) REFERENCES AttributeCategories(AttributeCatID)" +
            ");";
        tables.add(sql8);

        String sql9 = "CREATE TABLE IF NOT EXISTS PokemonGeneratedImage (" +
            "    PokemonID INTEGER PRIMARY KEY," +
            "    Image BLOB," +
            "    FOREIGN KEY (PokemonID) REFERENCES Pokemon(PokemonID)" +
            ");";
        tables.add(sql9);

        String sql10 = "CREATE TABLE IF NOT EXISTS PokemonRealImage (" +
            "    PokemonID INTEGER PRIMARY KEY," +
            "    Image BLOB," +
            "    FOREIGN KEY (PokemonID) REFERENCES Pokemon(PokemonID)" +
            ");";
        tables.add(sql10);

        String sql11 = "CREATE TABLE IF NOT EXISTS PokemonSightings (" +
            "    SightingID INTEGER PRIMARY KEY," +
            "    UserID INTEGER NOT NULL," +
            "    PokemonID INTEGER NOT NULL," +
            "    BiomeID INTEGER NOT NULL," +
            "    Longitude REAL," +
            "    Latitude REAL," +
            "    FoundDate DATETIME," +
            "    FOREIGN KEY (UserID) REFERENCES User(UserID)," +
            "    FOREIGN KEY (PokemonID) REFERENCES Pokemon(PokemonID)," +
            "    FOREIGN KEY (BiomeID) REFERENCES Biomes(BiomeID)" +
            ");";
        tables.add(sql11);

        String sql12 = "CREATE TABLE IF NOT EXISTS Sessions (" +
                " SessionID INTEGER PRIMARY KEY," +
                " UserID INTEGER NOT NULL," +
                " SessionToken TEXT NOT NULL," +
                " ExpiryDate DATETIME NOT NULL, " +
                " IsActive BOOLEAN NOT NULL," +
                " FOREIGN KEY (USERID) REFERENCES Users(UserID)" +
                ");";
        tables.add(sql12);

        String sql13 = "CREATE TABLE IF NOT EXISTS AttributeCategoriesImage (" +
                "    AttributeID INTEGER REFERENCES AttributeCategories (AttributeCatID) PRIMARY KEY," +
                "    Image BLOB" +
                ");";

        tables.add(sql13);

        for (String table : tables) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
                Statement stmt = conn.createStatement()) {
                stmt.execute(table);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getNewSession(int UserID) {
        String sql = "SELECT * FROM Sessions WHERE UserID = ? AND IsActive = 1 ORDER BY ExpiryDate DESC LIMIT 1";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, UserID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date expiry = new Date(Long.parseLong(rs.getString("ExpiryDate")));
                Date now = new Date();
                if (now.after(expiry)) {
                    this.closeSession(rs.getString("SessionToken"));
                    return getNewSession(UserID);
                }
                return rs.getString("SessionToken");
            } else {
                sql = "INSERT INTO Sessions (UserID, SessionToken, ExpiryDate, IsActive) VALUES (?, ?, ?, ?)";
                try {
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, UserID);
                    String sessionToken = UUID.randomUUID().toString();
                    stmt.setString(2, sessionToken);
                    stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis() + 3600000));
                    stmt.setBoolean(4, true);
                    stmt.executeUpdate();
                    
                    return sessionToken;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeSession(String sessionToken) {
        String sql = "UPDATE Sessions SET IsActive = 0 WHERE SessionToken = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, sessionToken);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertDefaultValues() {
        //Open JSON default values from resources
        JSONParser parser = new JSONParser();
        try {
            Resource res = new ClassPathResource("defaultValues.json");

            JSONParser jsonParser = new JSONParser();
            InputStreamReader reader = new InputStreamReader(res.getInputStream());

            JSONObject defaultVals = (JSONObject) parser.parse(reader);

            JSONObject user = (JSONObject) defaultVals.get("User");
            JSONArray roles = (JSONArray) defaultVals.get("Roles");
            JSONArray biomes = (JSONArray) defaultVals.get("Biomes");
            JSONArray plantTypes = (JSONArray) defaultVals.get("PlantTypes");
            JSONArray attributeCategories = (JSONArray) defaultVals.get("AttributeCategories");

            String userSql = "INSERT INTO Users (Email, Username, Password, Security_question, Security_answer, RoleID) VALUES (?,?,?,?,?,?)";
            String roleSql = "INSERT INTO Roles (RoleID, RoleName, RoleDescription) VALUES (?,?,?)";
            String biomeSql = "INSERT INTO Biomes (BiomeName, BiomeDescription) VALUES (?,?)";
            String plantTypeSql = "INSERT INTO PlantTypes (PlantTypeName, PlantTypeDesc) VALUES (?,?)";
            String attributeCategorySql = "INSERT INTO AttributeCategories (AttributeCatName, AttributeCatDesc) VALUES (?,?)";

            Connection conn = null;

            try {
                conn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
                PreparedStatement userStmt = conn.prepareStatement(userSql);
                PreparedStatement roleStmt = conn.prepareStatement(roleSql);
                PreparedStatement biomeStmt = conn.prepareStatement(biomeSql);
                PreparedStatement plantTypeStmt = conn.prepareStatement(plantTypeSql);
                PreparedStatement attributeCategoryStmt = conn.prepareStatement(
                    attributeCategorySql);

                //Insert default values for users
                userStmt.setString(1, (String) user.get("Email"));
                userStmt.setString(2, (String) user.get("Username"));
                PasswordHasher passwordHasher = new PasswordHasher((String) user.get("Password"));
                String hashedPassword = passwordHasher.hash();
                userStmt.setString(3, hashedPassword);
                userStmt.setString(4, (String) user.get("SecurityQuestion"));
                userStmt.setString(5, (String) user.get("SecurityAnswer"));
                userStmt.setInt(6, (int) ((Long) user.get("RoleID")).intValue());
                userStmt.executeUpdate();

                //Insert default values for roles
                for (Object role : roles) {
                    JSONObject roleObj = (JSONObject) role;
                    roleStmt.setString(1, (String) roleObj.get("RoleID"));
                    roleStmt.setString(2, (String) roleObj.get("RoleName"));
                    roleStmt.setString(3, (String) roleObj.get("RoleDescription"));
                    roleStmt.executeUpdate();
                }

                //Insert default values for biomes
                for (Object biome : biomes) {
                    JSONObject biomeObj = (JSONObject) biome;
                    biomeStmt.setString(1, (String) biomeObj.get("BiomeName"));
                    biomeStmt.setString(2, (String) biomeObj.get("BiomeDescription"));
                    biomeStmt.executeUpdate();
                }

                //Insert default values for plant types
                for (Object plantType : plantTypes) {
                    JSONObject plantTypeObj = (JSONObject) plantType;
                    plantTypeStmt.setString(1, (String) plantTypeObj.get("PlantTypeName"));
                    plantTypeStmt.setString(2, (String) plantTypeObj.get("PlantTypeDescription"));
                    plantTypeStmt.executeUpdate();
                }

                //Insert default values for attribute categories
                for (Object attributeCategory : attributeCategories) {
                    JSONObject attributeCategoryObj = (JSONObject) attributeCategory;
                    attributeCategoryStmt.setString(1,
                        (String) attributeCategoryObj.get("AttributeCategoryName"));
                    attributeCategoryStmt.setString(2,
                        (String) attributeCategoryObj.get("AttributeCategoryDescription"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser(String usernameOrEmail) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE Username = ? OR Email = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user = new User(rs.getInt("UserID"), rs.getString("Username"),
                    rs.getString("Password"), rs.getString("Email"), rs.getString("RoleID"),
                    rs.getString("Security_question"), rs.getString("Security_answer"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUser(int id) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE UserID = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user = new User(rs.getInt("UserID"), rs.getString("Username"),
                    rs.getString("Password"), rs.getString("Email"), rs.getString("RoleID"),
                    rs.getString("Security_question"), rs.getString("Security_answer"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User validateToken(String token) {
        User foundUser = null; 
        String sql = "SELECT * FROM Sessions WHERE SessionToken=?";

        try {
            Date currDate = new Date(System.currentTimeMillis());
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Token currToken = new Token(
                    Integer.parseInt(rs.getString("SessionID")),
                    Integer.parseInt(rs.getString("UserID")),
                    rs.getString("SessionToken"),
                    new Date(Long.parseLong(rs.getString("ExpiryDate"))),
                    (Integer.parseInt(rs.getString("IsActive"))) == 1
                );

                if (currDate.after(currToken.getExpiryDate())) {
                    String removeQry = "DELETE FROM Sessions WHERE SessionToken=?";
                    PreparedStatement removeStmt = conn.prepareStatement(removeQry);
                    removeStmt.execute();
                    String newToken = this.getNewSession(currToken.getUserID());

                }
                User query_usr = this.getUser(currToken.getUserID());
                if (query_usr != null && foundUser == null) {
                    foundUser = query_usr;
                }
                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundUser;
    }

    public User getUserId(String userName, String password) { //Might be redundent, could potentially be removed
        User user = null; 
        String sql = "SELECT * FROM Users WHERE Username=? AND Password=?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);

            //PasswordHasher pwHasher = new PasswordHasher(password);
            //stmt.setString(2, pwHasher.hash());
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = new User(rs.getInt("UserID"), rs.getString("Username"),
                    rs.getString("Password"), rs.getString("Email"), rs.getString("RoleID"),
                    rs.getString("Security_question"), rs.getString("Security_answer"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO Users (Email, Username, Password, Security_question, Security_answer, RoleID) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getSecurityQuestion());
            stmt.setString(5, user.getSecurityAnswer());
            stmt.setString(6, user.getRole());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertUserFollowing(int userID, int followingUserID) {
        String sql = "INSERT INTO userFollowing (UserID, FollowingUserID) VALUES (?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            stmt.setInt(2, followingUserID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(int userID, String password) {
        String sql = "UPDATE Users SET Password = ? WHERE UserID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, password);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateUser(int id, User user){
        String sql = "UPDATE Users SET Email = ?, Username = ?, Password = ?, Security_question = ?, Security_answer = ?, RoleID = ? WHERE UserID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            //PasswordHasher pwHasher = new PasswordHasher(user.getPassword());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getSecurityQuestion());
            stmt.setString(5, user.getSecurityAnswer());
            stmt.setString(6, user.getRole());
            stmt.setInt(7, id);
            int updated = stmt.executeUpdate();

            return updated > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0; // Return true if a user was deleted

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false; // Return false if an error occurred
        }
    }

    public JSONObject getSightingByID(int sightingID) {
        JSONObject pokemonInstance = null;
        String sql = "SELECT " +
                    "    Pokemon.PokemonID, " +
                    "    PokemonSightings.Longitude as Longitude, " + //
                    "    PokemonSightings.Latitude as Latitude, " + //
                    "    PokemonSightings.FoundDate, " + //
                    "    Pokemon.PokemonName, " + //
                    "    SpeciesName, " + //
                    "    HP, " + //
                    "    AttackDesc, " + //
                    "    DefenseDesc, " + //
                    "    TaxonomicCategory, " + //
                    "    PlantTypeName ," + //
                    "    PlantTypeDesc " + //
                    "FROM Pokemon" + //
                    "    JOIN PlantTypes " + //
                    "        ON Pokemon.PlantType = PlantTypes.PlantTypeID " + //
                    "    JOIN PokemonSightings " + //
                    "        ON PokemonSightings.PokemonID = Pokemon.PokemonID " + //
                    "WHERE " + //
                    "    PokemonSightings.SightingID = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(sightingID));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pokemonInstance = new JSONObject();

                pokemonInstance.put("PokemonID", rs.getInt("PokemonID"));
                pokemonInstance.put("Longitude", rs.getDouble("Longitude"));
                pokemonInstance.put("Latitude", rs.getDouble("Latitude"));
                pokemonInstance.put("FoundDate", rs.getLong("FoundDate"));
                pokemonInstance.put("PokemonName", rs.getString("PokemonName"));
                pokemonInstance.put("SpeciesName", rs.getString("SpeciesName"));
                pokemonInstance.put("HP", rs.getInt("HP"));
                pokemonInstance.put("AttackDesc", rs.getString("AttackDesc"));
                pokemonInstance.put("DefenseDesc", rs.getString("DefenseDesc"));
                pokemonInstance.put("TaxonomicCategory", rs.getString("TaxonomicCategory"));
                pokemonInstance.put("PlantTypeName", rs.getString("PlantTypeName"));
                pokemonInstance.put("PlantTypeDesc", rs.getString("PlantTypeDesc"));         
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokemonInstance;
    }
    
    public JSONArray getDexByUserID(int userId) {
        JSONArray dexItems = new JSONArray();
        String sql = "SELECT " +
                    "    PokemonSightings.SightingID, " +
                    "    PokemonSightings.Longitude as Longitude, " + //
                    "    PokemonSightings.Latitude as Latitude, " + //
                    "    PokemonSightings.FoundDate, " + //
                    "    Pokemon.PokemonName, " + //
                    "    SpeciesName, " + //
                    "    HP, " + //
                    "    AttackDesc, " + //
                    "    DefenseDesc, " + //
                    "    TaxonomicCategory, " + //
                    "    PlantTypeName ," + //
                    "    PlantTypeDesc " + //
                    "FROM Pokemon" + //
                    "    JOIN PlantTypes " + //
                    "        ON Pokemon.PlantType = PlantTypes.PlantTypeID " + //
                    "    JOIN PokemonSightings " + //
                    "        ON PokemonSightings.PokemonID = Pokemon.PokemonID " + //
                    "WHERE " + //
                    "    PokemonSightings.UserID = ? ";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(userId));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject pokemonInstance = new JSONObject();
                
                pokemonInstance.put("SightingID", rs.getInt("SightingID"));
                pokemonInstance.put("Longitude", rs.getDouble("Longitude"));
                pokemonInstance.put("Latitude", rs.getDouble("Latitude"));
                pokemonInstance.put("FoundDate", rs.getLong("FoundDate"));
                pokemonInstance.put("PokemonName", rs.getString("PokemonName"));
                pokemonInstance.put("SpeciesName", rs.getString("SpeciesName"));
                pokemonInstance.put("HP", rs.getInt("HP"));
                pokemonInstance.put("AttackDesc", rs.getString("AttackDesc"));
                pokemonInstance.put("DefenseDesc", rs.getString("DefenseDesc"));
                pokemonInstance.put("TaxonomicCategory", rs.getString("TaxonomicCategory"));
                pokemonInstance.put("PlantTypeName", rs.getString("PlantTypeName"));
                pokemonInstance.put("PlantTypeDesc", rs.getString("PlantTypeDesc"));

                dexItems.add(pokemonInstance);            
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dexItems;
    }

    public JSONArray getDexForAllUsers () {
        JSONArray dexItems = new JSONArray();
        String sql = "SELECT " +
                    "    PokemonSightings.SightingID, " +
                    "    PokemonSightings.Longitude as Longitude, " + //
                    "    PokemonSightings.Latitude as Latitude, " + //
                    "    PokemonSightings.FoundDate, " + //
                    "    Pokemon.PokemonName, " + //
                    "    SpeciesName, " + //
                    "    HP, " + //
                    "    AttackDesc, " + //
                    "    DefenseDesc, " + //
                    "    TaxonomicCategory, " + //
                    "    PlantTypeName ," + //
                    "    PlantTypeDesc " + //
                    "FROM Pokemon" + //
                    "    JOIN PlantTypes " + //
                    "        ON Pokemon.PlantType = PlantTypes.PlantTypeID " + //
                    "    JOIN PokemonSightings " + //
                    "        ON PokemonSightings.PokemonID = Pokemon.PokemonID ";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject pokemonInstance = new JSONObject();
                
                pokemonInstance.put("SightingID", rs.getInt("SightingID"));
                pokemonInstance.put("Longitude", rs.getDouble("Longitude"));
                pokemonInstance.put("Latitude", rs.getDouble("Latitude"));
                pokemonInstance.put("FoundDate", rs.getLong("FoundDate"));
                pokemonInstance.put("PokemonName", rs.getString("PokemonName"));
                pokemonInstance.put("SpeciesName", rs.getString("SpeciesName"));
                pokemonInstance.put("HP", rs.getInt("HP"));
                pokemonInstance.put("AttackDesc", rs.getString("AttackDesc"));
                pokemonInstance.put("DefenseDesc", rs.getString("DefenseDesc"));
                pokemonInstance.put("TaxonomicCategory", rs.getString("TaxonomicCategory"));
                pokemonInstance.put("PlantTypeName", rs.getString("PlantTypeName"));
                pokemonInstance.put("PlantTypeDesc", rs.getString("PlantTypeDesc"));

                dexItems.add(pokemonInstance);            
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dexItems;
    }

    public JSONArray getLogByUserID(int userId) {
        JSONArray dexItems = new JSONArray();
        String sql = "SELECT * FROM Sessions WHERE UserID = ? ORDER BY ExpiryDate DESC LIMIT 10";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(userId));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject pokemonInstance = new JSONObject();
                
                pokemonInstance.put("Type", "Log In");
                pokemonInstance.put("Date", rs.getLong("ExpiryDate") - 3600000);
                dexItems.add(pokemonInstance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dexItems;
    }

    public Pokemon getPokemon(Integer pokemonID, String pokemonName) {
        String sql = null;
        Pokemon pokemon = null;
        ResultSet rs = null;

        try {
            // Determine query based on input
            if (pokemonID != null) {
                // Query by ID if ID is provided
                sql = "SELECT * FROM Pokemon WHERE PokemonID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, pokemonID);
                rs = stmt.executeQuery();
            } else if (pokemonName != null) {
                // Query by name if ID is not provided and name is provided
                sql = "SELECT * FROM Pokemon WHERE PokemonName = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, pokemonName);
                rs = stmt.executeQuery();
            }

            // Process the result
            if (rs != null && rs.next()) {
                pokemon = new Pokemon(
                    rs.getInt("PokemonID"),
                    rs.getString("PokemonName"),
                    rs.getInt("PlantType"),
                    rs.getString("SpeciesName"),
                    rs.getInt("HP"),
                    rs.getInt("AttackCategory"),
                    rs.getString("AttackDesc"),
                    rs.getInt("DefenseCategory"),
                    rs.getString("DefenseDesc"),
                    rs.getString("TaxonomicCategory")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching Pokemon: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokemon;
    }

    public Pokemon getPokemon(int pokemonID, String pokemonName) {
        String sql = "SELECT * FROM Pokemon WHERE PokemonID = ? OR PokemonName = ?";
        Pokemon pokemon = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pokemonID);
            stmt.setString(2, pokemonName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pokemon = new Pokemon(rs.getInt("PokemonID"),
                    rs.getString("PokemonName"),
                    rs.getInt("PlantType"),
                    rs.getString("SpeciesName"),
                    rs.getInt("HP"),
                    rs.getInt("AttackCategory"),
                    rs.getString("AttackDesc"),
                    rs.getInt("DefenseCategory"),
                    rs.getString("DefenseDesc"),
                    rs.getString("TaxonomicCategory"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemon;
    }

    public boolean insertPokemon(Pokemon pokemon) {
        String sql = "INSERT INTO Pokemon (PokemonName, PlantType, SpeciesName, HP, AttackCategory, AttackDesc, DefenseCategory, DefenseDesc, TaxonomicCategory) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, pokemon.getPokemonName());
            stmt.setInt(2, pokemon.getPlantType());
            stmt.setString(3, pokemon.getSpeciesName());
            stmt.setInt(4, pokemon.getHP());
            stmt.setInt(5, pokemon.getAttackCategory());
            stmt.setString(6, pokemon.getAttackDescription());
            stmt.setInt(7, pokemon.getDefenseCategory());
            stmt.setString(8, pokemon.getDefenseDescription());
            stmt.setString(9, pokemon.getTaxonomicCategory());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting Pokemon: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePokemon(int id, Pokemon pokemon) {
        String sql = "UPDATE Pokemon SET PokemonName = ?, PlantType = ?, SpeciesName = ?, HP = ?, AttackCategory = ?, AttackDesc = ?, " +
            "DefenseCategory = ?, DefenseDesc = ?, TaxonomicCategory = ? WHERE PokemonID = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, pokemon.getPokemonName());
            stmt.setInt(2, pokemon.getPlantType());
            stmt.setString(3, pokemon.getSpeciesName());
            stmt.setInt(4, pokemon.getHP());
            stmt.setInt(5, pokemon.getAttackCategory());
            stmt.setString(6, pokemon.getAttackDescription());
            stmt.setInt(7, pokemon.getDefenseCategory());
            stmt.setString(8, pokemon.getDefenseDescription());
            stmt.setString(9, pokemon.getTaxonomicCategory());
            stmt.setInt(10, id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating Pokemon: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<PokemonSighting> getUsersPokemonSightings(int userID) {
        String sql = "SELECT * FROM PokemonSightings WHERE UserID = ?";

        ArrayList<PokemonSighting> pokemonSightings = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pokemonSightings.add(new PokemonSighting(rs.getInt("SightingID"),
                    rs.getInt("PokemonID"),
                    rs.getInt("UserID"),
                    rs.getInt("BiomeID"),
                    rs.getDouble("Longitude"),
                    rs.getDouble("Latitude"),
                    rs.getDate("FoundDate"))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemonSightings;
    }

    public Integer insertPokemonSighting(PokemonSighting pokemonSighting) {
        String sql = "INSERT INTO PokemonSightings (UserID, PokemonID, BiomeID, Longitude, Latitude, FoundDate) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, pokemonSighting.getUserID());
            stmt.setInt(2, pokemonSighting.getPokemonID());
            stmt.setInt(3, pokemonSighting.getBiomeID());
            stmt.setDouble(4, pokemonSighting.getLongitude());
            stmt.setDouble(5, pokemonSighting.getLatitude());
            stmt.setTimestamp(6, new Timestamp(pokemonSighting.getFoundDate().getTime()));
    
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            return null;
    
        } catch (SQLException e) {
            System.out.println("Error inserting Pokemon sighting: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<LeaderboardEntry> getLeaderboard() {
        String sql = "SELECT " +
            "    u.Username, " +
            "    COUNT(ps.SightingID) AS sightingsCount, " +
            "    ps2.SightingID, " +
            "    ps2.UserID, " +
            "    ps2.PokemonID, " +
            "    ps2.BiomeID, " +
            "    ps2.Longitude, " +
            "    ps2.Latitude, " +
            "    ps2.FoundDate " +
            "FROM " +
            "    Users u " +
            "LEFT JOIN " +
            "    PokemonSightings ps ON u.UserID = ps.UserID " +
            "LEFT JOIN " +
            "    ( " +
            "        SELECT " +
            "            ps1.UserID, " +
            "            ps1.SightingID, " +
            "            ps1.PokemonID, " +
            "            ps1.BiomeID, " +
            "            ps1.Longitude, " +
            "            ps1.Latitude, " +
            "            ps1.FoundDate " +
            "        FROM " +
            "            PokemonSightings ps1 " +
            "        WHERE " +
            "            ps1.FoundDate = (SELECT MAX(ps2.FoundDate) " +
            "                             FROM PokemonSightings ps2 " +
            "                             WHERE ps2.UserID = ps1.UserID) " +
            "    ) ps2 ON u.UserID = ps2.UserID " +
            "GROUP BY " +
            "    u.UserID, u.Username " +
            "ORDER BY " +
            "    sightingsCount DESC;";
        ArrayList<LeaderboardEntry> leaderboardEntries = new ArrayList<LeaderboardEntry>();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt("sightingsCount") != 0) {
                    leaderboardEntries.add(new LeaderboardEntry(
                            rs.getString("Username"),
                            rs.getInt("sightingsCount"),
                            rs.getInt("UserID"),
                            new PokemonSighting(
                                rs.getInt("SightingID"),
                                rs.getInt("PokemonID"),
                                rs.getInt("UserID"),
                                rs.getInt("BiomeID"),
                                rs.getDouble("Longitude"),
                                rs.getDouble("Latitude"),
                                new Date(Long.parseLong(rs.getString("FoundDate")))
                            )
                    ));
                } else {
                    leaderboardEntries.add(new LeaderboardEntry(
                        rs.getString("Username"),
                        rs.getInt("sightingsCount"),
                        rs.getInt("UserID"),
                        null
                    ));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaderboardEntries;
    }

    public void insertPokemonData() {
        String insertPokemonSql =
            "INSERT INTO Pokemon (PokemonID, PokemonName, PlantType, SpeciesName, HP, " +
                "AttackCategory, AttackDesc, DefenseCategory, DefenseDesc, TaxonomicCategory) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertPokemonSql)) {
            // Example data for the Pokemon table
            int[] pokemonIDs = {1, 2, 3, 4};
            String[] pokemonNames = {"Bulbasaur", "Ivysaur", "Venusaur", "NewPokemnon"};
            int[] plantTypes = {1, 1, 1,
                1}; // Assuming all belong to PlantTypeID = 1 (e.g., "Grass/Poison")
            String[] speciesNames = {"Seed", "Seed", "Seed", "Seed"};
            int[] HPs = {45, 60, 80, 1000};
            int[] attackCategories = {1, 2, 3,
                4}; // Assuming these match AttributeCatID values for Attack
            String[] attackDescriptions = {
                "Sharp-edged leaves are launched to slash at the opposing Pokémon.",
                "Whips the foe with slender vines.",
                "A large blast of solar energy is unleashed.",
                "blah"
            };
            int[] defenseCategories = {1, 2, 3,
                4}; // Assuming these match AttributeCatID values for Defense
            String[] defenseDescriptions = {"Boosts the power of Grass-type moves when HP is low.",
                "Boosts the power of Grass-type moves when HP is low.",
                "Boosts the power of Grass-type moves when HP is low.",
                "blah"
            };
            String[] taxonomicCategories = {"Seed Pokémon", "Seed Pokémon", "Seed Pokémon",
                "Seed Pokemon"};

            // Loop through and insert data
            for (int i = 0; i < pokemonIDs.length; i++) {
                pstmt.setInt(1, pokemonIDs[i]); // Set PokemonID
                pstmt.setString(2, pokemonNames[i]); // Set PokemonName
                pstmt.setInt(3, plantTypes[i]); // Set PlantType (Foreign Key)
                pstmt.setString(4, speciesNames[i]); // Set SpeciesName
                pstmt.setInt(5, HPs[i]); // Set HP
                pstmt.setInt(6, attackCategories[i]); // Set AttackCategory (Foreign Key)
                pstmt.setString(7, attackDescriptions[i]); // Set AttackDesc
                pstmt.setInt(8, defenseCategories[i]); // Set DefenseCategory (Foreign Key)
                pstmt.setString(9, defenseDescriptions[i]); // Set DefenseDesc
                pstmt.setString(10, taxonomicCategories[i]); // Set TaxonomicCategory
                pstmt.executeUpdate(); // Execute the INSERT statement
            }

            System.out.println("Pokemon data has been inserted successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertPokemonSightingsData() {
        // SQL INSERT statement for the PokemonSightings table
        String insertSightingSql =
            "INSERT INTO PokemonSightings (SightingID, UserID, PokemonID, BiomeID, Longitude, Latitude, FoundDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertSightingSql)) {

            // Example data for the PokemonSightings table
            int[] sightingIDs_1 = {1, 2, 3}; // Unique IDs for each sighting
            int[] sightingIDs_2 = {4, 5, 6, 7}; // Unique IDs for each sighting

            int userID_1 = 1; // Assuming a single user with UserID = 1
            int userID_2 = 2; // Assuming a single user with UserID = 2
            int[] pokemonIDs = {1, 2, 3, 4}; // Corresponding to Bulbasaur, Ivysaur, Venusaur
            int[] biomeIDs = {1, 2, 3, 4}; // Adjusted Biome IDs
            double[] longitudes = {-73.935242, -0.127758, -122.419418,
                -122.419418,}; // Example longitudes
            double[] latitudes = {40.730610, 51.507351, 37.774929, 37.774929}; // Example latitudes

            // Directly create an array of Timestamps
            Timestamp[] foundDates_1 = {
                Timestamp.valueOf("2024-09-01 14:30:00"),
                Timestamp.valueOf("2024-08-25 10:00:00"),
                Timestamp.valueOf("2024-07-15 09:15:00")
            };

            Timestamp[] foundDates_2 = {
                Timestamp.valueOf("2024-09-01 14:30:00"),
                Timestamp.valueOf("2024-08-25 10:00:00"),
                Timestamp.valueOf("2024-07-15 09:15:00"),
                Timestamp.valueOf("2025-07-15 09:15:00")
            };

            // Loop through and insert data
            for (int i = 0; i < sightingIDs_1.length; i++) {
                pstmt.setInt(1, sightingIDs_1[i]); // Set SightingID
                pstmt.setInt(2, userID_1); // Set UserID (assuming 1 user)
                pstmt.setInt(3, pokemonIDs[i]); // Set PokemonID
                pstmt.setInt(4, biomeIDs[i]); // Set BiomeID
                pstmt.setDouble(5, longitudes[i]); // Set Longitude
                pstmt.setDouble(6, latitudes[i]); // Set Latitude
                pstmt.setTimestamp(7, foundDates_1[i]); // Set FoundDate
                pstmt.executeUpdate(); // Execute the INSERT statement
            }

//            // Loop through and insert data
            for (int i = 0; i < sightingIDs_2.length; i++) {
                pstmt.setInt(1, sightingIDs_2[i]); // Set SightingID
                pstmt.setInt(2, userID_2); // Set UserID (assuming 1 user)
                pstmt.setInt(3, pokemonIDs[i]); // Set PokemonID
                pstmt.setInt(4, biomeIDs[i]); // Set BiomeID
                pstmt.setDouble(5, longitudes[i]); // Set Longitude
                pstmt.setDouble(6, latitudes[i]); // Set Latitude
                pstmt.setTimestamp(7, foundDates_2[i]); // Set FoundDate
                pstmt.executeUpdate(); // Execute the INSERT statement
            }

            System.out.println("PokemonSightings data has been inserted successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean deletePokemon(int id) {
        String sql = "DELETE FROM Pokemon WHERE PokemonID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting Pokemon: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<attributeImage> getAttributeImagesByPokemonID(int pokemonID) {
        String sql = "SELECT * FROM attributeCategoriesImage "+
                        "WHERE AttributeID IN (SELECT AttackCategory FROM Pokemon WHERE PokemonID=?)"+
                        "OR AttributeID IN (SELECT DefenseCategory FROM Pokemon WHERE PokemonID=?);";
        ArrayList<attributeImage> attributeImage = new ArrayList<>();
        ResultSet rs = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pokemonID);
            stmt.setInt(2, pokemonID);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                attributeImage.add(new attributeImage(rs.getInt("AttributeID"), rs.getBytes("Image")));
            }
        }
        catch (SQLException e) {
            System.out.println("Error fetching Pokemon: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attributeImage;
    }

    public PokemonGeneratedImage getPokemonImageById(int id) {
        String sql = null;
        PokemonGeneratedImage pokemonImage = null;
        ResultSet rs = null;
        try {
            sql = "SELECT * FROM PokemonGeneratedImage WHERE PokemonID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs != null && rs.next()) {
                pokemonImage = new PokemonGeneratedImage(
                    rs.getInt("PokemonId"),
                    rs.getBytes("Image")
                );
            }

            if (pokemonImage == null) {
                stmt.setInt(1, -1);
                rs = stmt.executeQuery();
                if (rs != null && rs.next()) {
                    pokemonImage = new PokemonGeneratedImage(
                        rs.getInt("PokemonId"),
                        rs.getBytes("Image")
                    );
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error fetching Pokemon: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokemonImage;
    }

    public int sightingToPokemonID(int sightingID) {
        String sql = "SELECT PokemonID FROM PokemonSightings WHERE SightingID=?";
        ResultSet rs = null;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sightingID);
            rs = stmt.executeQuery();
            
            if (rs != null && rs.next()) {
                return rs.getInt("PokemonID");
            }
        }
        catch (SQLException e) {
            System.out.println("Error fetching Pokemon: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int pokemonSpeciesExists(String speciesName) {
        int pokemonid = -1;
        String sql = "SELECT * FROM Pokemon WHERE SpeciesName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, speciesName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                pokemonid = rs.getInt("PokemonID");
            }
        } catch (SQLException e) {
            System.out.println("Error checking species existence: " + e.getMessage());
        }
        return pokemonid;
    }

    public int insPokemon(Pokemon pokemon) {
        String sql = "INSERT INTO Pokemon (PokemonName, PlantType, SpeciesName, HP, AttackCategory, AttackDesc, DefenseCategory, DefenseDesc, TaxonomicCategory) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pokemon.getPokemonName());
            stmt.setInt(2, pokemon.getPlantType());
            stmt.setString(3, pokemon.getSpeciesName());
            stmt.setInt(4, pokemon.getHP());
            stmt.setInt(5, pokemon.getAttackCategory());
            stmt.setString(6, pokemon.getAttackDescription());
            stmt.setInt(7, pokemon.getDefenseCategory());
            stmt.setString(8, pokemon.getDefenseDescription());
            stmt.setString(9, pokemon.getTaxonomicCategory());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int pokemonId = generatedKeys.getInt(1);
                        return pokemonId;
                    } else {
                        throw new SQLException("Inserting Pokémon failed, no ID obtained.");
                    }
                }
            } else {
                return -1; // Indicate failure
            }
        } catch (SQLException e) {
            System.out.println("Error inserting Pokémon: " + e.getMessage());
            return -1;
        }
    }

    public boolean insertPokemonGeneratedImage(int pokemonId, byte[] imageData) {
        String sql = "INSERT INTO PokemonGeneratedImage (PokemonID, Image) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pokemonId);
            stmt.setBytes(2, imageData);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting Pokémon generated image: " + e.getMessage());
            return false;
        }
    }

    public boolean insertPokemonRealImage(int pokemonId, byte[] imageData) {
        String sql = "INSERT INTO PokemonRealImage (PokemonID, Image) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pokemonId);
            stmt.setBytes(2, imageData);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting Pokémon real image: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        Database db = new Database("/mnt/c/Users/William/Desktop/UniFiles/USYD/2024-Semester2/Java Application Frameworks/Mon-09-12-Lab-Group-3-1/backend/dex/test.db");
        db.insertPokemonData();
        db.insertPokemonSightingsData();
        ArrayList<LeaderboardEntry> entries = db.getLeaderboard();
        for (LeaderboardEntry entry : entries) {
            System.out.println(entry.getUserName());
            System.out.println(entry.getNumSightings());
            if (entry.getMostRecentSighting() != null) {
                System.out.println(entry.getMostRecentSighting().getFoundDate());
            } else {
                System.out.println("The User has no pokemon sightings");
            }
        }
    }
}
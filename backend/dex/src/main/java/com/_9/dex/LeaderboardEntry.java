package com._9.dex;

public class LeaderboardEntry {
  private String userName;
  private int numSightings;
  private int userID; 
  private PokemonSighting mostRecentSighting;

  public LeaderboardEntry(String userName) {
    this.userName = userName;
  }
  public LeaderboardEntry(String userName, int numSightings, int userID, PokemonSighting pokemonSighting) {
    this.userName = userName;
    this.numSightings = numSightings;
    this.mostRecentSighting = pokemonSighting;
    this.userID = userID; 
  }

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public int getUserID() {
    return this.userID;
  }

  public int getNumSightings() {
    return numSightings;
  }
  public void setNumSightings(int numSightings) {
    this.numSightings = numSightings;
  }
  public PokemonSighting getMostRecentSighting() {
    return mostRecentSighting;
  }
  public void setMostRecentSighting(PokemonSighting mostRecentSighting) {
    this.mostRecentSighting = mostRecentSighting;
  }

}

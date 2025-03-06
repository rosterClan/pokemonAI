package com._9.dex;

import java.sql.Blob;

public class PokemonGeneratedImage {

  private int pokemonID;
  private byte[] image;

  // Constructor
  public PokemonGeneratedImage(int pokemonID, byte[] image) {
    this.pokemonID = pokemonID;
    this.image = image;
  }

  // Getters and Setters
  public int getPokemonID() {
    return pokemonID;
  }

  public void setPokemonID(int pokemonID) {
    this.pokemonID = pokemonID;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

}

package com._9.dex;

import java.util.Date;

public class PokemonSighting {
    private int sightingID;
    private int pokemonID;
    private int userID;
    private int biomeID;
    private double longitude;
    private double latitude;
    private Date foundDate;

    public PokemonSighting(int sightingID, int pokemonID, int userID, int biomeID, double longitude, double latitude, Date foundDate) {
        this.sightingID = sightingID;
        this.pokemonID = pokemonID;
        this.userID = userID;
        this.biomeID = biomeID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.foundDate = foundDate;
    }

    //Getters
    public int getSightingID() {
        return this.sightingID;
    }
    public int getPokemonID() {
        return this.pokemonID;
    }
    public int getUserID() {
        return this.userID;
    }
    public int getBiomeID() {
        return this.biomeID;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public Date getFoundDate() {
        return this.foundDate;
    }

}

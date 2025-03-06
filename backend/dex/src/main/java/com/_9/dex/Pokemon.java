package com._9.dex;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pokemon {

    private int pokemonID;
    private String pokemonName;
    private int plantType;
    private String speciesName;
    @JsonProperty
    private int HP;
    private int attackCategory;
    private String attackDescription;
    private int defenseCategory;
    private String defenseDescription;
    private String taxonomicCategory;

    public Pokemon(){

    }
    public Pokemon(int pokemonID, String pokemonName, int plantType, String speciesName, int HP, int attackCategory, String attackDescription, int defenseCategory, String defenseDescription, String taxonomicCategory) {
        this.pokemonID = pokemonID;
        this.pokemonName = pokemonName;
        this.plantType = plantType;
        this.speciesName = speciesName;
        this.HP = HP;
        this.attackCategory = attackCategory;
        this.attackDescription = attackDescription;
        this.defenseCategory = defenseCategory;
        this.defenseDescription = defenseDescription;
        this.taxonomicCategory = taxonomicCategory;
    }

    //Getters

    public int getPokemonID() {
        return this.pokemonID;
    }

    public String getPokemonName() {
        return this.pokemonName;
    }

    public int getPlantType() {
        return this.plantType;
    }

    public String getSpeciesName() {
        return this.speciesName;
    }

    public int getHP() {
        return this.HP;
    }

    public int getAttackCategory() {
        return this.attackCategory;
    }

    public String getAttackDescription() {
        return this.attackDescription;
    }

    public int getDefenseCategory() {
        return this.defenseCategory;
    }

    public String getDefenseDescription() {
        return this.defenseDescription;
    }

    public String getTaxonomicCategory() {
        return this.taxonomicCategory;
    }

    // Setters
    public void setPokemonID(int pokemonID) {
        this.pokemonID = pokemonID;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public void setPlantType(int plantType) {
        this.plantType = plantType;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public void setAttackCategory(int attackCategory) {
        this.attackCategory = attackCategory;
    }

    public void setAttackDescription(String attackDescription) {
        this.attackDescription = attackDescription;
    }

    public void setDefenseCategory(int defenseCategory) {
        this.defenseCategory = defenseCategory;
    }

    public void setDefenseDescription(String defenseDescription) {
        this.defenseDescription = defenseDescription;
    }

    public void setTaxonomicCategory(String taxonomicCategory) {
        this.taxonomicCategory = taxonomicCategory;
    }

}

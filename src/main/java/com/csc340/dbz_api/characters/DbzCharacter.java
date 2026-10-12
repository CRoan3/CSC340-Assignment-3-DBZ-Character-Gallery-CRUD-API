package com.csc340.dbz_api.characters;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

// Named DbzCharacter (not Character) to avoid colliding with java.lang.Character.
@Entity
@Table(name = "characters")
public class DbzCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long characterId;

    @NotBlank
    private String name;

    @NotBlank
    @Column(length = 1000)
    private String description;

    // Extra attributes from the Assignment 2 gallery cards.
    private String species;

    private String role;

    private String affiliation;

    private String imageUrl;

    // Date the character first appeared in the series (yyyy-MM-dd).
    private LocalDate firstAppearance;

    public DbzCharacter() {
    }

    public Long getCharacterId() {
        return characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getFirstAppearance() {
        return firstAppearance;
    }

    public void setFirstAppearance(LocalDate firstAppearance) {
        this.firstAppearance = firstAppearance;
    }
}

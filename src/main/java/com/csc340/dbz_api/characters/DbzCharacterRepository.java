package com.csc340.dbz_api.characters;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DbzCharacterRepository extends JpaRepository<DbzCharacter, Long> {

    // Category filter: "saiyan" matches "Saiyan".
    List<DbzCharacter> findBySpeciesIgnoreCase(String species);

    // Partial, case-insensitive name search: "veg" finds "Vegeta".
    List<DbzCharacter> findByNameContainingIgnoreCase(String name);

    // Both filters at once.
    List<DbzCharacter> findByNameContainingIgnoreCaseAndSpeciesIgnoreCase(String name, String species);
}

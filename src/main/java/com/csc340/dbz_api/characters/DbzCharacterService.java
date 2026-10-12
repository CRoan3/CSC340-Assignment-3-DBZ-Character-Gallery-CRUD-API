package com.csc340.dbz_api.characters;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbzCharacterService {

    private final DbzCharacterRepository repository;

    public DbzCharacterService(DbzCharacterRepository repository) {
        this.repository = repository;
    }

    // Combines the optional filters. Blank or null arguments are ignored
    public List<DbzCharacter> search(String name, String species) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasSpecies = species != null && !species.isBlank();

        if (hasName && hasSpecies) {
            return repository.findByNameContainingIgnoreCaseAndSpeciesIgnoreCase(name.trim(), species.trim());
        }
        if (hasName) {
            return repository.findByNameContainingIgnoreCase(name.trim());
        }
        if (hasSpecies) {
            return repository.findBySpeciesIgnoreCase(species.trim());
        }
        return repository.findAll();
    }

    // find by id, create, update, delete methods
    public DbzCharacter findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new DbzCharacterNotFoundException(id));
    }

    public DbzCharacter create(DbzCharacter character) {
        return repository.save(character);
    }

    public DbzCharacter update(Long id, DbzCharacter updated) {
        DbzCharacter existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setSpecies(updated.getSpecies());
        existing.setRole(updated.getRole());
        existing.setAffiliation(updated.getAffiliation());
        existing.setImageUrl(updated.getImageUrl());
        existing.setFirstAppearance(updated.getFirstAppearance());
        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id); // throws 404 if missing
        repository.deleteById(id);
    }
}

package com.csc340.dbz_api.characters;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class DbzCharacterController {

    private final DbzCharacterService service;

    public DbzCharacterController(DbzCharacterService service) {
        this.service = service;
    }

    // GET /api/characters, ?name=veg, ?species=Saiyan, or both
    @GetMapping
    public List<DbzCharacter> list(@RequestParam(required = false) String name,
            @RequestParam(required = false) String species) {
        return service.search(name, species);
    }

    @GetMapping("/{id}")
    public DbzCharacter get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DbzCharacter create(@Valid @RequestBody DbzCharacter character) {
        return service.create(character);
    }

    @PutMapping("/{id}")
    public DbzCharacter update(@PathVariable Long id, @Valid @RequestBody DbzCharacter character) {
        return service.update(id, character);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

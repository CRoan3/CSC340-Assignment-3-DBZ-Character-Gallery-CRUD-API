package com.csc340.dbz_api.characters;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DbzCharacterNotFoundException extends RuntimeException {
    public DbzCharacterNotFoundException(Long id) {
        super("Character " + id + " not found");
    }
}

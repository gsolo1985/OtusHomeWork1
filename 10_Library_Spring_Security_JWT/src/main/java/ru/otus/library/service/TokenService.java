package ru.otus.library.service;

import org.springframework.security.core.Authentication;

public interface TokenService {
    String token(Authentication authentication);
}

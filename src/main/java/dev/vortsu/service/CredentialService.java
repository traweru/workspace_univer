package dev.barapp.service;

import dev.barapp.entities.CredentialEntity;
import dev.barapp.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialRepository credentialRepository;

    public Optional<CredentialEntity> findByEmail(String email) {
        return credentialRepository.findCredentialEntityByEmail(email);
    }


}

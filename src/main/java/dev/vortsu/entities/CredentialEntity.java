package dev.barapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.barapp.entities.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CredentialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Role role;

    private String email;

    @JsonIgnore
    private String password;
}

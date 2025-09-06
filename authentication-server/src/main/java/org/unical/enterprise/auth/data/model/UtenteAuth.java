package org.unical.enterprise.auth.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unical.enterprise.shared.dto.UtenteAuthDTO;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "utente_auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteAuth {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(length = 500, nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    public static UtenteAuthDTO toSharedDTO(UtenteAuth utenteAuth) {
        return UtenteAuthDTO.builder()
                .id(utenteAuth.getId())
                .username(utenteAuth.getUsername())
                .build();
    }

}

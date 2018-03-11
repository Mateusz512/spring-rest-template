package mateuszhinc.springRestTemplate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mateuszhinc.springRestTemplate.persistence.model.User;
import mateuszhinc.springRestTemplate.service.AuthorityService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public @Data class UserDTO {
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private List<AuthorityDTO> authorities;

    public User toEntity(AuthorityService authorityService, PasswordEncoder passwordEncoder) {
        return new User(authorityService.findByNames(
                authorities
                        .stream()
                        .map(authorityDTO -> authorityDTO.name)
                        .collect(Collectors.toList())),
                null,
                username,
                passwordEncoder.encode(password),
                true

        );
    }
}

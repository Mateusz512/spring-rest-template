package mateuszhinc.springRestTemplate.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mateuszhinc.springRestTemplate.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.*;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public @Data class User implements UserDetails {

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    List<Authority> authorities;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
    @SequenceGenerator(name = "userSequence", sequenceName = "userSequence", initialValue = 10)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public UserDTO toDTO() {
        return new UserDTO(id,username, password,
                authorities.stream()
                        .map(Authority::toDTO)
                        .collect(Collectors.toList()));
    }

    public static User fromPrincipal(Principal principal){
        OAuth2Authentication auth = (OAuth2Authentication) principal;
        return (User) auth.getPrincipal();
    }
}
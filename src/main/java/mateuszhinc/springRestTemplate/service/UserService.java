package mateuszhinc.springRestTemplate.service;

import mateuszhinc.springRestTemplate.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Optional<List<UserDTO>> getAllUsers();

    Optional<UserDTO> createUser(UserDTO user);

    Optional<UserDTO> updateUser(long id, UserDTO userDTO);

    boolean isValidUser(UserDTO userDTO);

    void deleteUser(long id);
}

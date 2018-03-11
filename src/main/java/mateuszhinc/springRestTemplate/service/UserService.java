package mateuszhinc.springRestTemplate.service;

import mateuszhinc.springRestTemplate.dto.AuthorityDTO;
import mateuszhinc.springRestTemplate.dto.UserDTO;
import mateuszhinc.springRestTemplate.persistence.model.User;
import mateuszhinc.springRestTemplate.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsername(username);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(User::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createOrUpdateUser(UserDTO user) {
        return userRepository.save(user.toEntity(authorityService, passwordEncoder))
                .toDTO();
    }

    public boolean isValidUser(UserDTO dto) {
        if (dto == null) {
            return false;
        }
        if (dto.getUsername() == null || dto.getUsername().isEmpty() ||
                dto.getPassword() == null || dto.getPassword().isEmpty()) {
            return false;
        }
        if (dto.getAuthorities() == null || dto.getAuthorities().isEmpty() || dto.getAuthorities().size() > 2) {
            return false;
        }
        for (AuthorityDTO auth : dto.getAuthorities()) {
            if (!(auth.name.equals("ADMIN") || auth.name.equals("USER")))
                return false;
        }
        return true;
    }


    public void delete(long id) {
        userRepository.delete(id);
    }
}
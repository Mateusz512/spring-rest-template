package mateuszhinc.springRestTemplate.service;

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

    public void delete(long id) {
        userRepository.delete(id);
    }
}
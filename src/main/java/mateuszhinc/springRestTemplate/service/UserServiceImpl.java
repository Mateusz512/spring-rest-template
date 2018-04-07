package mateuszhinc.springRestTemplate.service;

import mateuszhinc.springRestTemplate.dto.AuthorityDTO;
import mateuszhinc.springRestTemplate.dto.UserDTO;
import mateuszhinc.springRestTemplate.helpers.AppConst;
import mateuszhinc.springRestTemplate.helpers.Predicates;
import mateuszhinc.springRestTemplate.persistence.model.User;
import mateuszhinc.springRestTemplate.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserServiceImpl implements UserService {

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

    @Override
    public Optional<List<UserDTO>> getAllUsers() {
        return Optional.ofNullable(userRepository.findAll())
                .map(list -> list
                        .stream()
                        .map(User::toDTO)
                        .collect(Collectors.toList()));
    }

    @Override
    public Optional<UserDTO> createUser(UserDTO user) {
        return Optional.ofNullable(user)
                .filter(this::isValidUser)
                .map(dto -> userRepository.save(dto.toEntity(authorityService, passwordEncoder)))
                .map(User::toDTO);
    }

    @Override
    public Optional<UserDTO> updateUser(long id, UserDTO userDTO) {
        return Optional.ofNullable(userRepository.findOne(id))
                .map(user -> {
                    Optional.ofNullable(userDTO.getUsername())
                            .ifPresent(user::setUsername);
                    Optional.ofNullable(userDTO.getPassword())
                            .ifPresent(user::setPassword);
                    Optional.ofNullable(userDTO.getAuthorities())
                            .filter(Predicates::isNotEmpty)
                            .ifPresent(authorityDTOS -> {
                                        List<String> names = authorityDTOS.stream()
                                                .map(AuthorityDTO::getName)
                                                .collect(Collectors.toList());
                                        authorityService.findByNames(names)
                                                .ifPresent(user::setAuthorities);
                                    }
                            );
                    return userRepository.saveAndFlush(user);
                })
                .map(User::toDTO);
    }

    @Override
    public boolean isValidUser(UserDTO userDTO) {
        return Optional.ofNullable(userDTO)
                .filter(dto -> Optional.ofNullable(dto.getUsername())
                        .filter(username -> !username.isEmpty())
                        .isPresent())
                .filter(dto -> Optional.ofNullable(dto.getPassword())
                        .filter(password -> !password.isEmpty())
                        .isPresent())
                .filter(dto -> Optional.ofNullable(dto.getAuthorities())
                        .filter(Predicates::isNotEmpty)
                        .filter(authorities -> authorities.size() <= 2)
                        .filter(authorities -> authorities.stream()
                                .allMatch(authority -> Optional.ofNullable(authority.getName())
                                        .filter(authorityName -> AppConst.validAuthoritiesNames
                                                .stream().anyMatch(validName -> validName.equals(authorityName)))
                                        .isPresent()))
                        .isPresent())
                .isPresent();
    }

    @Override
    public void deleteUser(long id) {
        userRepository.delete(id);
    }
}
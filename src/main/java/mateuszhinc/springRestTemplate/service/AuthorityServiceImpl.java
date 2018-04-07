package mateuszhinc.springRestTemplate.service;

import mateuszhinc.springRestTemplate.helpers.Predicates;
import mateuszhinc.springRestTemplate.persistence.model.Authority;
import mateuszhinc.springRestTemplate.persistence.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Optional<List<Authority>> findByNames(List<String> names) {
        return Optional.ofNullable(names)
                .filter(Predicates::isNotEmpty)
                .map(list -> list.stream()
                        .map(authorityRepository::findOneByName)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }
}

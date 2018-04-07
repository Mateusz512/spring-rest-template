package mateuszhinc.springRestTemplate.service;

import mateuszhinc.springRestTemplate.persistence.model.Authority;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {
    Optional<List<Authority>> findByNames(List<String> names);
}

package mateuszhinc.springRestTemplate.service;

import mateuszhinc.springRestTemplate.persistence.model.Authority;
import mateuszhinc.springRestTemplate.persistence.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    public List<Authority> findByNames(List<String> names){
        return names.stream()
                .map(authorityRepository::findOneByName)
                .collect(Collectors.toList());
    }
}

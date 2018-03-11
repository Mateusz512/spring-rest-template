package mateuszhinc.springRestTemplate.persistence.repository;

import mateuszhinc.springRestTemplate.persistence.model.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Authority findOneByName(String name);
}

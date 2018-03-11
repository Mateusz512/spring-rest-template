package mateuszhinc.springRestTemplate.persistence.repository;

import mateuszhinc.springRestTemplate.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);
}


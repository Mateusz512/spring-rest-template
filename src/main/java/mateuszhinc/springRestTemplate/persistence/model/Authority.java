package mateuszhinc.springRestTemplate.persistence.model;

import lombok.Data;
import mateuszhinc.springRestTemplate.dto.AuthorityDTO;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "authorities")
public @Data class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users;

    public AuthorityDTO toDTO(){
        return new AuthorityDTO(name);
    }
}

package mateuszhinc.springRestTemplate.service

import mateuszhinc.springRestTemplate.dto.AuthorityDTO
import mateuszhinc.springRestTemplate.dto.UserDTO
import mateuszhinc.springRestTemplate.helpers.AppConst
import org.assertj.core.util.Lists
import spock.lang.Specification
import spock.lang.Unroll

import static mateuszhinc.springRestTemplate.helpers.AppConst.ROLE_ADMIN
import static mateuszhinc.springRestTemplate.helpers.AppConst.ROLE_USER

class UserServiceSpecification extends Specification {

    static List<List<AuthorityDTO>> validAuthorities = [[new AuthorityDTO(ROLE_USER)],
                                                        [new AuthorityDTO(ROLE_ADMIN)],
                                                        [new AuthorityDTO(ROLE_USER),
                                                         new AuthorityDTO(ROLE_ADMIN)]]
    static List<List<AuthorityDTO>> invalidAuthorities = [[new AuthorityDTO("random")],
                                                          [new AuthorityDTO("")],
                                                          [new AuthorityDTO("random"),
                                                           new AuthorityDTO(ROLE_USER)],
                                                          []]
    UserService service = new UserService()

    def "Correctly validates UserDTO"() {

        expect:
        service.isValidUser(dto) == isValid

        where:
        dto                                                     | isValid

        new UserDTO(null, null, null, null)                     | false
        new UserDTO(null, "name", "pwd", null)                  | false
        new UserDTO(null, "", "pwd", null)                      | false
        new UserDTO(null, "name", "pwd", null)                  | false
        new UserDTO(null, "name", "", null)                     | false
        new UserDTO(null, "name", "pwd", Lists.emptyList())     | false
        new UserDTO(null, "name", "pwd", invalidAuthorities[0]) | false
        new UserDTO(null, "name", "pwd", invalidAuthorities[1]) | false
        new UserDTO(null, "name", "pwd", invalidAuthorities[2]) | false
        new UserDTO(null, "name", "pwd", invalidAuthorities[3]) | false

        new UserDTO(null, "name", "pwd", validAuthorities[0])   | true
        new UserDTO(1, "name", "pwd", validAuthorities[1])   | true
        new UserDTO(7, "name", "pwd", validAuthorities[2])   | true
    }

}

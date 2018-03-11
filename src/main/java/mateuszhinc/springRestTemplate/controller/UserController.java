package mateuszhinc.springRestTemplate.controller;

import mateuszhinc.springRestTemplate.dto.UserDTO;
import mateuszhinc.springRestTemplate.persistence.model.User;
import mateuszhinc.springRestTemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<UserDTO>> getAll() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> createOrUpdate(@RequestBody UserDTO user){
        return new ResponseEntity<>(userService.createOrUpdateUser(user), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long id){
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}

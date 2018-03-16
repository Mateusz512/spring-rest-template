package mateuszhinc.springRestTemplate.controller;

import mateuszhinc.springRestTemplate.dto.UserDTO;
import mateuszhinc.springRestTemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user){
        return Optional.ofNullable(user)
                .filter(userService::isValidUser)
                .map(userService::createUser)
                .map( u -> new ResponseEntity<>(u,HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateUser(@PathVariable long id, @RequestBody UserDTO received){
        return userService.updateUser(id,received)
                .map(user -> new ResponseEntity<>(user,HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}

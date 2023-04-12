package it.develhope.Unittest.controller;

import it.develhope.Unittest.entities.User;
import it.develhope.Unittest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("")
    public @ResponseBody User createUser(@RequestBody User user){
        return userRepository.save(user);
    }



    @GetMapping("/{id}")
    public @ResponseBody Optional<User> userById(@PathVariable Long id){
        return userRepository.findById(id);
    }

    @PutMapping("/{id}")
    public@ResponseBody User editUser(@PathVariable long id, @RequestBody User user){
        user.setId(id);
        return userRepository.save(user);
    }


    @DeleteMapping("/{id}")
    public @ResponseBody void deleteById(@PathVariable Long id){
        userRepository.deleteById(id);
    }

}

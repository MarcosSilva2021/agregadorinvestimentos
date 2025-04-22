package dev.mar.agregadorinvestimentos.controller;

import dev.mar.agregadorinvestimentos.entity.User;
import dev.mar.agregadorinvestimentos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto ){
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId){
        var user = userService.getUserById(userId);

        if (user.isPresent()){
                return ResponseEntity.ok(user.get());
        } else {
                return ResponseEntity.notFound().build();
        }

    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(){
        var users = userService.listUsers();

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId) {
        userService.deleteByid(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserBYId(@PathVariable("userId") String userId, @RequestBody UpdateUserDto updateUserDto){
        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }


}

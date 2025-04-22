package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.controller.CreateUserDto;
import dev.mar.agregadorinvestimentos.controller.UpdateUserDto;
import dev.mar.agregadorinvestimentos.entity.User;
import dev.mar.agregadorinvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        // converter DTO -> Entity
        var entity = new User(
                //UUID.randomUUID(), //-- conflito de add UUID entre aqui e o proprio hibernete - que ja add o UUID na inserção
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null);
        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public List<User> listUser(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String userId){

        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void deleteByid(String userId){
        var id = UUID.fromString(userId);

        var userExists = userRepository.existsById(id);

        if (userExists){
            userRepository.deleteById(id);
        }
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto){
        var id = UUID.fromString(userId);

        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()){
            var user = userEntity.get();

            if (updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }

            if (updateUserDto.password() != null){
                user.setPassword(updateUserDto.password());
            }

            userRepository.save(user);

        }

    }

}

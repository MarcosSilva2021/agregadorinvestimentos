package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.entity.User;
import dev.mar.agregadorinvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> listUser(){
        return userRepository.findAll();
    }

}

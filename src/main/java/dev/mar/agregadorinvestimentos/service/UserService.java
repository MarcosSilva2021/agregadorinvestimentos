package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.controller.dto.CreateAccountDto;
import dev.mar.agregadorinvestimentos.controller.dto.CreateUserDto;
import dev.mar.agregadorinvestimentos.controller.dto.UpdateUserDto;
import dev.mar.agregadorinvestimentos.entity.Account;
import dev.mar.agregadorinvestimentos.entity.BillingAddress;
import dev.mar.agregadorinvestimentos.entity.User;
import dev.mar.agregadorinvestimentos.repository.AccountRepository;
import dev.mar.agregadorinvestimentos.repository.BillingAddressRepository;
import dev.mar.agregadorinvestimentos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    private AccountRepository accountRepository;

    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
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

    public void createAccount(String userId, CreateAccountDto createAccountDto) {

        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // converter DTO -> Entity
        var account = new Account(
                UUID.randomUUID(),
                createAccountDto.description(),
                user,
                null,
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                createAccountDto.street(),
                createAccountDto.number()
        );

        billingAddressRepository.save(billingAddress);
    }
}

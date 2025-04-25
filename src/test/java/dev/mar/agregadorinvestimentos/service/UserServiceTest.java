package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.controller.CreateUserDto;
import dev.mar.agregadorinvestimentos.entity.User;
import dev.mar.agregadorinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    // Padrão - 3 way
    // Arrange -- arruma as dependencias do teste
    // Act -- Trecho do código a ser testado
    // Assert -- Verifica se os testes foram executados - com os parametros corretos

    // para não depender de dependencias externas
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // criando subclasses
    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateUserWithSuccess(){

            //Arrange
            var entity = new User(
                    //UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null

            );
            doReturn(entity).when(userRepository).save(any());
            var input = new CreateUserDto("name", "email@email.com", "123");

            //Act
            var output = userService.createUser(input);
            assertNotNull(output) ;
            //Assert

        }

    }

}
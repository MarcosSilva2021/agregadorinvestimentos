package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.controller.CreateUserDto;
import dev.mar.agregadorinvestimentos.entity.User;
import dev.mar.agregadorinvestimentos.repository.UserRepository;
import jakarta.persistence.Column;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

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

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    // criando subclasses
    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateUserWithSuccess(){

            //Arrange
            var entity = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null

            );
            doReturn(entity).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto("name", "email@email.com", "123");

            //Act
            var output = userService.createUser(input);

            //Assert
            assertNotNull(output);

            // captura o usuario criando no teste
            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());

        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            //Arrange
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto("name", "email@email.com", "123");

            //Act e Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }

    }

   @Nested
    class getUserById {

       @Test
       @DisplayName("Should get user by id with success")
       void shouldGetUserByIdWithSuccess() {

           //Arrange
           var user = new User(
                   UUID.randomUUID(),
                   "username",
                   "email@email.com",
                   "password",
                   Instant.now(),
                   null

           );
           doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

           //Act
           var output = userService.getUserById(user.getUserId().toString());

           //Assert
           assertTrue(output.isPresent());
           assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
       }
   }
}
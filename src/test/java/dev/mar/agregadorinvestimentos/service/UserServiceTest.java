package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.controller.dto.CreateUserDto;
import dev.mar.agregadorinvestimentos.controller.dto.UpdateUserDto;
import dev.mar.agregadorinvestimentos.entity.User;
import dev.mar.agregadorinvestimentos.repository.UserRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
       @DisplayName("Should get user by id with success when optional is present")
       void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {

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

       @Test
       @DisplayName("Should get user by id with success when optional is empty")
       void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {

           //Arrange
           var userId = UUID.randomUUID();

           doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

           //Act
           var output = userService.getUserById(userId.toString());

           //Assert
           assertTrue(output.isEmpty());
           assertEquals(userId, uuidArgumentCaptor.getValue());
       }
   }


    @Nested
    class listUser {

        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {

            // Arrange
            var user1 = new User(
                    UUID.randomUUID(),
                    "username1",
                    "email1@email.com",
                    "password1",
                    Instant.now(),
                    null
            );
            var user2 = new User(
                    UUID.randomUUID(),
                    "username2",
                    "email2@email.com",
                    "password2",
                    Instant.now(),
                    null
            );
            var userList = List.of(user1, user2);
            doReturn(userList)
                    .when(userRepository)
                    .findAll();

            // Act
            var output = userService.listUser();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
            assertTrue(output.contains(user1));
            assertTrue(output.contains(user2));

            // Verifica se o método findAll foi chamado corretamente
            verify(userRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no users exist")
        void shouldReturnEmptyListWhenNoUsersExist() {

            // Arrange
            var userList = List.<User>of();  // Lista vazia
            doReturn(userList)
                    .when(userRepository)
                    .findAll();

            // Act
            var output = userService.listUser();

            // Assert
            assertNotNull(output);
            assertTrue(output.isEmpty());  // Verifica se a lista retornada está vazia

            // Verifica se o método findAll foi chamado corretamente
            verify(userRepository, times(1)).findAll();
        }
    }



    @Nested
   class listUsers {

       @Test
       @DisplayName("Should return all users with success")
       void shouldReturnAllUsersWithSuccess() {

           //Arrange
           var user = new User(
                   UUID.randomUUID(),
                   "username",
                   "email@email.com",
                   "password",
                   Instant.now(),
                   null

           );
           var userList = List.of(user);
           doReturn(userList)
                   .when(userRepository)
                   .findAll();

           //Act
           var output = userService.listUsers();

           //Assert
           assertNotNull(output);
           assertEquals(userList.size(), output.size());

       }
   }

   @Nested
   class deleteByid{

       @Test
       @DisplayName("should delete user with success when user exists")
       void shouldDeleteUserWithSuccessWhenUserExists() {

           //Arrange
          doReturn(true)
                   .when(userRepository)
                   .existsById(uuidArgumentCaptor.capture());

          doNothing()
                   .when(userRepository)
                   .deleteById(uuidArgumentCaptor.capture());

          var userId = UUID.randomUUID();

           //Act
          userService.deleteByid(userId.toString());

          // Assert
          var idList = uuidArgumentCaptor.getAllValues();
          assertEquals(userId, idList.get(0));
          assertEquals(userId, idList.get(1));

          verify(userRepository, times(1)).existsById(idList.get(0));
          verify(userRepository, times(1)).deleteById(idList.get(1));
       }

       @Test
       @DisplayName("should not delete user when user Not exists")
       void shouldNotDeleteUserWhenUserNotExists() {

           //Arrange
           doReturn(false)
                   .when(userRepository)
                   .existsById(uuidArgumentCaptor.capture());

           var userId = UUID.randomUUID();

           //Act
           userService.deleteByid(userId.toString());

           // Assert
           assertEquals(userId, uuidArgumentCaptor.getValue());

           verify(userRepository, times(1))
                   .existsById(uuidArgumentCaptor.getValue());

           verify(userRepository, times(0)).deleteById(any());
       }
   }

   @Nested
   class updateUserById{

       @Test
       @DisplayName("Should update user by id when user exists and username and password is filled")
       void shouldUpdateUserByWhenUserExistsAndUsernameAndPasswordIsFilled() {

           //Arrange
           var updateUserDto = new UpdateUserDto(
                   "newUsername",
                   "newPassword"
           );

           var user = new User(
                   UUID.randomUUID(),
                   "username",
                   "email@email.com",
                   "password",
                   Instant.now(),
                   null

           );
           doReturn(Optional.of(user))
                   .when(userRepository)
                   .findById(uuidArgumentCaptor.capture());

           doReturn(user)
                   .when(userRepository)
                   .save(userArgumentCaptor.capture());

           //Act
           userService.updateUserById(user.getUserId().toString(), updateUserDto);

           //Assert
           assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

           var userCaptured = userArgumentCaptor.getValue();

           assertEquals(updateUserDto.username(), userCaptured.getUsername());
           assertEquals(updateUserDto.password(), userCaptured.getPassword());

           verify(userRepository, times(1))
                   .findById(uuidArgumentCaptor.getValue());

           verify(userRepository, times(1))
                   .save(user);
       }

       @Test
       @DisplayName("Should not update when user not exists")
       void shouldNotUpdateUserWhenUserNotExists() {

           //Arrange
           var updateUserDto = new UpdateUserDto(
                   "newUsername",
                   "newPassword"
           );

           var userId = UUID.randomUUID();

           doReturn(Optional.empty())
                   .when(userRepository)
                   .findById(uuidArgumentCaptor.capture());

           //Act
           userService.updateUserById(userId.toString(), updateUserDto);

           //Assert
           assertEquals(userId, uuidArgumentCaptor.getValue());

           verify(userRepository, times(1))
                   .findById(uuidArgumentCaptor.getValue());

           verify(userRepository, times(0))
                   .save(any());
       }

   }

}
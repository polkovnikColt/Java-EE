package kma.topic2.junit.service.validatior;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.validation.UserValidator;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.*;
import org.springframework.util.Assert;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserValidationTest {
    @Mock
    private UserRepository userR;
    @InjectMocks
    private UserValidator userV;

    @Test
    void shouldCreateUser(){
        userV.validateNewUser(
                NewUser.builder()
                .login("login")
                .password("passwor")
                .fullName("fullName")
                .build()
        );
        Mockito.verify(userR).isLoginExists("login");
    }

    @Test
    void shouldThrowExceptionWhenLoginExists(){
       Mockito.when(userR.isLoginExists("login")).thenReturn(true);

       assertThatThrownBy(() -> userV.validateNewUser(
                   NewUser.builder()
                   .password("passwor")
                   .login("login")
                   .fullName("fullName")
                   .build())
       ).isInstanceOf(LoginExistsException.class);
    }

    @ParameterizedTest
    @MethodSource("testPasswords")
    void shouldThrowExceptionWhenPasswordIsInvalid(String pass){
        assertThatThrownBy(() -> userV.validateNewUser(
                NewUser.builder()
                        .password(pass)
                        .login("login")
                        .fullName("fullName")
                        .build())
        )
                .isInstanceOf(ConstraintViolationException.class);
    }

    private static Stream<Arguments> testPasswords(){
        return Stream.of(
                Arguments.of("p"),
                Arguments.of("pass#"),
                Arguments.of("p#"),
                Arguments.of("pads#asdasdasd")
        );
    }
}

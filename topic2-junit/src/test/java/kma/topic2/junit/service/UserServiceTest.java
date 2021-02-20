package kma.topic2.junit.service;

import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.model.User;
import kma.topic2.junit.repository.UserRepository;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import kma.topic2.junit.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userS;
    @Autowired
    private UserRepository userR;
    @SpyBean
    private UserValidator userV;

    @Test
    void shouldCreateUser() {
        userS.createNewUser(
                NewUser.builder()
                        .password("pass")
                        .login("login")
                        .fullName("fullName")
                        .build());
        Mockito.verify(userV).validateNewUser(ArgumentMatchers.any());
        MatcherAssert.assertThat(userR.getUserByLogin("login"), is(
                User.builder()
                        .password("pass")
                        .login("login")
                        .fullName("fullName")
                        .build()
        ));
    }
}

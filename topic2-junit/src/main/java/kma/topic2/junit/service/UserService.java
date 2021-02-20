package kma.topic2.junit.service;

import org.springframework.stereotype.Service;

import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.model.User;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.validation.UserValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public void createNewUser(final NewUser newUser) {
        userValidator.validateNewUser(newUser);
        final User user = userRepository.saveNewUser(newUser);
    }

    public User getUserByLogin(final String login) {
        return userRepository.getUserByLogin(login);
    }

}

package com.leibel.weatherapi.service;

import com.leibel.weatherapi.model.User;
import com.leibel.weatherapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private boolean checkUsername(String username) {
        final boolean existsByUsername = userRepository.existsByUsername(username);
        if (existsByUsername) {
            return false;
        }
        return true;
    }

    public User register(String username) {
        if(checkUsername(username)){
            return null;
        };
        User user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }

    public User login(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}

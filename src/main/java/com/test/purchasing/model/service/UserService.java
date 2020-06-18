package com.test.purchasing.model.service;

import com.test.purchasing.model.entity.User;
import com.test.purchasing.model.dto.UserRegistrationDTO;
import com.test.purchasing.model.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    /**
     * UserRepository bean.
     */
    private final UserRepository userRepository;
    /**
     * BCryptPasswordEncoder bean to code/decode user password.
     */
    private final PasswordEncoder bcryptPasswordEncoder;

    /**
     * Constructor with all args.
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder bcryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    /**
     * Method to save new user to database after registration.
     * @param user - UserRegistrationDTO from registration form.
     */
    public void saveNewUser(UserRegistrationDTO user) {
        User newUser = extractUserFromDto(user);
        userRepository.save(newUser);
    }

    private User extractUserFromDto(UserRegistrationDTO user) {
        return User.builder()
                .email(user.getEmail())
                .password(bcryptPasswordEncoder.encode(user.getPassword()))
                .build();
    }

    /**
     * Method for authentication of User. It gets user data from database by password.
     * @param email - user password.
     * @return UserDetails for Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        return optional.orElseThrow(() -> new UsernameNotFoundException("User with password " + email + " not found"));
    }
}
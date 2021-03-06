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

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Class uses repository for connection with database to deal with user entity and BCrypt encoder for user password.
 *
 * @author Lina Chentsova
 */
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
     *
     * @param user - UserRegistrationDTO from registration form.
     */
    public void saveNewUser(UserRegistrationDTO user) {
        User newUser = extractUserFromDto(user);
        userRepository.save(newUser);
    }

    /**
     * Method to convert UserRegistrationDTO from registration form to user entity.
     *
     * @param user - UserRegistrationDTO from registration form.
     * @return user entity.
     */
    private User extractUserFromDto(UserRegistrationDTO user) {
        return User.builder()
                .email(user.getEmail())
                .password(bcryptPasswordEncoder.encode(user.getPassword()))
                .name(user.getName())
                .balance(new BigDecimal(50))
                .build();
    }

    /**
     * Method for authentication of User. It gets user data from database by password.
     *
     * @param email - user password.
     * @return UserDetails for Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        return optional.orElseThrow(() -> new UsernameNotFoundException("User with password " + email + " not found"));
    }

    /**
     * Method for user balance replenishing.
     * @param user  - the one to replenish balance.
     * @param sum - sum to add to user balance.
     */
    public void replenishBalance(User user, Integer sum) {
        user.setLocalizedBalance(user.getLocalizedBalance().add(new BigDecimal(sum)));
        userRepository.save(user);
    }


    /**
     * Method to subtract order sum from user balance.
     * @param user - the one that pays for order.
     * @param orderSum - sum of order to pay for.
     */
    public void subtractBalance(User user, BigDecimal orderSum){
        user.setLocalizedBalance(user.getLocalizedBalance().subtract(orderSum));
        userRepository.save(user);
    }
}

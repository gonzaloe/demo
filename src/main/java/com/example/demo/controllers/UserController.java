package com.example.demo.controllers;

import com.example.demo.domains.BadRequestError;
import com.example.demo.domains.NotFoundError;
import com.example.demo.domains.User;
import com.example.demo.domains.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/users/")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        if (userRepository.findById(user.getEmail()).isPresent()) {
            logger.error("user with email {} is already registered", user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequestError());
        }
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(value = "/users/{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email) {
        logger.info("looking for user with email: {}", email);
        Optional<User> result = userRepository.findById(email);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            logger.error("user with email {} not found in database", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundError());
        }
    }

    @GetMapping(value = "/users")
    public ResponseEntity<Object> listUsers(@RequestParam String prefix, @RequestParam String suffix) {
        List<User> result;
        if (prefix != null) {
            result = userRepository.findAllByUsernameStartsWithOrderByUsername(prefix);
        } else if (suffix != null) {
            result = userRepository.findAllByUsernameEndsWithOrderByUsername(suffix);
        } else {
            result = new ArrayList<>();
        }

        logger.info("users found: {}", result);

        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/users/{email}")
    public ResponseEntity<Object> updateUser(@PathVariable String email, @RequestBody User newData) {
        Optional<User> result = userRepository.findById(email);

        if (!result.isPresent()) {
            logger.error("user with email {} not found in database", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundError());
        }

        User oldData = result.get();
        oldData.setPassword(newData.getPassword() != null ? newData.getPassword() : oldData.getPassword());
        oldData.setUsername(newData.getUsername() != null ? newData.getUsername() : oldData.getUsername());
        logger.info("user with email {} got updated", oldData.getEmail());
        userRepository.save(oldData);

        return ResponseEntity.status(HttpStatus.OK).body(oldData);
    }

    @DeleteMapping(value = "/users/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        Optional<User> result = userRepository.findById(email);

        if (!result.isPresent()) {
            logger.error("user with email {} not found in database", email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequestError());
        }

        User user = result.get();
        userRepository.delete(user);
        logger.info("user with email {} successfully deleted", user.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}

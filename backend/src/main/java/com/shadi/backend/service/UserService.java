package com.shadi.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shadi.backend.exception.DuplicateResourceException;
import com.shadi.backend.exception.ResourceNotFoundException;
import com.shadi.backend.model.User;
import com.shadi.backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id)
                );
    }
    public User createUser(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new DuplicateResourceException("User Email already exists:" + user.getEmail());
        }
        return userRepository.save(user);
    }
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User existingUser = getUserById(id);
        userRepository.delete(existingUser);
    }
}

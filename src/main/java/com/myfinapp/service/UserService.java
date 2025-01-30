package com.myfinapp.service;

import com.myfinapp.model.User;
import com.myfinapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails){
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setCreated_at(userDetails.getCreated_at());

            //check password best practices
            user.setPassword(userDetails.getPassword());
            return userRepository.save(user);
        }).orElseThrow(()-> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
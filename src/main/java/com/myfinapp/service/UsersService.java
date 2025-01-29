package com.myfinapp.service;

import com.myfinapp.model.Users;
import com.myfinapp.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional(readOnly = true)
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users createUser(Users users) {
        return usersRepository.save(users);
    }

    public Users updateUser(Long id, Users usersDetails){
        return usersRepository.findById(id).map(users -> {
            users.setName(usersDetails.getName());
            users.setEmail(usersDetails.getEmail());
            users.setCreated_at(usersDetails.getCreated_at());

            //check password best practices
            users.setPassword(usersDetails.getPassword());
            return usersRepository.save(users);
        }).orElseThrow(()-> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id){
        Users users = usersRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        usersRepository.delete(users);
    }
}
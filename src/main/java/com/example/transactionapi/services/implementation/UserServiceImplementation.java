package com.example.transactionapi.services.implementation;

import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.UserRepository;
import com.example.transactionapi.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UserServiceImplementation implements ShowService<User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User saveorUpdate(User user) {
        return null;
    }

    @Override
    public String deleteById(Integer id) {
        return null;
    }
}

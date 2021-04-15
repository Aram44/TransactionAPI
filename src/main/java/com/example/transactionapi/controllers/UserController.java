package com.example.transactionapi.controllers;

import com.example.transactionapi.models.User;
import com.example.transactionapi.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController implements Resource<User> {

    @Autowired
    private ShowService<User> userService;

    @Override
    @GetMapping("/allusers")
    public ResponseEntity<Page<User>> findAll(Pageable pageable) {
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> findByID(Integer id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> save(User user) {
        return new ResponseEntity<>(userService.saveOrUpdate(user), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<User> update(User user) {
        return new ResponseEntity<>(userService.saveOrUpdate(user), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteById(Integer id) {
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.OK);
    }
}

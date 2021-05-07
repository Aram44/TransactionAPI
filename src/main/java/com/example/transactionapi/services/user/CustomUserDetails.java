package com.example.transactionapi.services.user;

import com.example.transactionapi.exceptions.UserException;
import com.example.transactionapi.model.user.User;
import com.example.transactionapi.repository.user.UserRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class CustomUserDetails {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    CustomUserDetails(UserRepository userRepository, MessageSource messageSource){
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> {throw new UserException(messageSource.getMessage("user.not.found", new Object[]{id}, Locale.ENGLISH));});
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    public String deleteById(long id) {
        JSONObject jsonObject = new JSONObject();
        try {
            userRepository.deleteById(id);
            jsonObject.put("message", "User deleted successfully");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
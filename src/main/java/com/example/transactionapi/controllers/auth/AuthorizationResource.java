package com.example.transactionapi.controllers.auth;

import com.example.transactionapi.config.JwtTokenProvider;
import com.example.transactionapi.models.utils.AuthRequest;
import com.example.transactionapi.models.User;
import com.example.transactionapi.repository.UserRepository;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthorizationResource {

    @Autowired
    private JwtTokenProvider jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String welcome() {
        return "Welcome!";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        JSONObject jsonObject = new JSONObject();
        User user = userRepository.findByEmail(authRequest.getEmail());
        jsonObject.put("token", jwtUtil.generateToken(authRequest.getEmail(), userRepository.findByEmail(authRequest.getEmail()).getRole()));
        jsonObject.put("email",user.getEmail());
        jsonObject.put("role",user.getRole().getName());
        jsonObject.put("id",user.getId());
        return jsonObject.toString();
    }

    @PostMapping("/change")
    public String generateToken(@RequestBody User user) throws Exception {
        User chnage = userRepository.findById(user.getId()).get();
        if (chnage==null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "User not found");
            return jsonObject.toString();
        }else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "Password changed successfuly");
            return jsonObject.toString();
        }
    }
}

package com.example.demo.web;


import com.example.demo.models.User;
import com.example.demo.request.UserSignIN;
import com.example.demo.request.UserSignUp;
import com.example.demo.dto.AuthenticationResp;
import com.example.demo.services.UserClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserRestController {
    private final UserClientService userClientService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResp> login(@RequestBody UserSignIN login) {
        AuthenticationResp token = this.userClientService.login(login);
        return ResponseEntity.ok(token);
    }


    @PostMapping(path = "/user/signup")
    public ResponseEntity<User> signup(@RequestBody UserSignUp signup) throws Exception {
        User userClient = this.userClientService.addUser(signup);
        return ResponseEntity.ok(userClient);
    }
}

package com.example.demo.services;


import com.example.demo.models.User;
import com.example.demo.repo.UserRepo;
import com.example.demo.request.UserSignIN;
import com.example.demo.request.UserSignUp;
import com.example.demo.security.jwt.JwtConfig;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.dto.AuthenticationResp;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserClientService {
    private final UserRepo userRepository;
    private final AuthenticationManager authenticationManager;
    private final ApplicationUserService applicationUserService;
    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResp login(UserSignIN login) throws BadCredentialsException {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
            final UserDetails userDetails = this.applicationUserService.loadUserByUsername(login.getUsername());
            final String jwt = this.jwtUtil.generateToken(userDetails);
            return new AuthenticationResp(jwt);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password ", e);
        }
    }


    public User addUser(UserSignUp userClient) throws Exception {
        try {
            String password = userClient.getPassword();
            String newPassword = passwordEncoder.encode(password);

            User user = new User();
            user.setUsername(userClient.getUsername());
            user.setPassword(newPassword);
            user.setEmail(userClient.getEmail());
            return this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            throw new Exception("User not saved");
        }
    }

    public User getUserByUserName(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("user not found"));

    }
}

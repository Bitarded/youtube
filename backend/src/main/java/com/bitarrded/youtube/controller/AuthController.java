package com.bitarrded.youtube.controller;

import com.bitarrded.youtube.dto.LoginRequest;
import com.bitarrded.youtube.dto.RegisterRequest;
import com.bitarrded.youtube.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",OK);

    }
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
         authService.login(loginRequest);
//        return
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }
}

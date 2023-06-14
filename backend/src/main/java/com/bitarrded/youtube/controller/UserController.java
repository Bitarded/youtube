package com.bitarrded.youtube.controller;

import com.bitarrded.youtube.service.AuthService;
import com.bitarrded.youtube.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("subscribe/{userId}")
    public boolean subscribeUser(@PathVariable String userId) {
        userService.subscribeUser(userId);
        return true;
    }

    @PostMapping("unsubscribe/{userId}")
    public boolean unsubscribeUser(@PathVariable String userId) {
        userService.unsubscribeUser(userId);
        return true;
    }

    @GetMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> userHistory(@PathVariable(name = "userId") String userId) {
        return userService.userHistory(userId);
    }

}

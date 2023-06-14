package com.bitarrded.youtube.service;

import com.bitarrded.youtube.dto.LoginRequest;
import com.bitarrded.youtube.dto.RegisterRequest;
import com.bitarrded.youtube.exceptions.YoutubeException;
import com.bitarrded.youtube.model.NotificationEmail;
import com.bitarrded.youtube.model.User;
import com.bitarrded.youtube.model.VerificationToken;
import com.bitarrded.youtube.repository.UserRepository;
import com.bitarrded.youtube.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new YoutubeException("This email is already taken ");
        if(userRepository.findByUsername(request.getUsername()).isPresent())
            throw new YoutubeException("This username is already taken ");

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .created(Instant.now())
                .build();

        userRepository.save(user);
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate Your Account",
                user.getEmail(),
                "http://localhost:8080/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.expiresIn(15L);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new YoutubeException("Invalid Token"));
        enableAccount(verificationToken.get());
    }


    @Transactional
    void enableAccount(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new YoutubeException("User not found " + username));
        if (verificationToken.isExpired()) throw new YoutubeException("Token expired");
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = jwtProvider.generateToken(authenticate);
//        return new AuthenticationResponse(token,loginRequest.getUsername());

    }
}

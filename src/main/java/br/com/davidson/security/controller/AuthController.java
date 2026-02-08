package br.com.davidson.security.controller;

import br.com.davidson.security.config.TokenConfig;
import br.com.davidson.security.domain.User;
import br.com.davidson.security.dto.request.CreateUserRequest;
import br.com.davidson.security.dto.request.LoginRequest;
import br.com.davidson.security.dto.response.CreateUserResponse;
import br.com.davidson.security.dto.response.LoginResponse;
import br.com.davidson.security.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager; //responsavel por gerenciar autenticacao
    private final PasswordEncoder encoder;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder encoder, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password());
        Authentication authenticate = authenticationManager.authenticate(userAndPass);

        User principal = (User) authenticate.getPrincipal();
        String token = tokenConfig.generateToken(principal);
        return ResponseEntity.ok(new LoginResponse(token));

    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUserResponse(user.getId(), user.getName(), user.getEmail()));
    }
}

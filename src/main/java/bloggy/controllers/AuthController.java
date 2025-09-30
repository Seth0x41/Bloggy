package securitytut.bloggy.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securitytut.bloggy.domain.dtos.AuthResponse;
import securitytut.bloggy.domain.dtos.LoginRequest;
import securitytut.bloggy.services.AuthenticationService;

@RestController
@RequestMapping(path = "/api/v1/auth/login")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
    UserDetails userDetails= authenticationService
            .authenticate(loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
    String TokenValue = authenticationService.generateToken(userDetails);
    AuthResponse authResponse =AuthResponse.builder().token(TokenValue).expiresIn(86400).build();
    return ResponseEntity.ok(authResponse);
    }
}

package hexlet.code.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.app.dto.AuthRequest;
import hexlet.code.app.util.JWTUtils;

@RestController
@RequestMapping("/api/login")
public class AuthenticationController {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public String create(@RequestBody AuthRequest authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());

        authenticationManager.authenticate(authentication);

        return jwtUtils.generateToken(authRequest.getUsername());
    }
}

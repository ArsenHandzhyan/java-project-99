package hexlet.code.app.controller;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(exposedHeaders = "X-Total-Count")
@Tag(name = "Users", description = "Users management endpoints")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a user by its ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        UserDTO userDTO = userMapper.map(user.orElse(null));
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userPresenceDTOs = userMapper.map(users);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(users.size()));
        return ResponseEntity.ok().headers(responseHeaders).body(userPresenceDTOs);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserCreateDTO userData) {
        User existingUser = userService.getUserByEmail(userData.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Optional<User> createdUser = userService.createUser(userData);
        UserDTO userDTO = userMapper.map(createdUser.orElse(null));
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserUpdateDTO userData, @PathVariable Long id) {
        User update = userService.updateUser(id, userData);
        UserDTO userDTO = userMapper.map(update);
        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}

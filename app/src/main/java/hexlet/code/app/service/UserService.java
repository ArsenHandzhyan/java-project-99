package hexlet.code.app.service;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TaskRepository taskRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public User createUser(UserCreateDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }
    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    @Transactional
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    @Transactional
    public User updateUser(Long id, @Valid UserUpdateDTO userData) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(userData.getEmail());
                    if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userData.getPassword()));
                    }
                    return userRepository.save(user);
                })
                .orElse(null);
    }
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!taskRepository.findByAssignee(user).isEmpty()) {
            throw new IllegalStateException("Cannot delete user with assigned tasks");
        }

        userRepository.delete(user);
    }
}

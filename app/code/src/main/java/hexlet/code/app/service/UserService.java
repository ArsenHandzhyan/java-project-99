package hexlet.code.app.service;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       TaskRepository taskRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public Optional<User> createUser(UserCreateDTO dto) {
        User user = userMapper.map(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return Optional.of(userRepository.save(user));
    }

    @Transactional
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
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
        User user = userRepository.findById(id)
                .map(user1 -> {
                    user1.setEmail(userData.getEmail());
                    if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
                        user1.setPassword(passwordEncoder.encode(userData.getPassword()));
                    }
                    return userRepository.save(user1);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id" + id));
        userMapper.update(userData, user);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id" + id));

        if (!taskRepository.findByAssignee(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete user with assigned tasks");
        }

        userRepository.delete(user);
    }
}

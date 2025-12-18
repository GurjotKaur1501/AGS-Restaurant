package se.yrgo.userservice.service.impl;

import se.yrgo.userservice.dto.UserDTO;
import se.yrgo.userservice.dto.UserRequestDTO;
import se.yrgo.userservice.entity.User;
import se.yrgo.userservice.entity.UserPreferences;
import se.yrgo.userservice.repository.UserRepository;
import se.yrgo.userservice.repository.UserPreferencesRepository;
import se.yrgo.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPreferencesRepository preferencesRepository;

    @Override
    public UserDTO createUser(UserRequestDTO request) {
        log.info("Creating user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        // Create user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword()) // In production, should be encrypted
                .phone(request.getPhone())
                .build();

        User savedUser = userRepository.save(user);

//        // Create preferences if provided
//        if (request.getPreferredLanguage() != null || request.getNotificationPreference() != null) {
//            UserPreferences preferences = UserPreferences.builder()
//                    .user(savedUser)
////                    .preferredLanguage(request.getPreferredLanguage())
////                    .notificationPreference(request.getNotificationPreference())
////                    .marketingEmails(request.getMarketingEmails() != null ? request.getMarketingEmails() : false)
//                    .timeZone("Europe/Stockholm")
//                    .theme("light")
//                    .build();
//
//            preferencesRepository.save(preferences);
//            savedUser.setPreferences(preferences);
//        }

        log.info("User created with ID: {}", savedUser.getId());
        return mapToDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserRequestDTO request) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update user fields
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }

        User updated = userRepository.save(user);

        // Update preferences if provided
//        if (request.getPreferredLanguage() != null || request.getNotificationPreference() != null) {
//            UserPreferences preferences = preferencesRepository.findByUserId(id)
//                    .orElse(UserPreferences.builder()
//                            .user(updated)
//                            .timeZone("Europe/Stockholm")
//                            .theme("light")
//                            .build());
//
//            preferences.setPreferredLanguage(request.getPreferredLanguage());
//            preferences.setNotificationPreference(request.getNotificationPreference());
//            preferences.setMarketingEmails(request.getMarketingEmails());
//
//            preferencesRepository.save(preferences);
//        }

        log.info("User updated with ID: {}", updated.getId());
        return mapToDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted with ID: {}", id);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
//                .updatedAt(user.getUpdatedAt())
                .build();

//        // Add preferences if they exist
//        if (user.getPreferences() != null) {
//            dto.setPreferredLanguage(user.getPreferences().getPreferredLanguage());
//            dto.setNotificationPreference(user.getPreferences().getNotificationPreference());
//            dto.setMarketingEmails(user.getPreferences().getMarketingEmails());
//        }

        return dto;
    }
}
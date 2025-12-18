package se.yrgo.userservice.service;

import se.yrgo.userservice.dto.UserDTO;
import se.yrgo.userservice.dto.UserRequestDTO;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserRequestDTO request);
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserRequestDTO request);
    void deleteUser(Long id);
}
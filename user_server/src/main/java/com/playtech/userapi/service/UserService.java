package com.playtech.userapi.service;


import com.playtech.userapi.dto.UserDTO;
import com.playtech.userapi.model.User;
import com.playtech.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO request) {
        // Validación: Email ya está registrado
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email.");
        }

        // Validación: Fecha de nacimiento no puede ser futura
        if (request.getDateBirth() != null && request.getDateBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }

        User user = mapToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return mapToResponse(repository.save(user));
    }

    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return mapToResponse(user);
    }

    public UserDTO updateUser(Long id, UserDTO request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validación: Email ya está en uso por otro usuario
        if (!user.getEmail().equals(request.getEmail())) {
            boolean emailExists = repository.findByEmail(request.getEmail())
                    .filter(existing -> !existing.getId().equals(id))
                    .isPresent();

            if (emailExists) {
                throw new IllegalArgumentException("El correo ya está en uso por otro usuario.");
            }

            user.setEmail(request.getEmail());
        }

        // Validación: Fecha de nacimiento
        if (request.getDateBirth() != null && request.getDateBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateBirth(request.getDateBirth());
        user.setAddress(request.getAddress());
        user.setToken(request.getToken());
        user.setMobilePhone(request.getMobilePhone());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setActivo(request.getActivo() != null ? request.getActivo() : true);

        return mapToResponse(repository.save(user));
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: usuario no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    private User mapToEntity(UserDTO dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateBirth(dto.getDateBirth())
                .address(dto.getAddress())
                .mobilePhone(dto.getMobilePhone())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .activo(true)
                .build();
    }
    

    public UserDTO mapToResponse(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateBirth(user.getDateBirth())
                .address(user.getAddress())
                .token(user.getToken())
                .mobilePhone(user.getMobilePhone())
                .email(user.getEmail())
                .password(user.getPassword()) // <-- sí, incluirla hasheada como pediste
                .activo(user.getActivo())
                .creadoEn(user.getCreadoEn())
                .actualizadoEn(user.getActualizadoEn())
                .build();
    }

}

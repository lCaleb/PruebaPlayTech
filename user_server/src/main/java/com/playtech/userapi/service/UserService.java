package com.playtech.userapi.service;

import com.playtech.userapi.dto.UserDTO;
import com.playtech.userapi.exception.ApiException;
import com.playtech.userapi.model.User;
import com.playtech.userapi.repository.UserRepository;
import com.playtech.userapi.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiException(Constants.EMAIL_IN_USE, HttpStatus.BAD_REQUEST);
        }

        if (request.getDateBirth() != null && request.getDateBirth().isAfter(LocalDate.now())) {
            throw new ApiException(Constants.FUTURE_BIRTH_DATE, HttpStatus.BAD_REQUEST);
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
                .orElseThrow(() -> new ApiException(Constants.USER_NOT_FOUND_ID, HttpStatus.NOT_FOUND));
        return mapToResponse(user);
    }

    public UserDTO updateUser(Long id, UserDTO request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ApiException(Constants.USER_NOT_FOUND_ID, HttpStatus.NOT_FOUND));

        if (!user.getEmail().equals(request.getEmail())) {
            boolean emailExists = repository.findByEmail(request.getEmail())
                    .filter(existing -> !existing.getId().equals(id))
                    .isPresent();

            if (emailExists) {
                throw new ApiException(Constants.EMAIL_ALREADY_USED_BY_ANOTHER, HttpStatus.BAD_REQUEST);
            }

            user.setEmail(request.getEmail());
        }

        if (request.getDateBirth() != null && request.getDateBirth().isAfter(LocalDate.now())) {
            throw new ApiException(Constants.FUTURE_BIRTH_DATE, HttpStatus.BAD_REQUEST);
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
            throw new ApiException(Constants.USER_CANNOT_BE_DELETED + id, HttpStatus.NOT_FOUND);
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
                .password(user.getPassword())
                .activo(user.getActivo())
                .creadoEn(user.getCreadoEn())
                .actualizadoEn(user.getActualizadoEn())
                .build();
    }
}

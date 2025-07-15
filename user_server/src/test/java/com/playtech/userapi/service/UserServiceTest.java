package com.playtech.userapi.service;

import com.playtech.userapi.dto.UserDTO;
import com.playtech.userapi.exception.ApiException;
import com.playtech.userapi.model.User;
import com.playtech.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import com.playtech.userapi.exception.ApiException;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearUsuario_conDatosValidos_devuelveUsuarioDTO() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("123456");
        dto.setFirstName("Test");
        dto.setLastName("User");

        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDTO creado = userService.createUser(dto);

        // Assert
        assertEquals("test@test.com", creado.getEmail());
        assertEquals("hashedPassword", creado.getPassword());
        verify(repository).save(any(User.class));
    }

    @Test
    void crearUsuario_emailExistente_lanzaExcepcion() {
        UserDTO dto = new UserDTO();
        dto.setEmail("existe@test.com");

        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

       ApiException ex = assertThrows(ApiException.class, () -> userService.createUser(dto));

        assertEquals("Ya existe un usuario con ese email.", ex.getMessage());
    }
}

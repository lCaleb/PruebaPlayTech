package com.playtech.userapi.service;


import com.playtech.userapi.dto.AuthResponseDTO;
import com.playtech.userapi.dto.UserDTO;
import com.playtech.userapi.model.User;
import com.playtech.userapi.repository.UserRepository;
import com.playtech.userapi.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    
    @Transactional
    public AuthResponseDTO login(UserDTO request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        String accessToken = jwtProvider.generateToken(auth);
        user.setToken(accessToken);
        userRepository.save(user);
    
        UserDTO userDTO = userService.mapToResponse(user); // Usamos el método del paso 3
    
        return AuthResponseDTO.builder()
                .access_token(accessToken)
                .token_type("borear")
                .user(userDTO)
                .build();
    }
}

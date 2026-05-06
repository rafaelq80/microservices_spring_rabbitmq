package com.rfl.auth_service.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rfl.auth_service.dto.AuthResponse;
import com.rfl.auth_service.dto.LoginRequest;
import com.rfl.auth_service.model.Usuario;
import com.rfl.auth_service.repository.UsuarioRepository;
import com.rfl.auth_service.security.JwtService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository     = usuarioRepository;
        this.jwtService            = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        // O AuthenticationManager delega ao DaoAuthenticationProvider,
        // que chama o UserDetailsServiceImpl e valida a senha com BCrypt.
        // Lança AuthenticationException automaticamente se as credenciais forem inválidas.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.usuario(), request.senha())
        );

        // Credenciais válidas: busca o usuário para obter o role e gerar o token.
        Usuario usuario = usuarioRepository.findByUsuario(request.usuario())
            .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação"));

        String token = gerarToken(usuario.getUsuario(), usuario.getRole().name());
        
        return new AuthResponse(token);
    }

    private String gerarToken(String usuario, String role) {
		return "Bearer " + jwtService.generateToken(usuario, role);	
	}
}
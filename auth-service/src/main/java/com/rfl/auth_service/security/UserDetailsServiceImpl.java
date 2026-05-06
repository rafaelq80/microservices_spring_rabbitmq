package com.rfl.auth_service.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rfl.auth_service.model.Usuario;
import com.rfl.auth_service.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // Injeção por construtor: remove a necessidade de @Autowired em campo
    // e garante que o repositório nunca seja nulo após a construção do bean.
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("Usuário (e-mail) não pode ser vazio");
        }

        Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);

        return usuario
            .map(UserDetailsImpl::new)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}
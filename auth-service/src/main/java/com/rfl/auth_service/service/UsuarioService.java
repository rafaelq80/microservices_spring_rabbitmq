package com.rfl.auth_service.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rfl.auth_service.dto.UsuarioCreateDTO;
import com.rfl.auth_service.dto.UsuarioResponseDTO;
import com.rfl.auth_service.dto.UsuarioUpdateDTO;
import com.rfl.auth_service.exception.RecursoNaoEncontradoException;
import com.rfl.auth_service.model.Usuario;
import com.rfl.auth_service.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public UsuarioResponseDTO criarUsuario(UsuarioCreateDTO dto) {

        if (repository.existsByUsuario(dto.usuario())) {
            throw new IllegalArgumentException(
                    "Usuário '" + dto.usuario() + "' já está em uso"
            );
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setUsuario(dto.usuario());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setFoto(dto.foto());
        usuario.setRole(dto.role());

        return toResponseDTO(repository.save(usuario));
    }

    // FIND BY ID
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );
        return toResponseDTO(usuario);
    }

    // FIND ALL
    public List<UsuarioResponseDTO> listarUsuarios() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // UPDATE
    public UsuarioResponseDTO atualizarUsuario(UsuarioUpdateDTO dto) {

        Usuario usuario = repository.findById(dto.id())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );

        if (dto.usuario() != null) {
            boolean usuarioAlterado = !dto.usuario().equalsIgnoreCase(usuario.getUsuario());
            if (usuarioAlterado && repository.existsByUsuario(dto.usuario())) {
                throw new IllegalArgumentException(
                        "Usuário '" + dto.usuario() + "' já está em uso"
                );
            }
            usuario.setUsuario(dto.usuario());
        }

        usuario.setNome(dto.nome());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setFoto(dto.foto());
		
        if (dto.role() != null) {
			usuario.setRole(dto.role());
		}

        if (dto.ativo() != null) {
        	usuario.setAtivo(dto.ativo());
        }

        return toResponseDTO(repository.save(usuario));
    }

    // DELETE
    public void deletarUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        repository.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

 // Mapper interno
    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getUsuario(),
                usuario.getFoto(),
                usuario.getRole(),
                usuario.isAtivo(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm()
        );
    }
}
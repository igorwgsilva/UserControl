/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author igor Wendling
 */
import model.StatusUsuario;
import model.Usuario;
import repository.IUsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrarNovoUsuario(Usuario novoUsuario) {
        
        if (usuarioRepository.buscarPorNomeDeUsuario(novoUsuario.getNomeDeUsuario()) != null) {
            throw new IllegalArgumentException("Nome de usuario '" + novoUsuario.getNomeDeUsuario() + "' ja está em uso. Escolha outro nome.");
        }
        
        if (!usuarioRepository.existeUsuarioCadastrado()) {
            novoUsuario.setTipoPerfil("ADMINISTRADOR");
            novoUsuario.setStatus(StatusUsuario.AUTORIZADO);
        } else {
            novoUsuario.setTipoPerfil("PADRAO");
            novoUsuario.setStatus(StatusUsuario.PENDENTE_AUTORIZACAO); 
        }

        novoUsuario.setDataCadastro(LocalDateTime.now());
        novoUsuario.setNotificacoesRecebidas(0);
        novoUsuario.setNotificacoesLidas(0);

        try {
            usuarioRepository.salvar(novoUsuario);
        } catch (RuntimeException e) {
            throw new RuntimeException("Falha ao salvar o novo usuário no banco de dados.", e);
        }
        
        return novoUsuario;
    }

    public Usuario autorizarUsuario(String nomeDeUsuario) {
        Usuario usuario = usuarioRepository.buscarPorNomeDeUsuario(nomeDeUsuario);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario '" + nomeDeUsuario + "' nao encontrado.");
        }

        if (usuario.getStatus() == StatusUsuario.AUTORIZADO) {
            throw new IllegalArgumentException("Usuario '" + nomeDeUsuario + "' ja esta AUTORIZADO.");
        }
        
        usuario.setStatus(StatusUsuario.AUTORIZADO);

        try {
            usuarioRepository.atualizar(usuario);
        } catch (RuntimeException e) {
            throw new RuntimeException("Falha ao atualizar o status do usuario no banco de dados.", e);
        }

        return usuario;
    }

    public Usuario autenticarUsuario(String nomeDeUsuario, String senhaEmTextoPuro) {
        Usuario usuario = usuarioRepository.buscarPorNomeDeUsuario(nomeDeUsuario);

        if (usuario == null) {
            return null;
        }

        if (usuario.getStatus() != StatusUsuario.AUTORIZADO) {
            throw new IllegalStateException("Usuário nao autorizado ou pendente de ativacao.");
        }

        if (senhaEmTextoPuro.equals(usuario.getSenha())) {
            return usuario;
        } else {
            return null;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author igor Wendling
 */
import java.util.List;
import model.Usuario;

public interface IUsuarioRepository {
    
    boolean existeUsuarioCadastrado(); 
    void salvar(Usuario usuario); 
    Usuario buscarPorNomeDeUsuario(String nomeDeUsuario);
    List<Usuario> buscarTodos(); 
    void atualizar(Usuario usuario);
}

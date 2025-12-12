/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.List;
import java.util.Optional;
import model.Usuario;

/**
 * 
 * @author igor Wendling
 */
public interface IUsuarioRepository {

    boolean existeUsuarioCadastrado();  //retorna verdadeiro se existe algum usuario cadastrado
    
    void salvar(Usuario usuario); 
    
    Usuario buscarPorNomeDeUsuario(String nomeDeUsuario);
    Optional<Usuario> buscarPorId(int id);;
    List<Usuario> buscarTodos(); 
    
    void atualizar(Usuario usuario);

    void excluir(int id);

    public Optional<Usuario> buscarPrimeiroAdministrador();
}
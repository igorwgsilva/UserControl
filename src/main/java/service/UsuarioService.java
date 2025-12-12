/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author igor Wendling
 */
import model.Usuario;
import repository.IUsuarioRepository;
import java.util.List;
import java.util.Optional;
import model.IPerfilUsuario;
import model.PerfilAdministrador;
import model.PerfilComum;

public class UsuarioService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    

     public Usuario buscarPorNomeDeUsuario(String nomeDeUsuario) {
        if (nomeDeUsuario == null || nomeDeUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome de usuário não pode ser vazio.");
        }
        
        return usuarioRepository.buscarPorNomeDeUsuario(nomeDeUsuario);
    }
    
  
    public boolean existeUsuarioCadastrado() {
        return usuarioRepository.existeUsuarioCadastrado();
    }
    
    public void cadastrarNovoUsuario(Usuario novoUsuario) {
        
        // Validação de duplicidade
        if (usuarioRepository.buscarPorNomeDeUsuario(novoUsuario.getNomeUsuario()) != null) {
            throw new IllegalArgumentException("Nome de usuario '" + novoUsuario.getNomeUsuario() + "' ja esta em uso. Escolha outro nome.");
        }
        
       // Regra do Primeiro Usuário (RNF/Regra de Negócio)
        if (!usuarioRepository.existeUsuarioCadastrado()) {
            // Primeiro usuário é Admin e Autorizado
            novoUsuario.setPerfil(new PerfilAdministrador());
            novoUsuario.setAutorizado(true);
        } else {
            // Demais usuários são Comuns e Pendentes (não autorizados)
            novoUsuario.setPerfil(new PerfilComum());
            novoUsuario.setAutorizado(false); 
        }

        try {
            usuarioRepository.salvar(novoUsuario);
        } catch (RuntimeException e) {
            throw new RuntimeException("Falha ao salvar o novo usuário no banco de dados.", e);
        }
        
    }

    public void atualizarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário para atualização não pode ser nulo.");
        }
        
      
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ficar vazio.");
        }

        try {
           
            usuarioRepository.atualizar(usuario);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar os dados do usuário no banco.", e);
        }
    }
    
    
     
   
    
    public void autorizarUsuario(String nomeDeUsuario) {
        // Busca usando o método do repositório
        Usuario usuario = usuarioRepository.buscarPorNomeDeUsuario(nomeDeUsuario);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario '" + nomeDeUsuario + "' nao encontrado.");
        }

        // Verifica boolean autorizado
        if (usuario.isAutorizado()) {
            throw new IllegalArgumentException("Usuario '" + nomeDeUsuario + "' ja esta AUTORIZADO.");
        }
        
        // Autoriza
        usuario.setAutorizado(true);

        try {
            usuarioRepository.atualizar(usuario); 
        } catch (RuntimeException e) {
            throw new RuntimeException("Falha ao atualizar o status do usuario no banco de dados.", e);
        }

        
    }
////////////////////////////////////§===============================================================
    public boolean autenticarUsuario(String nomeDeUsuario, String senhaEmTextoPuro) { // VERIFICAR SE É NECESSARIO UMA CLASSE LOGINSERVICE PARA ESSE METODO, PODE FERIR PRINCIPIO DA RESPONSABILIDADE UNICA
       
        Usuario usuario = usuarioRepository.buscarPorNomeDeUsuario(nomeDeUsuario);

        // Se o usuário não existir, retorna falso imediatamente
        if (usuario == null) {
            return false;
        }

        // Verifica a autorização usando o boolean da classe Usuario
        if (!usuario.isAutorizado()) {
            throw new IllegalStateException("Usuário pendente de autorização. Contate o administrador.");
        }

        //retorna true se usuario e senha batem
        return senhaEmTextoPuro.equals(usuario.getSenha());
    }
    
    ///====================================================================================================
    
    
    public List<Usuario> buscarTodos() {
        return usuarioRepository.buscarTodos(); // Assume que o repositório tem este método
    }
    
    public void excluirUsuario(Usuario usuarioExecutor, Usuario usuarioParaExcluir) {
        
        //apenas Admin pode excluir
        //admin não pode excluir a si mesmo
        
        if (!usuarioExecutor.isAdministrador()) {
             throw new RuntimeException("Apenas administradores podem excluir usuários.");
        }
        if(usuarioExecutor.getId() == usuarioParaExcluir.getId()){
                throw new RuntimeException("Não é possível excluir o proprio usuario");
            }
         if(isPrimeiroAdmin(usuarioParaExcluir)){
                throw new RuntimeException("Não é possível excluir o Administrador Supremo");
         }
        
        
        usuarioRepository.excluir(usuarioParaExcluir.getId());
    }

    private boolean isPrimeiroAdmin(Usuario usuarioParaExcluir) { //verifica se é o primeiro usuario administrador
        Optional<Usuario> primeiro = usuarioRepository.buscarPrimeiroAdministrador();
        
        return primeiro.isPresent() && primeiro.get().getId() == usuarioParaExcluir.getId();
    }
    
    
    public void alterarPerfil(Usuario alterador, Usuario aSerAlterado, IPerfilUsuario perfilNovo){
         if(!alterador.isAdministrador()){
                throw new RuntimeException("Somente Administradore Supremo pode alterar perfis.");
            }
         if(alterador.getId() == aSerAlterado.getId()){
                throw new RuntimeException("Não é possível alterar o próprio usuario");
            }
         
         aSerAlterado.setPerfil(perfilNovo);
         usuarioRepository.atualizar(aSerAlterado);
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 *
 * @author Igor Wendling
 */
import model.Usuario;
import service.UsuarioService;
import view.ILoginView;

public class LoginPresenter {

    private final ILoginView view;
    private final UsuarioService usuarioService;

    public LoginPresenter(ILoginView view, UsuarioService usuarioService) {
        this.view = view;
        this.usuarioService = usuarioService;
        
        verificarCadastroInicial(); 
    }

    private void verificarCadastroInicial() {
        if (!usuarioService.existeUsuarioCadastrado()) {
            view.abrirAutoCadastroView();

            view.fechar(); 
        }
    }

    /**
     * US 02: Lógica de autenticação.
     */
    public void realizarLogin() {
        String nomeUsuario = view.getNomeUsuario();
        String senha = view.getSenha();

        if (nomeUsuario.isEmpty() || senha.isEmpty()) {
            view.exibirMensagemErro("Preencha todos os campos para fazer login.");
            return;
        }

        try {
            // O Service retorna true ou false se a senha bater, e lança exceção se não estiver autorizado.
            boolean autenticado = usuarioService.autenticarUsuario(nomeUsuario, senha);

            if (autenticado) {
                // Se a autenticação foi bem-sucedida, busca o usuário completo para passar à MainView.
                Usuario usuarioAutenticado = usuarioService.buscarPorNomeDeUsuario(nomeUsuario);
                
                view.abrirJanelaPrincipal(usuarioAutenticado);
                view.fechar();
                // Log de sucesso
            } else {
                view.exibirMensagemErro("Nome de usuário ou senha inválidos.");
                // Log de falha
            }
        } catch (IllegalStateException e) {
            // Captura o erro do Service se o usuário não estiver AUTORIZADO (US 02)
            view.exibirMensagemErro("Acesso negado: " + e.getMessage());
            // Log de falha
        } catch (RuntimeException e) {
            view.exibirMensagemErro("Erro interno ao tentar autenticar.");
            // Log de falha de persistência
        }
    }
}

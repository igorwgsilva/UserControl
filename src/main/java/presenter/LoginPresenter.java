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

    public void realizarLogin() {
        String nomeUsuario = view.getNomeUsuario();
        String senha = view.getSenha();

        if (nomeUsuario.isEmpty() || senha.isEmpty()) {
            view.exibirMensagemErro("Preencha todos os campos para fazer login.");
            return;
        }

        try {

            boolean autenticado = usuarioService.autenticarUsuario(nomeUsuario, senha);

            if (autenticado) {
                Usuario usuarioAutenticado = usuarioService.buscarPorNomeDeUsuario(nomeUsuario);
                
                view.abrirJanelaPrincipal(usuarioAutenticado);
                view.fechar();
            } else {
                view.exibirMensagemErro("Nome de usuário ou senha inválidos.");
            }
        } catch (IllegalStateException e) {
            view.exibirMensagemErro("Acesso negado: " + e.getMessage());
        } catch (RuntimeException e) {
            view.exibirMensagemErro("Erro interno ao tentar autenticar.");
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 *
 * @author 
 */

import model.Usuario;
import service.UsuarioService;
import view.IAutoCadastroView;
import model.PerfilComum;

public class AutoCadastroPresenter {

    private final IAutoCadastroView view;
    private final UsuarioService usuarioService;

    public AutoCadastroPresenter(IAutoCadastroView view, UsuarioService usuarioService /*, IValidadorSenha validador */) {
        this.view = view;
        this.usuarioService = usuarioService;
    }

    public void cadastrarUsuario() {
        String nome = view.getNomeCompleto();
        String username = view.getUsername();
        String senha = view.getSenha();

        if (nome.isEmpty() || username.isEmpty() || senha.isEmpty()) {
            view.exibirMensagemErro("Todos os campos são obrigatórios.");
            return;
        }
        
        /*
        Integrar depois com o validador de senhas
         if (!validador.validar(senha)) {
             view.exibirMensagemErro("A senha não atende aos requisitos.");
             return;
         }
*/

        try {

            Usuario novoUsuario = new Usuario(nome, username, senha);
            
            usuarioService.cadastrarNovoUsuario(novoUsuario);
            view.exibirMensagemSucesso("Cadastro realizado com sucesso! Você é o administrador principal.");
            view.fechar();
        } catch (IllegalArgumentException e) {
            view.exibirMensagemErro("Erro de Validação: " + e.getMessage());
        } catch (RuntimeException e) {
            view.exibirMensagemErro("Erro interno ao tentar cadastrar.");
        }
    }
    
    public void cancelar() {
        view.fechar();
    }
}
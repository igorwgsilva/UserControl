/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Igor Wendling
 */
package view;

import model.Usuario;

public interface ILoginView {
    String getNomeUsuario();
    String getSenha();
    void exibirMensagemErro(String mensagem);
    void fechar();
    void abrirJanelaPrincipal(Usuario usuarioAutenticado);
    void abrirAutoCadastroView();// em caso de sistema vazio
}
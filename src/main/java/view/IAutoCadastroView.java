/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package view;

/**
 *
 * 
 */

import model.Usuario;

public interface IAutoCadastroView {
    String getNomeCompleto();
    String getUsername();
    String getSenha();
    void exibirMensagemSucesso(String mensagem);
    void exibirMensagemErro(String mensagem);
    void fechar();
}

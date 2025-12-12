/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package view;

/**
 *
 * @author Igor Wendling
 */

import javax.swing.JDesktopPane;
import javax.swing.JMenu; 

import model.Usuario;

public interface IMainView {
    void exibirNomeUsuario(String nome);
    void exibirPerfil(String perfil);
    void exibirContagemNaoLidas(int contagem);
    void habilitarMenuAdmin(boolean habilitar);
    void fecharSistema();
    // Desktop Pane
    void abrirManterUsuarioView(Usuario usuarioAdmin);
    void abrirEnviarNotificacaoView(Usuario usuarioAdmin);
    void abrirMinhasNotificacoesView(Usuario usuario);
    void abrirConfiguracaoLogView();
    void abrirRestaurarSistemaView(Usuario usuarioAdmin);
}
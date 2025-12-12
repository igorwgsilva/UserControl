/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.usercontrol;

import java.time.LocalDateTime;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import model.PerfilAdministrador;
import model.Usuario;
import presenter.JanelaPrincipalPresenter;
import presenter.ManterUsuarioPresenter;
import repository.UsuarioJdbcRepository;
import service.UsuarioService;
import view.ManterUsuarioView;

/**
 *
 * @author Igor Wendling
 */
public class UserControl {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
//        Usuario usuarioLogado = new Usuario(1, "Joao Batista", "Batistinha", "234", new PerfilAdministrador() , true, LocalDateTime.now());
//        
//        
//         new JanelaPrincipalPresenter(usuarioLogado);



// 1. Configura Dependências (Banco e Service)
        UsuarioJdbcRepository.createTableUsuario();
        UsuarioJdbcRepository repository = new UsuarioJdbcRepository();
        UsuarioService service = new UsuarioService(repository);
        
        // 2. Simula um Usuário Logado (Admin)
        Usuario usuarioLogadoMock = new Usuario("Admin Teste", "admin", "123");
        usuarioLogadoMock.setId(1);
        usuarioLogadoMock.setPerfil(new PerfilAdministrador());
        usuarioLogadoMock.setAutorizado(true);

        // 3. Cria a Janela Principal Falsa (O Palco)
        JFrame frame = new JFrame("Teste Isolado - Manter Usuários");
        JDesktopPane desktop = new JDesktopPane(); // O fundo onde as janelas flutuam
        frame.setContentPane(desktop);
        frame.setSize(1200, 800); // Tamanho grande para caber tudo
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centraliza na tela
        
        // 4. Cria a View (A janela filha)
        ManterUsuarioView viewUsuarios = new ManterUsuarioView();
        
        // --- O PULO DO GATO (CORREÇÃO) ---
        // Adiciona a view ao desktop ANTES de chamar o presenter
        desktop.add(viewUsuarios); 
        // ---------------------------------

        // 5. Inicia o Presenter
        // O presenter vai carregar os dados e fazer o view.setVisible(true)
        new ManterUsuarioPresenter(viewUsuarios, service, desktop, usuarioLogadoMock);
        
        // 6. Exibe a janela principal
        frame.setVisible(true);
    }
}

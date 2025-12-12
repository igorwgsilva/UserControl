/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Usuario;
import service.UsuarioService;
import view.LoginCadastrolView2;

/**
 *
 * @author Erko
 */
public class LoginPresenter2 {
    private LoginCadastrolView2 view;
    private UsuarioService service;
    
    
    public LoginPresenter2(UsuarioService service) {
        this.service = service;
        this.view = new LoginCadastrolView2(null, true); 

        configurarListeners();
        
        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }
    private void fazerLogin() {
        String user = view.getTxtUsername().getText();
        String senha = view.getTxtSenha().getText();

        try {
            // O service verifica: Existe? Senha bate? Está Autorizado?
            boolean autenticado = service.autenticarUsuario(user, senha);

            if (autenticado) {
                // Recupera o objeto completo (ID, Nome, Perfil) para a sessão
                Usuario usuarioLogado = service.buscarPorNomeDeUsuario(user); // Certifique-se que o Service expõe isso
                
                view.dispose(); // Fecha tela de login
                
                // Abre a Janela Principal passando quem logou
                new JanelaPrincipalPresenter(usuarioLogado); 
                
            } else {
                JOptionPane.showMessageDialog(view, "Usuário ou senha inválidos.");
            }
            
        } catch (IllegalStateException ex) {
            // Captura msg de "Usuário não autorizado" vinda do Service
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Acesso Pendente", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro técnico: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    

    private void configurarListeners() {
        // --- Botão Login ---
        view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//FAZER                fazerLogin();
            }
        });

        // --- Botão Cadastrar ---
        view.getBtnCadastrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//FAZER                abrirAutoCadastro();
            }
        });
        
        
    }
}

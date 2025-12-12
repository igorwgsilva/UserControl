/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioJdbcRepository;
import service.UsuarioService;
import view.JanelaPrincipalView;
import view.ManterUsuarioView;

/**
 *
 * @author Erko
 */
public class JanelaPrincipalPresenter {
     private Usuario usuarioLogado;
    private JanelaPrincipalView viewPrincipal;
    private UsuarioService usuarioService;

    public JanelaPrincipalPresenter(Usuario usuarioLogado) {
        this.usuarioLogado=usuarioLogado;
        this.viewPrincipal = new JanelaPrincipalView();

        //instancia classe de repositorio
        IUsuarioRepository repositorioUsuarios = new UsuarioJdbcRepository(); 
        
        //configurações iniciais
        //nome no rodape
        viewPrincipal.getLblRodapeNomePerfil().setText(usuarioLogado.getNome()+ " - "+ usuarioLogado.getNomeDePerfil());
        
        this.usuarioService = new UsuarioService(repositorioUsuarios);

        //congigurar listeners
        configurarListeners();

        //this.viewPrincipal.setLocationRelativeTo(null); // Centraliza se não estiver maximizado
        this.viewPrincipal.setVisible(true);
//    atualizarRodape(); //FAZER    
    }

    private void configurarListeners() {
        // --- Botão de Notificações (Rodapé) ---
        viewPrincipal.getBtnNNotificacoes().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(viewPrincipal, "Abrir tela de notificações (A implementar)");
            }
        });
        
        // --- Menu: Manter Usuários ---
        // ATENÇÃO: Você precisa criar o getter 'getMitManterUsuarios' na View Principal
        if (viewPrincipal.getBtnManterUsuario() != null) {
            viewPrincipal.getBtnManterUsuario().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirManterUsuarios();
                }
            });
        }
    }

    private void abrirManterUsuarios() {


        // 1. Cria a View Filha (Baseada no seu arquivo ManterUsuarioView.java)
        ManterUsuarioView subView = new ManterUsuarioView();
        
        // 2. Adiciona ao DesktopPane da Principal
        viewPrincipal.getDskPrincipalPanel().add(subView);
        
        // 3. Cria o Presenter da Filha (Passando o desktop para ele poder abrir modais se precisar)
        // Isso conecta a lógica da tela de usuários
        new ManterUsuarioPresenter(subView, usuarioService, viewPrincipal.getDskPrincipalPanel(), this.usuarioLogado);
        
        // 4. Exibe
        subView.setVisible(true);
    }
}

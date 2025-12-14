/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Usuario;
import repository.INotificacaoRepository;
import repository.IUsuarioRepository;
import repository.NotificacaoJdbcRepository;
import repository.UsuarioJdbcRepository;
import service.NotificacaoService;
import service.UsuarioService;
import view.AlterarSenhaView;
import view.JanelaPrincipalView;
import view.ManterUsuarioView;
import view.MinhasNotificacoesView;

/**
 *
 * @author Erko
 */
public class JanelaPrincipalPresenter {
    private Usuario usuarioLogado;
    private JanelaPrincipalView viewPrincipal;
    private UsuarioService usuarioService;
    private NotificacaoService notificacaoService;
    
    
    public JanelaPrincipalPresenter(Usuario usuarioLogado) {
        this.usuarioLogado=usuarioLogado;
        this.viewPrincipal = new JanelaPrincipalView();

        //instancia classe de repositoriousuario e repositorio notificacao
        IUsuarioRepository repositorioUsuarios = new UsuarioJdbcRepository(); 
        INotificacaoRepository repositorioNotificacao = new NotificacaoJdbcRepository();
        
        //configurações iniciais
        //nome no rodape
        viewPrincipal.getLblRodapeNomePerfil().setText(usuarioLogado.getNome()+ " - "+ usuarioLogado.getNomeDePerfil());
        
        //centraliza a tela
        viewPrincipal.setLocationRelativeTo(null); 
        
        
        //instancias de services
        this.usuarioService = new UsuarioService(repositorioUsuarios);
        this.notificacaoService = new NotificacaoService(repositorioUsuarios, repositorioNotificacao);
        
        
        
        //se não for admin esconde menu feito para admins. limita acesso
        if (!usuarioLogado.isAdministrador()) {
            viewPrincipal.getMitManterUsuario().setVisible(false);
            //esconde o menu enviar notificações se não for admin
            if (viewPrincipal.getMitEnviarNotificacao() != null) {
                viewPrincipal.getMitEnviarNotificacao().setVisible(false);
            }
        }
        
        //atualiza o contador de notificações logo ao abrir (US 13)
        atualizarContadorNotificacoes();
        
        
        //congigurar listeners
        configurarListeners();

        //this.viewPrincipal.setLocationRelativeTo(null); // Centraliza se não estiver maximizado
        this.viewPrincipal.setVisible(true);
        atualizarContadorNotificacoes();    
    }

    private void configurarListeners() {
        // --- Botão de Notificações (Rodapé) ---
        viewPrincipal.getBtnNNotificacoes().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirMinhasNotificacoes();
            }
        });
        
        // --- Menu: Manter Usuários ---
  
        if (viewPrincipal.getMitManterUsuario() != null) {
            viewPrincipal.getMitManterUsuario().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirManterUsuarios();
                }
            });
        }
        
      //--- MENU: ALTERAR SENHA ---
        if (viewPrincipal.getMitAlterarSenha() != null) {
            viewPrincipal.getMitAlterarSenha().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirAlterarSenha();
                }
            });
        }
        
        // --- menu: Enviar Notificações ---
        if (viewPrincipal.getMitEnviarNotificacao() != null) {
            viewPrincipal.getMitEnviarNotificacao().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirEnviarNotificacao();
                }
            });
        }
        
        // --- Menu "Minhas Notificações" ---
        if (viewPrincipal.getMitMinhasNotificacoes() != null) {
            viewPrincipal.getMitMinhasNotificacoes().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    abrirMinhasNotificacoes(); 
                }
            });
        }
        
    }

    private void abrirEnviarNotificacao() {
        
        view.EnviarNotificacaoView viewEnvio = new view.EnviarNotificacaoView();
        
        viewPrincipal.getDskPrincipalPanel().add(viewEnvio);
        
       
        new presenter.EnviarNotificacaoPresenter(
            viewEnvio, 
            usuarioService, 
            notificacaoService, 
            usuarioLogado
        );
        
        viewEnvio.setVisible(true);
    }
    
    private void abrirMinhasNotificacoes() {
        
        MinhasNotificacoesView viewNotificacoes = new MinhasNotificacoesView();
        viewPrincipal.getDskPrincipalPanel().add(viewNotificacoes);
        
        new MinhasNotificacoesPresenter(viewNotificacoes, notificacaoService, usuarioLogado);
        
        viewNotificacoes.setVisible(true);
        
        // Adiciona um ouvinte para saber quando essa janela for fechada
        viewNotificacoes.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                // Assim que fechar a janela, atualiza o botão no rodapé!
                atualizarContadorNotificacoes();
            }
        });
        
    }
    
    private void abrirAlterarSenha() {
        AlterarSenhaView viewSenha = new AlterarSenhaView();
        viewPrincipal.getDskPrincipalPanel().add(viewSenha);
        
        new AlterarSenhaPresenter(viewSenha, usuarioService, usuarioLogado);
    }
    
    public void atualizarContadorNotificacoes() {
        try {
            int naoLidas = notificacaoService.contarNotificacoesNaoLidas(usuarioLogado.getId());
            viewPrincipal.getBtnNNotificacoes().setText(String.valueOf(naoLidas));
        } catch (Exception e) {
            System.err.println("Erro ao contar notificações: " + e.getMessage());
        }
    }
    
    private void abrirManterUsuarios() {


        // 1. ria a View Filha (Baseada no seu arquivo ManterUsuarioView.java)
        ManterUsuarioView subView = new ManterUsuarioView();
        
        // 2. Adiciona ao DesktopPane da Principal
        viewPrincipal.getDskPrincipalPanel().add(subView);
        
        // 3. Cria o Presenter da Filha (Passando o desktop para ele poder abrir modais se precisar)
        // Isso conecta a lógica da tela de usuários
        new ManterUsuarioPresenter(subView, usuarioService, this.notificacaoService, viewPrincipal.getDskPrincipalPanel(), this.usuarioLogado);
        // 4. Exibe
        subView.setVisible(true);
    }
}

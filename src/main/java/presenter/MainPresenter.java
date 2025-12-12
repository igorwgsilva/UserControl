/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 * 
 * author: Igor Wendling
 */
import model.Usuario;
import service.UsuarioService;
import service.NotificacaoService;
import view.IMainView;
import java.util.Timer;
import java.util.TimerTask;

public class MainPresenter {

    private final IMainView view;
    private final UsuarioService usuarioService;
    private final NotificacaoService notificacaoService;
    private final Usuario usuarioAutenticado;
    private final Timer timer;

    // Intervalo de atualização do rodapé em milissegundos
    private static final long INTERVALO_ATUALIZACAO = 10000; 

    public MainPresenter(IMainView view, UsuarioService usuarioService, NotificacaoService notificacaoService, Usuario usuarioAutenticado) {
        this.view = view;
        this.usuarioService = usuarioService;
        this.notificacaoService = notificacaoService;
        this.usuarioAutenticado = usuarioAutenticado;
        this.timer = new Timer();
        
        inicializarView();
        iniciarAtualizacaoPeriodica();
    }

    private void inicializarView() {
 
        view.exibirNomeUsuario(usuarioAutenticado.getNome());
        view.exibirPerfil(usuarioAutenticado.getPerfil().getNomeExibicao());
        boolean isAdmin = usuarioAutenticado.isAdministrador();
        view.habilitarMenuAdmin(isAdmin);

        atualizarContagemNotificacoes();
    }

    private void iniciarAtualizacaoPeriodica() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                atualizarContagemNotificacoes();
            }
        }, INTERVALO_ATUALIZACAO, INTERVALO_ATUALIZACAO);
    }

    public void atualizarContagemNotificacoes() {
        try {
            int contagem = notificacaoService.contarNotificacoesNaoLidas(usuarioAutenticado.getId());
            view.exibirContagemNaoLidas(contagem);
        } catch (RuntimeException e) {
            System.err.println("Falha ao atualizar a contagem de notificações: " + e.getMessage());
        }
    }

    public void abrirManterUsuario() {
        view.abrirManterUsuarioView(usuarioAutenticado);
    }

    public void abrirEnviarNotificacao() {
        view.abrirEnviarNotificacaoView(usuarioAutenticado);
    }

    public void abrirMinhasNotificacoes() {
        view.abrirMinhasNotificacoesView(usuarioAutenticado);
        atualizarContagemNotificacoes(); 
    }

    public void abrirConfiguracaoLog() {
        view.abrirConfiguracaoLogView();
    }
    
    public void abrirRestaurarSistema() {
        view.abrirRestaurarSistemaView(usuarioAutenticado);
    }

    public void sair() {
        timer.cancel();
        view.fecharSistema();
    }
}

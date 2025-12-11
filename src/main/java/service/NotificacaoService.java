package service;
/**
 *
 * @author Igor Wendling
 */

import model.Notificacao;

import model.Usuario;
import repository.INotificacaoRepository;
import repository.IUsuarioRepository;
import java.util.List;
import java.util.Optional;

public class NotificacaoService {

    private final IUsuarioRepository usuarioRepository;
    private final INotificacaoRepository notificacaoRepository;

    public NotificacaoService(IUsuarioRepository usuarioRepository, INotificacaoRepository notificacaoRepository) {
        if (usuarioRepository == null || notificacaoRepository == null) {
            throw new IllegalArgumentException("Os repositorios nao podem ser nulos.");
        }
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
    }

    public void enviarNotificacao(Usuario remetente, List<Integer> idsDestinatarios, String mensagem) {
        if (!"ADMINISTRADOR".equals(remetente.getTipoPerfil())) {
            throw new IllegalStateException("Apenas administradores podem enviar notificacoes.");
        }

        if (idsDestinatarios == null || idsDestinatarios.isEmpty()) {
            throw new IllegalArgumentException("A lista de destinatarios nao pode ser vazia.");
        }

        
        for (Integer idDestinatario : idsDestinatarios) {
            Optional<Usuario> optDestinatario = usuarioRepository.buscarPorId(idDestinatario);

            if (optDestinatario.isEmpty()) {
                System.err.println("Destinatario  ID " + idDestinatario + " nao encontrado.");
                continue;
            }
            
            Usuario destinatario = optDestinatario.get(); 
            if (destinatario.getStatus() != StatusUsuario.AUTORIZADO) { 
                System.err.println("Destinatario ID " + idDestinatario + " nao autorizado e sera ignorado.");
                continue;
            }

            Notificacao novaNotificacao = new Notificacao(idDestinatario, mensagem);
            try {
                notificacaoRepository.salvar(novaNotificacao);
            } catch (RuntimeException e) {
                System.err.println("Falha de persistencia ao salvar notificacao para ID " + idDestinatario);

            }
        }
    }

    public void marcarComoLida(int idNotificacao, int idUsuarioAutenticado) {
        Optional<Notificacao> optNotificacao = notificacaoRepository.buscarPorId(idNotificacao);

        if (optNotificacao.isEmpty()) {
            throw new IllegalArgumentException("Notificacao nao encontrada.");
        }
        
        Notificacao notificacao = optNotificacao.get();
        if (notificacao.getIdDestinatario() != idUsuarioAutenticado) {
             throw new IllegalStateException("Acesso negado. Tentativa de marcar notificacao de outro usuario como lida.");
        }
        
        if (!notificacao.isLida()) {
            try {          
                notificacao.ler(); 
                notificacaoRepository.atualizar(notificacao);             
            } catch (RuntimeException e) {             
                throw new RuntimeException("Falha ao persistir a marcacao de notificacao como lida.", e);

            }
        }
    }
   
    public List<Notificacao> buscarNotificacoesDoUsuario(int idUsuario) {
        return notificacaoRepository.buscarTodasPorUsuario(idUsuario);
    }

    public int contarNotificacoesNaoLidas(int idUsuario) {
        return notificacaoRepository.contarNaoLidas(idUsuario);
    }

    public int contarTotalLidas(int idUsuario) {
        return notificacaoRepository.contarTotalLidas(idUsuario);
    }
    
    public int contarTotalRecebidas(int idUsuario) {
        return notificacaoRepository.contarTotalRecebidas(idUsuario);
    }
}
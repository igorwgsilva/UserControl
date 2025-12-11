package service;
/**
 *
 * @author Igor Wendling
 */

import model.Notificacao;
import model.StatusUsuario;
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
            throw new IllegalArgumentException("Os repositórios não podem ser nulos.");
        }
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
    }

    // =========================================================================
    // US 08: Envio de Notificações (Lógica de Negócio)
    // =========================================================================
    
    /**
     * Envia uma notificação do remetente (Admin) para uma lista de destinatários.
     * O remetente deve ser um Administrador e os destinatários devem estar AUTORIZADOS.
     * @param remetente O usuário logado que está enviando a notificação.
     * @param idsDestinatarios Lista de IDs dos usuários que devem receber a mensagem.
     * @param mensagem O conteúdo da notificação.
     */
    public void enviarNotificacao(Usuario remetente, List<Integer> idsDestinatarios, String mensagem) {
        // Regra 1: O remetente deve ter perfil de Administrador (US 08 - Critério 1)
        if (!"ADMINISTRADOR".equals(remetente.getTipoPerfil())) {
            throw new IllegalStateException("Apenas administradores podem enviar notificações.");
        }

        if (idsDestinatarios == null || idsDestinatarios.isEmpty()) {
            throw new IllegalArgumentException("A lista de destinatários não pode ser vazia.");
        }
        
        // Simplesmente itera e salva uma notificação para cada destinatário.
        for (Integer idDestinatario : idsDestinatarios) {
            Optional<Usuario> optDestinatario = usuarioRepository.buscarPorId(idDestinatario);
            
            // Regra 2: O destinatário deve existir e estar autorizado (US 08 - Critério 2)
            if (optDestinatario.isEmpty()) {
                // Em um sistema real, você registraria este erro no log e continuaria.
                System.err.println("Destinatário ID " + idDestinatario + " não encontrado.");
                continue;
            }
            
            Usuario destinatario = optDestinatario.get();
            // Verifica se o status é 'AUTORIZADO'
            if (destinatario.getStatus() != StatusUsuario.AUTORIZADO) { 
                // Regra 2: Destinatário não autorizado
                System.err.println("Destinatário ID " + idDestinatario + " não autorizado e será ignorado.");
                continue;
            }

            // Cria e salva a notificação (US 08 - Critério 3)
            Notificacao novaNotificacao = new Notificacao(idDestinatario, mensagem);
            try {
                notificacaoRepository.salvar(novaNotificacao);
                // Aqui seria o ponto para o Log de sucesso (US 08 - Critério 4)
            } catch (RuntimeException e) {
                // Tratar falha de persistência (US 08 - Cenário de falha)
                System.err.println("Falha de persistência ao salvar notificação para ID " + idDestinatario);
                // Aqui seria o ponto para o Log de falha
            }
        }
    }

    // =========================================================================
    // US 09: Visualização e Marcação
    // =========================================================================
    
    /**
     * Marca uma notificação específica como lida.
     * @param idNotificacao O ID único da notificação a ser marcada.
     * @param idUsuarioAutenticado O ID do usuário logado (para verificação de segurança).
     */
    public void marcarComoLida(int idNotificacao, int idUsuarioAutenticado) {
        Optional<Notificacao> optNotificacao = notificacaoRepository.buscarPorId(idNotificacao);

        if (optNotificacao.isEmpty()) {
            throw new IllegalArgumentException("Notificação não encontrada.");
        }
        
        Notificacao notificacao = optNotificacao.get();

        // Regra de Segurança: A notificação só pode ser marcada como lida pelo seu dono (US 09 - Critério 1)
        if (notificacao.getIdDestinatario() != idUsuarioAutenticado) {
             throw new IllegalStateException("Acesso negado. Tentativa de marcar notificação de outro usuário como lida.");
        }
        
        if (!notificacao.isLida()) {
            try {
                // Aplica a lógica da Entidade e persiste (US 09 - Critério 4)
                notificacao.ler(); // O método 'ler()' da entidade muda o estado 'lida' para true
                notificacaoRepository.atualizar(notificacao);
                // Aqui seria o ponto para o Log de sucesso (US 09 - Critério 5)
            } catch (RuntimeException e) {
                // Tratar falha de persistência (US 09 - Cenário de falha)
                throw new RuntimeException("Falha ao persistir a marcação de notificação como lida.", e);
                // Aqui seria o ponto para o Log de falha
            }
        }
    }
    
    // =========================================================================
    // US 13/10: Consultas de Leitura
    // =========================================================================
    
    /**
     * Retorna todas as notificações de um usuário específico (US 09/13).
     * @param idUsuario O ID do usuário para o qual as notificações serão buscadas.
     * @return A lista de notificações do usuário.
     */
    public List<Notificacao> buscarNotificacoesDoUsuario(int idUsuario) {
        return notificacaoRepository.buscarTodasPorUsuario(idUsuario);
    }
    
    /**
     * Retorna a contagem de notificações não lidas para o rodapé (US 13).
     * @param idUsuario O ID do usuário.
     * @return O número de notificações não lidas.
     */
    public int contarNotificacoesNaoLidas(int idUsuario) {
        return notificacaoRepository.contarNaoLidas(idUsuario);
    }
    
    /**
     * Retorna o total de notificações que o usuário já leu para fins de relatório (US 10).
     * @param idUsuario O ID do usuário.
     * @return O número total de notificações lidas.
     */
    public int contarTotalLidas(int idUsuario) {
        return notificacaoRepository.contarTotalLidas(idUsuario);
    }
    
    /**
     * Retorna o total de notificações recebidas pelo usuário para fins de relatório (US 10).
     * @param idUsuario O ID do usuário.
     * @return O número total de notificações recebidas (lidas + não lidas).
     */
    public int contarTotalRecebidas(int idUsuario) {
        return notificacaoRepository.contarTotalRecebidas(idUsuario);
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repository;

/**
 *
 * @author Eriko
 */
import java.util.List;
import java.util.Optional;
import model.Notificacao;

public interface INotificacaoRepository {
    /**
     * Salva uma nova notificação no banco de dados.
     * Usado no US 08 Envio de notificações por administrado
     * @param notificacao A notificação a ser persistida.
     */
    void salvar(Notificacao notificacao);
    
    /**
     * Atualiza o estado da notificação (ex: marcar como lida).
     * Usado na US 09 (Visualização e marcação).
     * @param notificacao A notificação com o estado atualizado.
     */
    void atualizar(Notificacao notificacao);
    
    /**
     * Busca uma notificação específica pelo ID.
     * Necessário para o Service carregar a notificação antes de marcá-la como lida.
     * @param id O ID da notificação.
     * @return Um Optional contendo a notificação, se existir.
     */
    Optional<Notificacao> buscarPorId(int id);
    
    
    /**
     * Retorna todas as notificações de um usuário específico.
     * Usado na US 09 e US 13 para preencher a janela de visualização.
     * @param idUsuario O ID do usuário dono das mensagens.
     * @return Lista de notificações (pode ser vazia).
     */
    List<Notificacao> buscarTodasPorUsuario(int idUsuario);
    
    
    /**
     * Conta quantas notificações NÃO LIDAS existem para um usuário.
     * Usado na US 13 (Rodapé) para exibir o número no botão.
     * Query SQL equivalente: SELECT COUNT(*) FROM notificacao WHERE id_destinatario = ? AND lida = 0
     * @param idUsuario O ID do usuário.
     * @return O número de mensagens pendentes.
     */
    int contarNaoLidas(int idUsuario);

    /**
     * Conta o total de notificações enviadas para um usuário (Lidas + Não Lidas).
     * Usado na US 10 (Listagem de usuários pelo administrador).
     * @param idUsuario O ID do usuário.
     * @return Total histórico de mensagens recebidas.
     */
    int contarTotalRecebidas(int idUsuario);

    /**
     * Conta o total de notificações que o usuário já leu.
     * Usado na US 10 (Listagem de usuários pelo administrador).
     * @param idUsuario O ID do usuário.
     * @return Total de mensagens com status lida = true.
     */
    int contarTotalLidas(int idUsuario);
    
    
}

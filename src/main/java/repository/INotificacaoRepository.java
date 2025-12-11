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

    void salvar(Notificacao notificacao);
    void atualizar(Notificacao notificacao);
    Optional<Notificacao> buscarPorId(int id);
    List<Notificacao> buscarTodasPorUsuario(int idUsuario);
    int contarNaoLidas(int idUsuario);
    int contarTotalRecebidas(int idUsuario);
    int contarTotalLidas(int idUsuario);
    
    
}

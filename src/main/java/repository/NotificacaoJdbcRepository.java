/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author Igor Wendling
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Notificacao;
import util.DbUtils;

public class NotificacaoJdbcRepository implements INotificacaoRepository {

    private Notificacao mapResultSetToNotificacao(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idDestinatario = rs.getInt("id_destinatario");
        String mensagem = rs.getString("mensagem");
        boolean lida = rs.getBoolean("lida");
        
        LocalDateTime dataEnvio = LocalDateTime.parse(rs.getString("data_envio"));
        
        return new Notificacao(id, idDestinatario, mensagem, lida, dataEnvio);
    }

    public static void createTableNotificacao() {
        String sql = """
                     CREATE TABLE IF NOT EXISTS notificacao (
                        id INTEGER PRIMARY KEY,
                        id_destinatario INTEGER NOT NULL,
                        mensagem TEXT NOT NULL,
                        lida BOOLEAN NOT NULL,
                        data_envio TEXT NOT NULL,
                        FOREIGN KEY (id_destinatario) REFERENCES usuario(id)
                     );
                     """;

        try (Connection conn = DbUtils.connect();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Tabela 'notificacao' verificada e pronta.");
            
        } catch (SQLException e) {
            System.err.println("Erro na criação da tabela 'notificacao': " + e.getMessage());
        }
    }


    @Override
    public void salvar(Notificacao notificacao) {
        String sql = "INSERT INTO notificacao(id_destinatario, mensagem, lida, data_envio) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, notificacao.getIdDestinatario());
            pstmt.setString(2, notificacao.getMensagem());
            pstmt.setBoolean(3, notificacao.isLida());
            pstmt.setString(4, notificacao.getDataEnvio().toString());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    notificacao.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar notificação: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Notificacao notificacao) {
        String sql = "UPDATE notificacao SET mensagem = ?, lida = ?, data_envio = ? WHERE id = ?";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, notificacao.getMensagem());
            pstmt.setBoolean(2, notificacao.isLida());
            pstmt.setString(3, notificacao.getDataEnvio().toString());
            pstmt.setInt(4, notificacao.getId());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar notificação: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Notificacao> buscarPorId(int id) {
        String sql = "SELECT * FROM notificacao WHERE id = ?";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToNotificacao(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar notificação por ID: " + e.getMessage(), e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Notificacao> buscarTodasPorUsuario(int idUsuario) {
        List<Notificacao> notificacoes = new ArrayList<>();
        String sql = "SELECT * FROM notificacao WHERE id_destinatario = ? ORDER BY data_envio DESC";

        try (Connection conn = DbUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notificacoes.add(mapResultSetToNotificacao(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar notificações por usuário: " + e.getMessage(), e);
        }
        return notificacoes;
    }

    @Override
    public int contarNaoLidas(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM notificacao WHERE id_destinatario = ? AND lida = 0";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar notificações não lidas: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int contarTotalRecebidas(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM notificacao WHERE id_destinatario = ?";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de notificações recebidas: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int contarTotalLidas(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM notificacao WHERE id_destinatario = ? AND lida = 1";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de notificações lidas: " + e.getMessage(), e);
        }
        return 0;
    }
}

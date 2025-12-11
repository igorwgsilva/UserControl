package repository;

/**
 * 
 * @author igor Wendling
 */
import model.Usuario;
import util.DbUtils;
import model.StatusUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioJdbcRepository implements IUsuarioRepository {
  
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        
        usuario.setId(rs.getInt("id")); 
        usuario.setTipoPerfil(rs.getString("tipo_perfil"));
        usuario.setStatus(StatusUsuario.valueOf(rs.getString("status_usuario"))); 
        usuario.setNomeDeUsuario(rs.getString("nome_de_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setSenha(rs.getString("senha"));
        
        String dataString = rs.getString("data_cadastro");
        usuario.setDataCadastro(LocalDateTime.parse(dataString)); 
        
        usuario.setNotificacoesRecebidas(rs.getInt("notificacoes_recebidas"));
        usuario.setNotificacoesLidas(rs.getInt("notificacoes_lidas"));
        
        return usuario;
    }

    public static void createTableUsuario() {
        String sql = """
                     CREATE TABLE IF NOT EXISTS usuario (
                        id INTEGER PRIMARY KEY,
                        tipo_perfil TEXT NOT NULL,
                        status_usuario TEXT NOT NULL,
                        nome_de_usuario TEXT UNIQUE NOT NULL,
                        nome TEXT NOT NULL,
                        senha TEXT NOT NULL,
                        data_cadastro TEXT NOT NULL,
                        notificacoes_recebidas INTEGER,
                        notificacoes_lidas INTEGER
                     );
                     """;

        try (Connection conn = DbUtils.connect();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Tabela 'usuario' verificada e pronta.");
            
        } catch (SQLException e) {
            System.err.println("Erro na criacao da tabela 'usuario': " + e.getMessage());
        }
    }

    @Override
    public Usuario buscarPorNomeDeUsuario(String nomeDeUsuario) {
        String sql = "SELECT * FROM usuario WHERE nome_de_usuario = ?";
        Usuario usuario = null;

        try (Connection conn = DbUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeDeUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = mapResultSetToUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario por nome: " + e.getMessage());
            throw new RuntimeException("Falha na consulta ao banco de dados.", e);
        }
        return usuario;
    }
   
    @Override
    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario por ID: " + e.getMessage());
            throw new RuntimeException("Falha na consulta ao banco de dados.", e);
        }
        return Optional.empty();
    }


    @Override
    public void salvar(Usuario usuario) {
        String sql = """
                     INSERT INTO usuario 
                     (tipo_perfil, status_usuario, nome_de_usuario, nome, senha, 
                     data_cadastro, notificacoes_recebidas, notificacoes_lidas) 
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                     """;

        try (Connection conn = DbUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getTipoPerfil());
            stmt.setString(2, usuario.getStatus().name()); 
            stmt.setString(3, usuario.getNomeDeUsuario());
            stmt.setString(4, usuario.getNome());
            stmt.setString(5, usuario.getSenha());
            stmt.setString(6, usuario.getDataCadastro().toString()); 
            stmt.setInt(7, usuario.getNotificacoesRecebidas());
            stmt.setInt(8, usuario.getNotificacoesLidas());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1)); 
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuario: " + e.getMessage());
            throw new RuntimeException("Falha na persistencia do usuario.", e);
        }
    }

    @Override
    public boolean existeUsuarioCadastrado() {
        String sql = "SELECT COUNT(*) FROM usuario";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existencia de usuarios: " + e.getMessage());
            throw new RuntimeException("Falha na consulta ao banco de dados.", e);
        }
        return false;
    }
    
    @Override
    public List<Usuario> buscarTodos() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DbUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os usuarios: " + e.getMessage());
            throw new RuntimeException("Falha na consulta ao banco de dados.", e);
        }
        return usuarios;
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = """
                     UPDATE usuario SET 
                     tipo_perfil = ?, 
                     status_usuario = ?, 
                     nome = ?, 
                     senha = ?, 
                     notificacoes_recebidas = ?, 
                     notificacoes_lidas = ?
                     WHERE id = ?
                     """;

        try (Connection conn = DbUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getTipoPerfil());
            stmt.setString(2, usuario.getStatus().name()); 
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getSenha());
            stmt.setInt(5, usuario.getNotificacoesRecebidas());
            stmt.setInt(6, usuario.getNotificacoesLidas());
            
            stmt.setInt(7, usuario.getId()); 
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Erro ao atualizar: Usuario com ID " + usuario.getId() + " nao encontrado.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuario: " + e.getMessage());
            throw new RuntimeException("Falha na atualizacao da persistencia.", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        
        try (Connection conn = DbUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Erro ao excluir: Usuario com ID " + id + " nao encontrado.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir usuario: " + e.getMessage());
            throw new RuntimeException("Falha na exclusão da persistencia.", e);
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author igor Wendling
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    
   
    private static final String URL = "jdbc:sqlite:atividade.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            
            conn = DriverManager.getConnection(URL);
            System.out.println("Conexao com SQLite estabelecida com sucesso.");
            return conn;
            
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados SQLite: " + e.getMessage());
            return null; 
        }
    }
    
public static void createNewTable() {
    
    String sql = """
                 CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY,
                    tipo_perfil TEXT NOT NULL,
                    status_usuario TEXT NOT NULL,
                    nome_de_usuario TEXT UNIQUE NOT NULL,
                    nome TEXT NOT NULL,
                    senha TEXT NOT NULL,
                    data_cadastro TEXT NOT NULL,
                    notificacoes_recebidas INTEGER DEFAULT 0,
                    notificacoes_lidas INTEGER DEFAULT 0
                 );
                 """;

    try (Connection conn = connect();
         java.sql.Statement stmt = conn.createStatement()) {
        
        stmt.execute(sql);
        System.out.println("Tabela 'usuario' criada ou ja existente.");
        
    } catch (SQLException e) {
        System.err.println("Erro na criacao da tabela: " + e.getMessage());
    }
}
}

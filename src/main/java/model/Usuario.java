/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Igor Wendling
 */
import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String tipoPerfil; //Administrador ou Usuário
    private StatusUsuario status; //Autorizado ou Não autorizado
    private String nomeDeUsuario;
    private String nome;
    private String senha;
    private LocalDateTime dataCadastro;
    private int notificacoesRecebidas;
    private int notificacoesLidas;

    public Usuario() {
    }
    

    public Usuario(String tipoPerfil, StatusUsuario status, String nomeDeUsuario, String nome, String senha, LocalDateTime dataCadastro) {
        this.tipoPerfil = tipoPerfil;
        this.status = status;
        this.nomeDeUsuario = nomeDeUsuario;
        this.nome = nome;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
        this.notificacoesRecebidas = 0;
        this.notificacoesLidas = 0;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }

    public String getNomeDeUsuario() {
        return nomeDeUsuario;
    }

    public void setNomeDeUsuario(String nomeDeUsuario) {
        this.nomeDeUsuario = nomeDeUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public int getNotificacoesRecebidas() {
        return notificacoesRecebidas;
    }

    public void setNotificacoesRecebidas(int notificacoesRecebidas) {
        this.notificacoesRecebidas = notificacoesRecebidas;
    }

    public int getNotificacoesLidas() {
        return notificacoesLidas;
    }

    public void setNotificacoesLidas(int notificacoesLidas) {
        this.notificacoesLidas = notificacoesLidas;
    }

}

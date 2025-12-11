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
       private int id; //utilizar id de banco para fins de log
    private String nome;

    
    private String nomeUsuario; // username
    private String senha;
    private IPerfilUsuario perfil; //perfil de usuario
    
    private boolean autorizado;
    private LocalDateTime dataCadastro;
    
 
    public Usuario(String nome, String nomeUsuario, String senha) {
        this.nome = nome;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.perfil = new PerfilComum();
        this.autorizado = false; // Padrão: não autorizado 
        this.dataCadastro = LocalDateTime.now();
        
    }
    
    public Usuario(int id, String nome, String nomeUsuario, String senha, IPerfilUsuario perfil, boolean autorizado, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.perfil = perfil;
        this.autorizado = autorizado;
        this.dataCadastro = dataCadastro;
    }
    
    
    public boolean isAdministrador(){
        return perfil.isAdministrador();
    }
        
//pode ser necessario remover depois ==========================================
    public void setId(int id) {
        this.id = id;
    } //pode ser necessario remover depois ====================================

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", nomeUsuario=" + nomeUsuario + ", senha=" + senha + ", perfil=" + perfil + ", autorizado=" + autorizado + ", dataCadastro=" + dataCadastro + '}';
    }
     
   
//    public boolean {
//        return this.perfil
//    }

    public IPerfilUsuario getPerfil() {
        return this.perfil;
    }
    
    public String getNomeDePerfil(){
        if (this.perfil == null) {
            return "Indefinido"; // pra caso o perfil não tenha sido setado
        }
        return this.perfil.getNomeExibicao();
    }

    public void setPerfil(IPerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public int getId() {
        return id;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}

package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.time.LocalDateTime;

/**
 *
 * @author Erko
 */
public class Notificacao {
    private int id;             //id no banco
    private int idDestinatario; //id do destinatario
    private String mensagem;
    private boolean lida;
    private LocalDateTime dataEnvio;

    
    //construtor sem id para nova notificação, facilita criação 
    public Notificacao(int idDestinatario, String mensagem) {
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem da notificação não pode ser vazia.");
        }
        this.idDestinatario = idDestinatario;
        this.mensagem = mensagem;
        this.lida = false; // Padrão: nasce como NÃO lida [cite: 215]
        this.dataEnvio = LocalDateTime.now(); // Padrão: data/hora atual
    }
    
    
    //construtor completo
    public Notificacao(int id, int idDestinatario, String mensagem, boolean lida, LocalDateTime dataEnvio) {
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem da notificação não pode ser vazia.");
        }
        this.id = id;
        this.idDestinatario = idDestinatario;
        this.mensagem = mensagem;
        this.lida = lida;
        this.dataEnvio = dataEnvio;
    }
    
    public void ler() {
        this.lida = true;
    }

    public int getId() {
        return id;
    }

    public int getIdDestinatario() {
        return idDestinatario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public boolean isLida() {
        return lida;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    @Override
    public String toString() {
        return "Notificacao{" + "id=" + id + ", idDestinatario=" + idDestinatario + ", mensagem=" + mensagem + ", lida=" + lida + ", dataEnvio=" + dataEnvio + '}';
    }
    
}

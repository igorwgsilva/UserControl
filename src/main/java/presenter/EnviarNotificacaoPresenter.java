/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Usuario;
import service.NotificacaoService;
import service.UsuarioService;
import view.EnviarNotificacaoView;

/**
 *
 * @author Erko
 */
public class EnviarNotificacaoPresenter {
    private EnviarNotificacaoView view;
    private UsuarioService usuarioService;
    private NotificacaoService notificacaoService;
    private Usuario usuarioRemetente;
    
    //lista aux para usuarios na tabela desta tela
    private List<Usuario> usuariosNaTabela;
    
    public EnviarNotificacaoPresenter(EnviarNotificacaoView view, UsuarioService usuarioService, NotificacaoService notificacaoService, Usuario usuarioRemetente) {
        this.view = view;
        this.usuarioService = usuarioService;
        this.notificacaoService = notificacaoService;
        this.usuarioRemetente = usuarioRemetente;
        
//        configurarView();
        carregarDestinatarios();
        configurarListeners();
        
        this.view.setVisible(true);
    }
    
    
    private void carregarDestinatarios() {
        try {
            // Busca todos do banco
            List<Usuario> todosUsuarios = usuarioService.buscarTodos();
            usuariosNaTabela = new ArrayList<>();
            
            DefaultTableModel modelo = (DefaultTableModel) view.getTabDestinatarios().getModel();
            modelo.setNumRows(0);
            
            for (Usuario usuarioAtual : todosUsuarios) {
                
                //não mostra o próprio Admin que está enviando
                //só mostra usuários autorizados (US 08 - não enviar para não autorizados)
                if (usuarioAtual.getId() != usuarioRemetente.getId() && usuarioAtual.isAutorizado()) {
                    
                    usuariosNaTabela.add(usuarioAtual); // Adiciona na lista auxiliar
                    
                    modelo.addRow(new Object[]{
                        usuarioAtual.getNome(),
                        usuarioAtual.getNomeUsuario()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar lista: " + e.getMessage());
        }
    }
    
    private void configurarListeners() {
        view.getBtnEnviar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviar();
            }
        });
        
        view.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }
    
    private void enviar() {
        try {
            //validar mensagem
            String mensagem = view.getTxtMensagem().getText();
            if (mensagem.trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Digite uma mensagem para enviar.");
                return; //sai do metodo
            }

            //validar seleção de usuários
            int[] linhasSelecionadas = view.getTabDestinatarios().getSelectedRows();
            if (linhasSelecionadas.length == 0) {
                JOptionPane.showMessageDialog(view, "Selecione pelo menos um destinatário na lista (Use CTRL ou SHIFT para vários).");
                return; //sai do metodo
            }

            //converte as linhas selecionadas em IDs
            List<Integer> idsParaEnviar = new ArrayList<>();
            for (int linha : linhasSelecionadas) {
                Usuario u = usuariosNaTabela.get(linha);
                idsParaEnviar.add(u.getId());
            }

         
            notificacaoService.enviarNotificacao(usuarioRemetente, idsParaEnviar, mensagem);
            
            JOptionPane.showMessageDialog(view, "Sucesso! Notificação enviada para " + idsParaEnviar.size() + " usuário(s).");
            view.dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao enviar: " + e.getMessage());
        }
    }
    
}

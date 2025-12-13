/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Notificacao;
import model.Usuario;
import service.NotificacaoService;
import view.MinhasNotificacoesView;

/**
 *
 * @author Erko
 */
public class MinhasNotificacoesPresenter {
    private MinhasNotificacoesView view;
    private NotificacaoService service;
    private Usuario usuarioLogado;
    
    private List<Notificacao> listaNotificacoes;
    
    
    public MinhasNotificacoesPresenter(MinhasNotificacoesView view, NotificacaoService service, Usuario usuarioLogado) {
        this.view = view;
        this.service = service;
        this.usuarioLogado = usuarioLogado;
        configurarView();
        carregarNotificacoes();
        configurarListeners();
        this.view.setVisible(true);
    }
    
    
    private void configurarView() {
        //centraliza
        java.awt.Dimension desktopSize = view.getParent().getSize();
        java.awt.Dimension frameSize = view.getSize();
        view.setLocation((desktopSize.width - frameSize.width)/2, (desktopSize.height - frameSize.height)/2);
        
       
    }
    
    private void carregarNotificacoes() {
        try {
            listaNotificacoes = service.buscarNotificacoesDoUsuario(usuarioLogado.getId());
            
            DefaultTableModel modelo = (DefaultTableModel) view.getTabNotificacoes().getModel();
            modelo.setNumRows(0);
            
            //cria um formatador de data, utilizado no for para deixar a data no formato que utilizamos no brasil (Ex: 13/12/2025 14:30)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Notificacao notificacaoAtual : listaNotificacoes) {
                
                // Formata a data manualmente aqui no presenter
                String dataPadraoBrasil = "";
                if (notificacaoAtual.getDataEnvio() != null) {
                    dataPadraoBrasil = notificacaoAtual.getDataEnvio().format(formatter);
                }

                modelo.addRow(new Object[]{
                    dataPadraoBrasil,                                   //Coluna Data
                    notificacaoAtual.isLida() ? "Lida" : "NÃO LIDA",   //Coluna Status Lida ou não lida
                    notificacaoAtual.getMensagem()                     //Coluna Mensagem
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar notificações: " + e.getMessage());
        }
    }
 
    private void configurarListeners() {
        //listener da Tabela: quando clicar na linha, mostra o texto completo embaixo
        view.getTabNotificacoes().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    exibirConteudoSelecionado();
                }
            }
        });

        //botão "Marcar como Lida'
        view.getBtnMarcarLida().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarComoLida();
            }
        });
        
        // Botão Fechar
        view.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }
    
    private void exibirConteudoSelecionado() {
        int linha = view.getTabNotificacoes().getSelectedRow();
        if (linha != -1) {
            // Pega o objeto da lista (cache) baseado na linha clicada
            Notificacao n = listaNotificacoes.get(linha);
            // Exibe a mensagem completa na área de texto
            view.getTxtConteudoNotificacao().setText(n.getMensagem()); 
        }
    }
    
    private void marcarComoLida() {
        int linha = view.getTabNotificacoes().getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(view, "Selecione uma notificação na lista.");
            return;
        }

        Notificacao n = listaNotificacoes.get(linha);
        
        if (n.isLida()) {
            JOptionPane.showMessageDialog(view, "Esta notificação já foi lida.");
            return;
        }

        try {
            // Chama o service para atualizar no banco
            service.marcarComoLida(n.getId(), usuarioLogado.getId());
            
            JOptionPane.showMessageDialog(view, "Marcada como lida!");
            
            // Recarrega a tabela para atualizar o status visualmente
            carregarNotificacoes();
            view.getTxtConteudoNotificacao().setText(""); // Limpa o campo de texto
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro: " + e.getMessage());
        }
    }
    
}

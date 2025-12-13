/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Usuario;
import service.UsuarioService;
import view.FormularioUsuarioView;
import view.ManterUsuarioView;



/**
 *
 * @author Erko
 */
public class ManterUsuarioPresenter {
    private Usuario usuarioLogado;
    private ManterUsuarioView view;
    private UsuarioService service;
    private JDesktopPane desktop;
//    private Usuario usuarioLogado; verificar futuramente se precisa 
    
    // Lista auxiliar para mapear Linha da Tabela -> Objeto Usuario
    private List<Usuario> listaUsuariosCache;

    
    
    public ManterUsuarioPresenter(ManterUsuarioView view, UsuarioService usuarioService, JDesktopPane dskPrincipalPanel, Usuario usuarioLogado) {  
        this.usuarioLogado = usuarioLogado;
        this.view = view;
        this.service = usuarioService;
        this.desktop = dskPrincipalPanel;
        
        //configurar view
        view.setTitle("Gestão de Usuários");
        configurarListeners();
        carregarTabela();
        
        this.view.setVisible(true);
   
    }
    
    
    
   
    private void abrirFormulario(Usuario usuarioEdicao) {
        // Cria a view do formulário
        FormularioUsuarioView formView = new FormularioUsuarioView();
        
        // Adiciona ao desktop pane
        desktop.add(formView);
        
        new FormularioUsuarioPresenter(formView, service, usuarioEdicao, this);
        
        formView.setVisible(true);
    }
    
    private void configurarListeners() {
        // --- Botão Novo ---
        view.getBtnNovoUsuario().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormulario(null); // Passa null indicando que é um NOVO cadastro
            }
        });

        // --- Botão Alterar ---
        view.getBtnAlterar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarUsuario();
            }
        });

        // --- Botão Excluir ---
        view.getBtnExcluir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirUsuario();
            }
        });
        
        view.getBtnAutorizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autorizarUsuarioSelecionado();
            }
        });
        
    }
    
    private void autorizarUsuarioSelecionado() {
        //linha selecionada na tabela
        int linha = view.getTabTabelaUsuarios().getSelectedRow();
        
        if (linha == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário na lista.");
            return;
        }

        // Pega o objeto real da lista (cache)
        Usuario usuarioAlvo = listaUsuariosCache.get(linha);

        // Verifica se já não está autorizado
        if (usuarioAlvo.isAutorizado()) {
            JOptionPane.showMessageDialog(view, "Este usuário já está autorizado!");
            return;
        }

        // Confirmação
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Confirma a autorização de acesso para " + usuarioAlvo.getNome() + "?",
            "Autorizar Usuário",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Chama o Service (que vai dar o update no banco)
                service.autorizarUsuario(usuarioAlvo.getNomeUsuario());
                
                JOptionPane.showMessageDialog(view, "Acesso liberado com sucesso!");
                
                // Recarrega a tabela para mudar o texto de "Não" para "Sim"
                carregarTabela();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Erro: " + e.getMessage());
            }
        }
    }
    

    public void carregarTabela() {
        DefaultTableModel model = (DefaultTableModel) view.getTabTabelaUsuarios().getModel();
        model.setRowCount(0); // inicializa tabela limpa, por precaução...

        try {
            //  Busca todos os usuários do banco através do Service
            this.listaUsuariosCache = service.buscarTodos(); 
            
           //Declaração de classe para formatar data
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            //string local que recebe a data
            String dataFormatada = null;
            
            //percorre a lista e adiciona na tabela
            for (Usuario u : listaUsuariosCache) {
                
                // Define o texto do Perfil
                String perfilTexto = u.getNomeDePerfil();
                //pega data de criação
                if (u.getDataCadastro() != null) {
                    dataFormatada = u.getDataCadastro().format(formatador);
                }
                
                 String autorizadoTexto = u.isAutorizado() ? "Sim" : "Não"; //preenche de acordo se o usuario já é autorizado
                 
                //Adiciona as linhas na tabela:               
                model.addRow(new Object[]{
                    u.getNome(),          // Coluna 1
                    u.getNomeUsuario(),   // Coluna 2
                    perfilTexto,          // Coluna 3
                    dataFormatada,        // Coluna 4
                    0,                    // Coluna 5 (Notificações Enviadas - Placeholder)
                    0,                    // Coluna 6 (Notificações Lidas - Placeholder)
                    autorizadoTexto       // Coluna 7
                    
                    //service.contarNotificacoesEnviadas(u.getId()),   //FAZER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //service.contarNotificacoesLidas(u.getId())        //FAZER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar usuários: " + e.getMessage());
            //e.printStackTrace(); REMOVER
        }
    }
    
    private void alterarUsuario() {
        // Verifica qual linha da tabela está selecionada
        int linha = view.getTabTabelaUsuarios().getSelectedRow();

        // se não houve linha estiver selecionada (retorna -1), avisa o usuário
        if (linha == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário na tabela para alterar.");
            return;
        }

        // Recupera o objeto Usuario real usando a lista em memória (cache)
        Usuario usuarioAlvo = listaUsuariosCache.get(linha);

        //Abre o formulário passando o usuário para ser editado
        abrirFormulario(usuarioAlvo);
    }
    
    private void excluirUsuario() {
        //guarda linha selecionada
        int linha = view.getTabTabelaUsuarios().getSelectedRow();

        
        //mensagem se nenhuma linha foi selecionada
        if (linha == -1) { 
            JOptionPane.showMessageDialog(view, "Selecione um usuário para excluir.");
            return;
        }

        //recupera o objeto do usuário que será excluído da lista cache
        Usuario usuarioASerExcluido = listaUsuariosCache.get(linha);

        //janela de confirmação de exclusão
        int confirm = JOptionPane.showConfirmDialog(view, 
                "Tem certeza que deseja excluir o usuário " + usuarioASerExcluido.getNome() + "?", 
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                
                // Chamamos o método do seu Service passando QUEM EXCLUI e QUEM É EXCLUÍDO
                service.excluirUsuario(this.usuarioLogado, usuarioASerExcluido);
                
                //Se não deu erro, atualiza a tela
                carregarTabela(); 
                JOptionPane.showMessageDialog(view, "Usuário excluído com sucesso!");
                
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(view, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                //erros genéricos de banco ou sistema
                JOptionPane.showMessageDialog(view, "Erro inesperado: " + e.getMessage());
                
            }
        }
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.PerfilAdministrador;
import model.PerfilComum;
import model.Usuario;
import service.UsuarioService;
import view.FormularioUsuarioView;
import com.pss.senha.validacao.ValidadorSenha;
import java.util.List;


/**
 *
 * @author Erko
 */
public class FormularioUsuarioPresenter {
    private FormularioUsuarioView view;
    private UsuarioService service;
    private Usuario usuarioEdicao;
    
    private ManterUsuarioPresenter presenterPai;
    
 
    //se usuarioEdicao for null significa que não é edição
    public FormularioUsuarioPresenter(FormularioUsuarioView view, UsuarioService service, Usuario usuarioEdicao, ManterUsuarioPresenter presenterPai) {
        this.view = view;
        this.service = service;
        this.usuarioEdicao = usuarioEdicao;
        this.presenterPai = presenterPai; // guarda presenter pai ManterUsuarioPresenter

        configurarEstadoInicial();
        configurarListeners();
    }
    
    
    private void configurarEstadoInicial() {
        if (usuarioEdicao != null) {
            view.setTitle("Alterar Usuário");
            view.getTxtNome().setText(usuarioEdicao.getNome());
            view.getTxtUsername().setText(usuarioEdicao.getNomeUsuario());
            view.getTxtSenha().setText(usuarioEdicao.getSenha());
            
            view.getChkAdministrador().setSelected(usuarioEdicao.isAdministrador());
            view.getChkAutorizado().setSelected(usuarioEdicao.isAutorizado());
            
            view.getTxtUsername().setEditable(false); 
        } else {
            view.setTitle("Novo Usuário");
            view.getChkAutorizado().setSelected(false);
            view.getChkAdministrador().setSelected(false);
        }
        
        
        
    }

    private void configurarListeners() {
        view.getBtnSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvar();
            }
        });

        view.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }
    
    private void salvar() {
        try {
            String nome = view.getTxtNome().getText();
            String username = view.getTxtUsername().getText();
            String senha = view.getTxtSenha().getText();
            boolean isAdmin = view.getChkAdministrador().isSelected();
            boolean isAutorizado = view.getChkAutorizado().isSelected();

            if (nome.isEmpty() || username.isEmpty() || senha.isEmpty()) { //validação para campos vazios
                JOptionPane.showMessageDialog(view, "Preencha todos os campos obrigatórios.");
                return;
            }
            
            ValidadorSenha validador = new ValidadorSenha(); //validador de senha par a o requisito especifico para senha importado do github
            List<String> errosSenha = validador.validar(senha);
            
            // Se a lista não tiver vazia significa que a senha é ruim
            if (!errosSenha.isEmpty()) {
                
                String msgErro = "Sua senha é fraca:\n";
                
                for (String erro : errosSenha) {
                    msgErro += "- " + erro + "\n"; // Concatena erros
                }
                
                
                throw new IllegalArgumentException(msgErro); //lança exceção de parametros invalidos para senha fraca
            }
            

            if (usuarioEdicao == null) {
                // --- NOVO ---
                Usuario novo = new Usuario(nome, username, senha);
                service.cadastrarNovoUsuario(novo);
                JOptionPane.showMessageDialog(view, "Usuário cadastrado com sucesso!");
            } else {
                // --- EDITAR ---
                usuarioEdicao.setNome(nome);
                usuarioEdicao.setSenha(senha);
                usuarioEdicao.setAutorizado(isAutorizado);
                
                if (isAdmin) {
                    usuarioEdicao.setPerfil(new PerfilAdministrador());
                } else {
                    usuarioEdicao.setPerfil(new PerfilComum());
                }
                
                
                service.atualizarUsuario(usuarioEdicao); 
                JOptionPane.showMessageDialog(view, "Usuário atualizado com sucesso!");
            }

            // MUDANÇA 3: Chamamos diretamente o método público do pai
            if (presenterPai != null) {
                presenterPai.carregarTabela();
            }

            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar: " + ex.getMessage());
        }
    }
    
    

}



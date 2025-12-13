/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import com.pss.senha.validacao.ValidadorSenha;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import model.Usuario;
import service.UsuarioService;
import view.AlterarSenhaView;

/**
 *
 * @author Erko
 */
public class AlterarSenhaPresenter {
    private AlterarSenhaView view;
    private UsuarioService service;
    private Usuario usuarioLogado;
    
    
    public AlterarSenhaPresenter(AlterarSenhaView view, UsuarioService service, Usuario usuarioLogado) {
        this.view = view;
        this.service = service;
        this.usuarioLogado = usuarioLogado;

        configurarListeners();
        this.view.setVisible(true);
    }
    
    
    private void configurarListeners() {
        view.getBtnConfirmar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarNovaSenha();
            }
        });

        view.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }
    
    private void salvarNovaSenha() {
        try {
            String senhaAtual = new String(view.getTxtSenhaAtual().getText());
            String novaSenha = new String(view.getTxtSenhaNova().getText());

            // valida se a senha atual do usuario
            if (!senhaAtual.equals(usuarioLogado.getSenha())) {
                JOptionPane.showMessageDialog(view, "A senha atual está incorreta.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // valida a força da nova senha com o modulo importado pelo maven (RNF08)
            ValidadorSenha validador = new ValidadorSenha();
            
            //essa lista guarda os problemas com a senha nova
            List<String> erros = validador.validar(novaSenha); 
            
            // se ele não estiver vazio significa que a senha é fraca
            if (!erros.isEmpty()) {
                String msg = "Nova senha fraca:\n";
                for (String erro : erros) 
                    msg = msg + "- " + erro + "\n";     //concatena erros na string para depois ser lançada exception
                throw new IllegalArgumentException(msg);
            }

            //atualiza o objeto e salva no banco
            usuarioLogado.setSenha(novaSenha);
            service.atualizarUsuario(usuarioLogado);

            JOptionPane.showMessageDialog(view, "Senha alterada com sucesso!");
            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro: " + ex.getMessage());
        }
    }
}

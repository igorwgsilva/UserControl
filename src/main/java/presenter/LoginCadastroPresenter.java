/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 *
 * @author Igor Wendling
 */
import com.pss.senha.validacao.ValidadorSenha;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import model.Usuario;
import service.UsuarioService;
import view.LoginCadastroView;


public class LoginCadastroPresenter {

    private LoginCadastroView view; //Jdiag para ficar independente do MDI, a janela principal só aparece quando o login é realizado com sucesso
    private  UsuarioService usuarioService;

    
    
    
    public LoginCadastroPresenter(UsuarioService usuarioService) {
        
        this.usuarioService = usuarioService;
        
        // Inicializa a view como Modal (bloqueia o fundo)
        this.view = new LoginCadastroView();

        configurarView();
        configurarListeners();
        
        this.view.setVisible(true);
    }

    private void configurarView() { //VERIFICAR depois se pode melhorar
        this.view.setLocationRelativeTo(null); // Centraliza na tela

        
    }

    private void configurarListeners() {
       // CADASTRAR ------------------------------------------------------------------
        view.getBtnSolicitarCadastro().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarCadastro();
            }
        });

        // LOGIN ------------------------------------------------------------------
        view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }
    
    
    // 
    // CADASTRO========================================================================
    private void realizarCadastro() {
        try {
            String nome = view.getTxtNome().getText();
            String username = view.getTxtUsername().getText();
            String senha = view.getTxtSenha().getText();

            // validação de nome  obrigatório no cadastro
            if (nome.trim().isEmpty() || username.trim().isEmpty() || senha.trim().isEmpty()) {
                throw new IllegalArgumentException("Para cadastrar, preencha Nome, Usuário e Senha.");
            }

            // validação de senha forte (RNF08 -dependência externa)
            ValidadorSenha validador = new ValidadorSenha();
            List<String> erros = validador.validar(senha);
            
            if (!erros.isEmpty()) { //se não estiver vazia essa coleção então tem problemas na senha
                String msg = "Senha insegura:\n";
                for (String erro : erros) {
                    msg += "- " + erro + "\n";
                }
                throw new IllegalArgumentException(msg);
            }

            // cadastra no banco
            Usuario novo = new Usuario(nome, username, senha);
            usuarioService.cadastrarNovoUsuario(novo); 

            // se o banco estava vazio, o service mudou o perfil deste objeto para admin. aqui verifica se essa alteração aconteceu.
            // se essa verificacao foi verdadeira então quer dizer que é o primeiro admin  
            if (novo.isAdministrador()) {
                 JOptionPane.showMessageDialog(view, "Sucesso! Você é o Administrador inicial do sistema.");
                 entrarNoSistema(novo);
            } else {
                 JOptionPane.showMessageDialog(view, "Solicitação enviada! Aguarde um administrador liberar seu acesso.");
                 limparCampos();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao cadastrar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void realizarLogin() {
        try {
            String username = view.getTxtUsername().getText();
            String senha = view.getTxtSenha().getText();
            
            if (username.trim().isEmpty() || senha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Preencha Usuário e Senha para entrar.");
                return;
            }

            // autenticação
            boolean autenticado = usuarioService.autenticarUsuario(username, senha);

            if (autenticado) {
                // envelopa  o objeto completo para a sessão, ele pode ser null, em um optional
                Optional<Usuario> usuarioLogadoOpt = Optional.ofNullable(usuarioService.buscarPorNomeDeUsuario(username));
                
                if (usuarioLogadoOpt.isPresent()) {
                    entrarNoSistema(usuarioLogadoOpt.get());
                } else {
                     JOptionPane.showMessageDialog(view, "Erro crítico: Usuário autenticado mas não encontrado.");
                }
            } else {
                JOptionPane.showMessageDialog(view, "Usuário ou senha inválidos.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            }

        } catch (IllegalStateException e) {
            // captura msg de "usuário pendente" vinda do service
            JOptionPane.showMessageDialog(view, e.getMessage(), "Aguarde Aprovação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro técnico no login: " + e.getMessage());
            e.printStackTrace();
        }
        
        
        
    }
    
    private void entrarNoSistema(Usuario usuario) {
        view.dispose(); // fecha tela login/cadastro
        
        // chama a presenter da tela inicial
        new JanelaPrincipalPresenter(usuario);
    }
    
    private void limparCampos() {
        view.getTxtNome().setText("");
        view.getTxtSenha().setText("");
        
    }
}

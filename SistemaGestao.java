import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SistemaGestao {

    // ==========================================
    // 1. CONFIGURAÇÃO CONEXÃO BANCO DE DADOS
    // ==========================================
    private static final String URL = "jdbc:mysql://localhost:3306/gestao_projetos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String SENHA = "root"; // Altere para a sua senha local se necessário

    public static Connection getConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do MySQL não encontrado!", e);
        }
    }

    // ==========================================
    // 2. MODELO DE DADOS (CLASSES ENCAPSULADAS)
    // ==========================================
    public static class Usuario {
        private int idUsuario;
        private String nomeCompleto;
        private String perfil;

        public int getIdUsuario() { return idUsuario; }
        public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
        public String getNomeCompleto() { return nomeCompleto; }
        public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
        public String getPerfil() { return perfil; }
        public void setPerfil(String perfil) { this.perfil = perfil; }
    }

    // ==========================================
    // 3. PERSISTÊNCIA DE DADOS (MÉTODOS DAO)
    // ==========================================
    public static class UsuarioDAO {
        public Usuario autenticar(String login, String senha) {
            String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
            try (Connection conn = getConexao();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, login);
                stmt.setString(2, senha);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Usuario u = new Usuario();
                        u.setIdUsuario(rs.getInt("id_usuario"));
                        u.setNomeCompleto(rs.getString("nome_completo"));
                        u.setPerfil(rs.getString("perfil"));
                        return u;
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco: " + e.getMessage(), "Erro de Banco", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        }
    }

    // ==========================================
    // 4. INTERFACE GRÁFICA (TELA DE LOGIN)
    // ==========================================
    public static class TelaLogin extends JFrame {
        private JTextField txtLogin;
        private JPasswordField txtSenha;
        private JButton btnEntrar;

        public TelaLogin() {
            setTitle("Autenticação do Sistema");
            setSize(350, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(3, 2, 10, 10));

            add(new JLabel("  Login (Usuário):"));
            txtLogin = new JTextField();
            add(txtLogin);

            add(new JLabel("  Senha:"));
            txtSenha = new JPasswordField();
            add(txtSenha);

            add(new JLabel("")); 
            btnEntrar = new JButton("Entrar");
            add(btnEntrar);

            btnEntrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String login = txtLogin.getText();
                    String senha = new String(txtSenha.getPassword());

                    UsuarioDAO dao = new UsuarioDAO();
                    Usuario usuarioLogado = dao.autenticar(login, senha);

                    if (usuarioLogado != null) {
                        JOptionPane.showMessageDialog(null, 
                            "Login realizado com sucesso!\n\n" +
                            "Welcome, " + usuarioLogado.getNomeCompleto() + "\n" +
                            "Perfil de Acesso: " + usuarioLogado.getPerfil(), 
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        
                        dispose(); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
    }

    // ==========================================
    // 5. MÉTODO PRINCIPAL DE EXECUÇÃO
    // ==========================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}

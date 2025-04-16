package qlsv_v2;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class QLSV_v2 extends JFrame {
    private Connection conn;
    private CardLayout cardLayout;  // Khai báo CardLayout
    private final JPanel mainPanel;

    public QLSV_v2() {
        // Kết nối CSDL
        connectDatabase();

        // Khởi tạo CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);  // Gán CardLayout cho mainPanel
        
        // Thêm các panel vào mainPanel
        mainPanel.add(new MonHocPanel(conn, cardLayout), "MonHoc");
        mainPanel.add(new DangKyPanel(conn, cardLayout), "DangKy");
        mainPanel.add(new KetQuaHocTapPanel(conn, cardLayout), "KetQua");
        mainPanel.add(new SinhVienPanel(conn, cardLayout), "SinhVien");  // Thêm SinhVienPanel vào mainPanel

        setTitle("Quản Lý Sinh Viên");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Call createLoginForm() to display login form at first
        createLoginForm();
    }

    private void connectDatabase() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QLYSV2;user=sa;password=123456;encrypt=true;trustServerCertificate=true;");
            if (conn != null) {
                System.out.println("Kết nối đến cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối không thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLoginForm() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createTitledBorder("Đăng Nhập"));
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Đăng Nhập");
        btnLogin.addActionListener(e -> handleLogin(txtUsername, txtPassword));
        loginPanel.add(new JLabel("Tên Đăng Nhập:"));
        loginPanel.add(txtUsername);
        loginPanel.add(new JLabel("Mật Khẩu:"));
        loginPanel.add(txtPassword);
        loginPanel.add(btnLogin);
        add(loginPanel, BorderLayout.CENTER);
    }

    private void handleLogin(JTextField txtUsername, JPasswordField txtPassword) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        try {
            String sql = "SELECT * FROM TaiKhoan WHERE TenDN = ? AND MatKhau = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                getContentPane().removeAll();
                getContentPane().add(mainPanel);
                cardLayout.show(mainPanel, "SinhVien");  // Hiển thị SinhVienPanel sau khi đăng nhập thành công
                validate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QLSV_v2().setVisible(true));
    }
}
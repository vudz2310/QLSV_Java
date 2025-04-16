package qlsv_v2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class KetQuaHocTapPanel extends JPanel {
    private Connection conn;
    private JTextField txtMaDK, txtDiem;
    private JTable tblKetQua;
    private DefaultTableModel modelKetQua;
    private CardLayout cardLayout;

    public KetQuaHocTapPanel(Connection conn, CardLayout cardLayout) {
        this.conn = conn;
        this.cardLayout = cardLayout;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Quản Lý Kết Quả Học Tập"));

        // Khu vực nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        txtMaDK = new JTextField();
        txtDiem = new JTextField();
        inputPanel.add(new JLabel("Mã DK:"));
        inputPanel.add(txtMaDK);
        inputPanel.add(new JLabel("Điểm:"));
        inputPanel.add(txtDiem);

        // JTable hiển thị kết quả
        modelKetQua = new DefaultTableModel(new String[]{"Mã DK", "Điểm"}, 0);
        tblKetQua = new JTable(modelKetQua);
        JScrollPane tablePane = new JScrollPane(tblKetQua);

        // Các nút chức năng (CRUD)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập Nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm Mới");
        btnAdd.addActionListener(e -> addKetQua());
        btnUpdate.addActionListener(e -> updateKetQua());
        btnDelete.addActionListener(e -> deleteKetQua());
        btnRefresh.addActionListener(e -> loadKetQua());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        // Thêm các phần vào panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);  // Phần nhập liệu
        mainPanel.add(tablePane, BorderLayout.CENTER);  // Phần hiển thị bảng
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Các nút CRUD ở dưới bảng dữ liệu

        // Thêm panel điều hướng (nút chuyển bảng)
        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton btnToSinhVien = new JButton("Sinh Viên");
        JButton btnToMonHoc = new JButton("Môn Học");
        JButton btnToDangKy = new JButton("Đăng Ký");
        btnToSinhVien.addActionListener(e -> cardLayout.show(getParent(), "SinhVien"));
        btnToMonHoc.addActionListener(e -> cardLayout.show(getParent(), "MonHoc"));
        btnToDangKy.addActionListener(e -> cardLayout.show(getParent(), "DangKy"));
        navigationPanel.add(btnToSinhVien);
        navigationPanel.add(btnToMonHoc);
        navigationPanel.add(btnToDangKy);

        // Thêm các phần vào giao diện chính
        add(mainPanel, BorderLayout.CENTER);  // Thêm phần nhập liệu, bảng và nút CRUD vào giữa
        add(navigationPanel, BorderLayout.SOUTH);  // Thêm nút chuyển bảng ở cuối

        loadKetQua();
    }

    private void loadKetQua() {
        modelKetQua.setRowCount(0);
        try {
            String sql = "SELECT * FROM tblKetQuaHocTap";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                modelKetQua.addRow(new Object[]{
                        rs.getString("MaDK"),
                        rs.getDouble("Diem")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

        private void addKetQua() {
        try {
            // Kiểm tra xem MaDK có tồn tại trong bảng hay không
            String checkSQL = "SELECT COUNT(*) FROM tblKetQuaHocTap WHERE MaDK = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setString(1, txtMaDK.getText());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Nếu MaDK đã tồn tại, hiển thị thông báo lỗi
                JOptionPane.showMessageDialog(this, "Mã DK này đã tồn tại. Vui lòng chọn mã DK khác.");
            } else {
                // Nếu MaDK chưa tồn tại, thực hiện thêm mới
                String sql = "INSERT INTO tblKetQuaHocTap (MaDK, Diem) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, txtMaDK.getText());
                pstmt.setDouble(2, Double.parseDouble(txtDiem.getText()));  // Chuyển đổi điểm thành Double
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm kết quả thành công!");
                loadKetQua();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm kết quả thất bại!");
        }
    }
    

    private void updateKetQua() {
        try {
            String sql = "UPDATE tblKetQuaHocTap SET Diem=? WHERE MaDK=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, Double.parseDouble(txtDiem.getText()));
            pstmt.setString(2, txtMaDK.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật kết quả thành công!");
            loadKetQua();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cập nhật kết quả thất bại!");
        }
    }

    private void deleteKetQua() {
        try {
            String sql = "DELETE FROM tblKetQuaHocTap WHERE MaDK=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaDK.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xóa kết quả thành công!");
            loadKetQua();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Xóa kết quả thất bại!");
        }
    }
}
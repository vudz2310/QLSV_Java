package qlsv_v2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DangKyPanel extends JPanel {
    private Connection conn;
    private JTextField txtMaDK, txtMaSV_DK, txtMaMH_DK, txtNgayDK;
    private JTable tblDangKy;
    private DefaultTableModel modelDangKy;
    private CardLayout cardLayout;

    public DangKyPanel(Connection conn, CardLayout cardLayout) {
        this.conn = conn;
        this.cardLayout = cardLayout;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Quản Lý Đăng Ký"));

        // Khu vực nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        txtMaDK = new JTextField();
        txtMaSV_DK = new JTextField();
        txtMaMH_DK = new JTextField();
        txtNgayDK = new JTextField();

        inputPanel.add(new JLabel("Mã ĐK:"));
        inputPanel.add(txtMaDK);
        inputPanel.add(new JLabel("Mã SV:"));
        inputPanel.add(txtMaSV_DK);
        inputPanel.add(new JLabel("Mã MH:"));
        inputPanel.add(txtMaMH_DK);
        inputPanel.add(new JLabel("Ngày ĐK:"));
        inputPanel.add(txtNgayDK);

        // JTable hiển thị đăng ký
        modelDangKy = new DefaultTableModel(new String[]{"Mã ĐK", "Mã SV", "Mã MH", "Ngày ĐK"}, 0);
        tblDangKy = new JTable(modelDangKy);
        JScrollPane tablePane = new JScrollPane(tblDangKy);

        // Các nút chức năng (CRUD)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập Nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm Mới");

        btnAdd.addActionListener(e -> addDangKy());
        btnUpdate.addActionListener(e -> updateDangKy());
        btnDelete.addActionListener(e -> deleteDangKy());
        btnRefresh.addActionListener(e -> loadDangKy());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        // Các nút chuyển bảng
        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton btnToSinhVien = new JButton("Sinh Viên");
        JButton btnToMonHoc = new JButton("Môn Học");
        JButton btnToKetQua = new JButton("Kết Quả");

        btnToSinhVien.addActionListener(e -> cardLayout.show(getParent(), "SinhVien"));
        btnToMonHoc.addActionListener(e -> cardLayout.show(getParent(), "MonHoc"));
        btnToKetQua.addActionListener(e -> cardLayout.show(getParent(), "KetQua"));

        navigationPanel.add(btnToSinhVien);
        navigationPanel.add(btnToMonHoc);
        navigationPanel.add(btnToKetQua);

        // Bố trí lại các phần vào panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);  // Phần nhập liệu
        mainPanel.add(tablePane, BorderLayout.CENTER);  // Phần hiển thị bảng
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Phần các nút CRUD

        // Đặt panel điều hướng ở dưới cùng để tránh che khuất nhập liệu
        add(mainPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        loadDangKy();
    }



    private void loadDangKy() {
        modelDangKy.setRowCount(0);
        try {
            String sql = "SELECT * FROM tblDangKy";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                modelDangKy.addRow(new Object[]{
                        rs.getString("MaDK"),
                        rs.getString("MaSV"),
                        rs.getString("MaMH"),
                        rs.getDate("NgayDangKy")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addDangKy() {
        try {
            String sql = "INSERT INTO tblDangKy VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaDK.getText());
            pstmt.setString(2, txtMaSV_DK.getText());
            pstmt.setString(3, txtMaMH_DK.getText());
            pstmt.setString(4, txtNgayDK.getText()); 
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm đăng ký thành công!");
            loadDangKy();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm đăng ký thất bại!");
        }
    }

    private void updateDangKy() {
        try {
            String sql = "UPDATE tblDangKy SET MaSV=?, MaMH=?, NgayDangKy=? WHERE MaDK=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaSV_DK.getText());
            pstmt.setString(2, txtMaMH_DK.getText());
            pstmt.setString(3, txtNgayDK.getText());  
            pstmt.setString(4, txtMaDK.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật đăng ký thành công!");
            loadDangKy();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cập nhật đăng ký thất bại!");
        }
    }

    private void deleteDangKy() {
        try {
            String sql = "DELETE FROM tblDangKy WHERE MaDK=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaDK.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xóa đăng ký thành công!");
            loadDangKy();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Xóa đăng ký thất bại!");
        }
    }
}

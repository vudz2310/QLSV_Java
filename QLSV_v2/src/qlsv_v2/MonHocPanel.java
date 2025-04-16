package qlsv_v2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MonHocPanel extends JPanel {
    private Connection conn;
    private JTextField txtMaMH, txtTenMH, txtSoTinChi;
    private JTable tblMonHoc;
    private DefaultTableModel modelMonHoc;
    private CardLayout cardLayout;

    public MonHocPanel(Connection conn, CardLayout cardLayout) {
        this.conn = conn;
        this.cardLayout = cardLayout;  // Gán CardLayout

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Quản Lý Môn Học"));

        // Khu vực nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        txtMaMH = new JTextField();
        txtTenMH = new JTextField();
        txtSoTinChi = new JTextField();

        inputPanel.add(new JLabel("Mã Môn Học:"));
        inputPanel.add(txtMaMH);
        inputPanel.add(new JLabel("Tên Môn Học:"));
        inputPanel.add(txtTenMH);
        inputPanel.add(new JLabel("Số Tín Chỉ:"));
        inputPanel.add(txtSoTinChi);

        // JTable hiển thị môn học
        modelMonHoc = new DefaultTableModel(new String[]{"Mã MH", "Tên Môn Học", "Số Tín Chỉ"}, 0);
        tblMonHoc = new JTable(modelMonHoc);
        JScrollPane tablePane = new JScrollPane(tblMonHoc);

        // Các nút chức năng (CRUD)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập Nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm Mới");

        btnAdd.addActionListener(e -> addMonHoc());
        btnUpdate.addActionListener(e -> updateMonHoc());
        btnDelete.addActionListener(e -> deleteMonHoc());
        btnRefresh.addActionListener(e -> loadMonHoc());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        // Các nút chuyển bảng
        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton btnToSinhVien = new JButton("Sinh Viên");
        JButton btnToDangKy = new JButton("Đăng Ký");
        JButton btnToKetQua = new JButton("Kết Quả");

        btnToSinhVien.addActionListener(e -> cardLayout.show(getParent(), "SinhVien"));
        btnToDangKy.addActionListener(e -> cardLayout.show(getParent(), "DangKy"));
        btnToKetQua.addActionListener(e -> cardLayout.show(getParent(), "KetQua"));

        navigationPanel.add(btnToSinhVien);
        navigationPanel.add(btnToDangKy);
        navigationPanel.add(btnToKetQua);

        // Bố trí lại các phần vào panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);  // Phần nhập liệu
        mainPanel.add(tablePane, BorderLayout.CENTER);  // Phần hiển thị bảng
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Phần các nút CRUD

        // Đặt panel điều hướng ở dưới cùng để tránh che khuất nhập liệu
        add(mainPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        loadMonHoc();
    }

    MonHocPanel(Connection conn, CardLayout cardLayout, JPanel mainPanel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void loadMonHoc() {
        modelMonHoc.setRowCount(0);
        try {
            String sql = "SELECT * FROM tblMonHoc";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                modelMonHoc.addRow(new Object[]{
                        rs.getString("MaMH"),
                        rs.getString("TenMonHoc"),
                        rs.getInt("SoTinChi")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addMonHoc() {
        try {
            String sql = "INSERT INTO tblMonHoc VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaMH.getText());
            pstmt.setString(2, txtTenMH.getText());
            pstmt.setInt(3, Integer.parseInt(txtSoTinChi.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm môn học thành công!");
            loadMonHoc();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm môn học thất bại!");
        }
    }

    private void updateMonHoc() {
        try {
            String sql = "UPDATE tblMonHoc SET TenMonHoc=?, SoTinChi=? WHERE MaMH=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtTenMH.getText());
            pstmt.setInt(2, Integer.parseInt(txtSoTinChi.getText()));
            pstmt.setString(3, txtMaMH.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật môn học thành công!");
            loadMonHoc();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cập nhật môn học thất bại!");
        }
    }

    private void deleteMonHoc() {
        try {
            String sql = "DELETE FROM tblMonHoc WHERE MaMH=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaMH.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xóa môn học thành công!");
            loadMonHoc();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Xóa môn học thất bại!");
        }
    }
}
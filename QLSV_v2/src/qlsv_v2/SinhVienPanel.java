package qlsv_v2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SinhVienPanel extends JPanel {
    private Connection conn;
    private JTextField txtMaSV, txtHoTen, txtNgaySinh, txtGioiTinh, txtDiaChi, txtDienThoai;
    private JTable tblSinhVien;
    private DefaultTableModel modelSinhVien;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public SinhVienPanel(Connection conn, CardLayout cardLayout) {
        this.conn = conn;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Quản Lý Sinh Viên"));

        // Khu vực nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        txtMaSV = new JTextField();
        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField();
        txtGioiTinh = new JTextField();
        txtDiaChi = new JTextField();
        txtDienThoai = new JTextField();
        inputPanel.add(new JLabel("Mã SV:"));
        inputPanel.add(txtMaSV);
        inputPanel.add(new JLabel("Họ Tên:"));
        inputPanel.add(txtHoTen);
        inputPanel.add(new JLabel("Ngày Sinh (YYYY-MM-DD):"));
        inputPanel.add(txtNgaySinh);
        inputPanel.add(new JLabel("Giới Tính:"));
        inputPanel.add(txtGioiTinh);
        inputPanel.add(new JLabel("Địa Chỉ:"));
        inputPanel.add(txtDiaChi);
        inputPanel.add(new JLabel("Điện Thoại:"));
        inputPanel.add(txtDienThoai);

        // JTable hiển thị sinh viên
        modelSinhVien = new DefaultTableModel(new String[]{"Mã SV", "Họ Tên", "Ngày Sinh", "Giới Tính", "Địa Chỉ", "Điện Thoại"}, 0);
        tblSinhVien = new JTable(modelSinhVien);
        JScrollPane tablePane = new JScrollPane(tblSinhVien);

        // Các nút chức năng (CRUD)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập Nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm Mới");
        btnAdd.addActionListener(e -> addSinhVien());
        btnUpdate.addActionListener(e -> updateSinhVien());
        btnDelete.addActionListener(e -> deleteSinhVien());
        btnRefresh.addActionListener(e -> loadSinhVien());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        // Các nút chuyển bảng
        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton btnToMonHoc = new JButton("Môn Học");
        JButton btnToDangKy = new JButton("Đăng Ký");
        JButton btnToKetQua = new JButton("Kết Quả");
        btnToMonHoc.addActionListener(e -> cardLayout.show(getParent(), "MonHoc"));
        btnToDangKy.addActionListener(e -> cardLayout.show(getParent(), "DangKy"));
        btnToKetQua.addActionListener(e -> cardLayout.show(getParent(), "KetQua"));
        navigationPanel.add(btnToMonHoc);
        navigationPanel.add(btnToDangKy);
        navigationPanel.add(btnToKetQua);

        // Bố trí lại các phần vào panel chính
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);  // Phần nhập liệu
        mainPanel.add(tablePane, BorderLayout.CENTER);  // Phần hiển thị bảng
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Phần các nút CRUD

        // Đặt panel điều hướng ở dưới cùng để tránh che khuất nhập liệu
        add(mainPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        loadSinhVien();  // Load data on panel initialization
    }

    private void loadSinhVien() {
        modelSinhVien.setRowCount(0);
        try {
            String sql = "SELECT * FROM tblSinhVien";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                modelSinhVien.addRow(new Object[]{
                        rs.getString("MaSV"),
                        rs.getString("HoTen"),
                        rs.getDate("NgaySinh"),
                        rs.getString("GioiTinh"),
                        rs.getString("DiaChi"),
                        rs.getString("DienThoai")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addSinhVien() {
        try {
            String sql = "INSERT INTO tblSinhVien VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaSV.getText());
            pstmt.setString(2, txtHoTen.getText());
            pstmt.setString(3, txtNgaySinh.getText());
            pstmt.setString(4, txtGioiTinh.getText());
            pstmt.setString(5, txtDiaChi.getText());
            pstmt.setString(6, txtDienThoai.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            loadSinhVien();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại!");
        }
    }

    private void updateSinhVien() {
        try {
            String sql = "UPDATE tblSinhVien SET HoTen = ?, NgaySinh = ?, GioiTinh = ?, DiaChi = ?, DienThoai = ? WHERE MaSV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtHoTen.getText());
            pstmt.setString(2, txtNgaySinh.getText());
            pstmt.setString(3, txtGioiTinh.getText());
            pstmt.setString(4, txtDiaChi.getText());
            pstmt.setString(5, txtDienThoai.getText());
            pstmt.setString(6, txtMaSV.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadSinhVien();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void deleteSinhVien() {
        try {
            String sql = "DELETE FROM tblSinhVien WHERE MaSV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtMaSV.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!");
            loadSinhVien();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Xóa sinh viên thất bại!");
        }
    }}
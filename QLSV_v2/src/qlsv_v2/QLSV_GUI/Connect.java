/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qlsv_v2.QLSV_GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author nitro
 */
class Connect {
    private Connection conn;
    public void connectDatabase() {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Set up the connection string
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QLYSV2;user=sa;password=123456;encrypt=true;trustServerCertificate=true;";
            
            // Establish the connection
            conn = DriverManager.getConnection(url);
            
            // Check connection status
            if (conn != null) {
                System.out.println("Kết nối đến cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối không thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Server JDBC Driver không tìm thấy!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối không thành công. Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getter for the connection
    public Connection getConnection() {
        return conn;
    }
    // Test connection
    public static void main(String[] args) {
        Connect conn = new Connect();
        conn.connectDatabase();
    }

}



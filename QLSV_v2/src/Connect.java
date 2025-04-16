import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private Connection conn;

    // Method to connect to the database
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

    // Method to close the connection
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Kết nối đã được đóng!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Test connection
    public static void main(String[] args) {
        Connect connect = new Connect();
        connect.connectDatabase();
        // Close connection after test
        connect.closeConnection();
    }
}
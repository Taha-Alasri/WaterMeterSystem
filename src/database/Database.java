package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Database {
    private static Database instance;
    private Connection connection;
    
    private final String URL = "jdbc:mysql://localhost:3306/water_meter_db?serverTimezone=UTC";
    private final String USER = "root";
    private final String PASSWORD = "";
    
    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ تم الاتصال بقاعدة البيانات!");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "❌ لم يتم العثور على MySQL Driver!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ خطأ في الاتصال: " + e.getMessage());
        }
    }
    
    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
}

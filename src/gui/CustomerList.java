package gui;

import database.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerList extends JDialog {
    
    private DefaultTableModel model;
    
    public CustomerList(JFrame owner) {
        super(owner, "📋 قائمة العملاء", true);
        setSize(800, 550);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("📋 قائمة العملاء");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(13, 71, 161));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        String[] cols = {"الرقم", "الاسم", "الهاتف", "الشقة", "المبنى", "تاريخ التسجيل"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable table = new JTable(model);
        styleTable(table);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(187, 222, 251), 1));
        panel.add(scroll, BorderLayout.CENTER);
        
        loadData();
        add(panel);
        setVisible(true);
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(new Color(30, 41, 59));
        table.setBackground(Color.WHITE);
        table.setRowHeight(40);
        table.setGridColor(new Color(227, 242, 253));
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(227, 242, 253));
        table.getTableHeader().setForeground(new Color(13, 71, 161));
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));
    }
    
    private void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = Database.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customers ORDER BY customer_id");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getString("apartment_number"),
                    rs.getString("building_name"),
                    rs.getDate("created_at")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}

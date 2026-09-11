package gui;

import database.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class Reports extends JDialog {
    
    private DefaultTableModel model;
    private JTextArea txtReport;
    
    public Reports(JFrame owner) {
        super(owner, "📊 التقارير والفواتير", true);
        setSize(900, 650);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        
        JLabel lblTitle = new JLabel("📊 تقرير الفواتير - " + LocalDate.now());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(13, 71, 161));
        top.add(lblTitle, BorderLayout.WEST);
        
        JButton btnPrint = new JButton("🖨️ طباعة / PDF");
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPrint.setBackground(new Color(239, 108, 0));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFocusPainted(false);
        btnPrint.addActionListener(e -> printReport());
        top.add(btnPrint, BorderLayout.EAST);
        
        panel.add(top, BorderLayout.NORTH);
        
        // Table
        String[] cols = {"#", "العميل", "الشقة", "القراءة السابقة", "القراءة الحالية", "الوحدات", "السعر", "الإجمالي", "التاريخ"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable table = new JTable(model);
        styleTable(table);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(187, 222, 251), 1));
        panel.add(scroll, BorderLayout.CENTER);
        
        // Summary
        txtReport = new JTextArea();
        txtReport.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtReport.setBackground(new Color(255, 243, 224));
        txtReport.setForeground(new Color(230, 81, 0));
        txtReport.setEditable(false);
        txtReport.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        txtReport.setPreferredSize(new Dimension(0, 50));
        panel.add(txtReport, BorderLayout.SOUTH);
        
        loadData();
        add(panel);
        setVisible(true);
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setForeground(new Color(30, 41, 59));
        table.setBackground(Color.WHITE);
        table.setRowHeight(38);
        table.setGridColor(new Color(227, 242, 253));
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(227, 242, 253));
        table.getTableHeader().setForeground(new Color(13, 71, 161));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
    }
    
    private void loadData() {
        model.setRowCount(0);
        double grandTotal = 0;
        
        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "SELECT r.*, c.full_name, c.apartment_number FROM readings r " +
                         "JOIN customers c ON r.customer_id = c.customer_id " +
                         "ORDER BY r.reading_id DESC";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            int i = 1;
            while (rs.next()) {
                double total = rs.getDouble("total_amount");
                grandTotal += total;
                
                model.addRow(new Object[]{
                    i++,
                    rs.getString("full_name"),
                    rs.getString("apartment_number"),
                    rs.getInt("previous_reading"),
                    rs.getInt("current_reading"),
                    rs.getInt("consumed_units"),
                    rs.getDouble("unit_price"),
                    String.format("%.2f", total),
                    rs.getDate("reading_date")
                });
            }
            
            txtReport.setText("  💰 إجمالي المبالغ المستحقة: " + String.format("%.2f", grandTotal) + " ريال يمني");
            
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
    
    private void printReport() {
        try {
            boolean printed = txtReport.print();
            if (printed) {
                JOptionPane.showMessageDialog(this, "✅ تم إرسال التقرير للطباعة!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ خطأ في الطباعة: " + ex.getMessage());
        }
    }
}

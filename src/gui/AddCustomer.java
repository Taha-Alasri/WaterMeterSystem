package gui;

import database.Database;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddCustomer extends JDialog {
    
    private JTextField txtName, txtPhone, txtApt, txtBuilding;
    
    public AddCustomer(JFrame owner) {
        super(owner, "👤 إضافة عميل جديد", true);
        setSize(500, 520);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("👤 إضافة عميل جديد", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(13, 71, 161));
        lblTitle.setBounds(50, 20, 400, 40);
        panel.add(lblTitle);
        
       
        JLabel lblName = new JLabel("الاسم الكامل:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblName.setForeground(new Color(55, 71, 79));
        lblName.setBounds(50, 85, 400, 25);
        panel.add(lblName);
        
        txtName = new JTextField();
        txtName.setBounds(50, 112, 400, 42);
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(187, 222, 251), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtName);
        
       
        JLabel lblPhone = new JLabel("رقم الهاتف:");
        lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPhone.setForeground(new Color(55, 71, 79));
        lblPhone.setBounds(50, 170, 400, 25);
        panel.add(lblPhone);
        
        txtPhone = new JTextField();
        txtPhone.setBounds(50, 197, 400, 42);
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPhone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(187, 222, 251), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtPhone);
        
        // رقم الشقة
        JLabel lblApt = new JLabel("رقم الشقة:");
        lblApt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblApt.setForeground(new Color(55, 71, 79));
        lblApt.setBounds(50, 255, 400, 25);
        panel.add(lblApt);
        
        txtApt = new JTextField();
        txtApt.setBounds(50, 282, 400, 42);
        txtApt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtApt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(187, 222, 251), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtApt);
        
        // المبنى
        JLabel lblBuilding = new JLabel("اسم المبنى / العمارة:");
        lblBuilding.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuilding.setForeground(new Color(55, 71, 79));
        lblBuilding.setBounds(50, 340, 400, 25);
        panel.add(lblBuilding);
        
        txtBuilding = new JTextField();
        txtBuilding.setBounds(50, 367, 400, 42);
        txtBuilding.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuilding.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(187, 222, 251), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtBuilding);
        
        // زر الحفظ
        JButton btnSave = new JButton("💾 حفظ العميل");
        btnSave.setBounds(50, 435, 400, 48);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSave.setBackground(new Color(33, 150, 243));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> saveCustomer());
        panel.add(btnSave);
        
        add(panel);
        setVisible(true);
    }
    
    private void saveCustomer() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String apt = txtApt.getText().trim();
        String building = txtBuilding.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ الاسم والهاتف مطلوبان!");
            return;
        }
        
        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "INSERT INTO customers (full_name, phone, apartment_number, building_name) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, apt);
            stmt.setString(4, building);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "✅ تم حفظ العميل بنجاح!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ خطأ: " + ex.getMessage());
        }
    }
}

package gui;

import database.Database;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {
    
    private JTextField txtUser;
    private JPasswordField txtPass;
    
    public Login() {
        setTitle("🚰 نظام عدادات المياه - تسجيل الدخول");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // دائرة علوية
        JPanel circle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(33, 150, 243));
                g2.fillOval(0, 0, 90, 90);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
                g2.drawString("🚰", 22, 58);
            }
        };
        circle.setBounds(205, 40, 90, 90);
        circle.setOpaque(false);
        mainPanel.add(circle);
        
        JLabel lblTitle = new JLabel("نظام عدادات المياه", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(13, 71, 161));
        lblTitle.setBounds(50, 140, 400, 40);
        mainPanel.add(lblTitle);
        
        JLabel lblSub = new JLabel("Water Meter Billing System", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(100, 149, 237));
        lblSub.setBounds(50, 180, 400, 25);
        mainPanel.add(lblSub);
        

        JLabel lblUser = new JLabel("👤 اسم المستخدم");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(new Color(55, 71, 79));
        lblUser.setBounds(50, 240, 400, 25);
        mainPanel.add(lblUser);
        
        txtUser = new JTextField();
        txtUser.setBounds(50, 270, 400, 45);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUser.setBackground(Color.WHITE);
        txtUser.setForeground(new Color(33, 33, 33));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(187, 222, 251), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        mainPanel.add(txtUser);
        

        JLabel lblPass = new JLabel("🔒 كلمة المرور");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(new Color(55, 71, 79));
        lblPass.setBounds(50, 335, 400, 25);
        mainPanel.add(lblPass);
        
        txtPass = new JPasswordField();
        txtPass.setBounds(50, 365, 400, 45);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPass.setBackground(Color.WHITE);
        txtPass.setForeground(new Color(33, 33, 33));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(187, 222, 251), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        mainPanel.add(txtPass);
        
        // زر الدخول
        JButton btnLogin = new JButton("تسجيل الدخول");
        btnLogin.setBounds(50, 440, 400, 50);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> checkLogin());
        
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(25, 118, 210));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(33, 150, 243));
            }
        });
        mainPanel.add(btnLogin);
        
        JLabel lblHint = new JLabel("admin / admin123", SwingConstants.CENTER);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHint.setForeground(new Color(144, 164, 174));
        lblHint.setBounds(50, 510, 400, 20);
        mainPanel.add(lblHint);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void checkLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ يرجى ملء جميع الحقول", "تنبيه", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("full_name");
                JOptionPane.showMessageDialog(this, "✅ أهلاً " + name + "!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new Dashboard(name);
            } else {
                JOptionPane.showMessageDialog(this, "❌ اسم المستخدم أو كلمة المرور غير صحيحة", "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ خطأ: " + ex.getMessage());
        }
    }
}

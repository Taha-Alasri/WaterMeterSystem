package gui;

import database.Database;
import models.Customer;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SettingsFrame extends JDialog {

    private JTextField txtPrice;
    private JComboBox<Customer> comboDeleteCustomer;

    public SettingsFrame(JFrame owner) {
        super(owner, "اعدادات النظام", true);
        setSize(500, 500);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("اعدادات النظام", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 71, 161));
        lblTitle.setBounds(50, 20, 350, 35);
        panel.add(lblTitle);

        // قسم تعديل السعر
        JLabel lblPrice = new JLabel("سعر الوحدة الحالي:");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrice.setForeground(new Color(55, 71, 79));
        lblPrice.setBounds(50, 80, 350, 25);
        panel.add(lblPrice);

        txtPrice = new JTextField();
        txtPrice.setBounds(50, 110, 350, 45);
        txtPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtPrice.setForeground(new Color(46, 125, 50));
        txtPrice.setHorizontalAlignment(SwingConstants.CENTER);
        txtPrice.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtPrice);

        loadCurrentPrice();

        JButton btnSave = new JButton("حفظ السعر");
        btnSave.setBounds(50, 170, 350, 45);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(33, 150, 243));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> savePrice());
        panel.add(btnSave);

        // فاصل
        JSeparator sep = new JSeparator();
        sep.setBounds(50, 235, 350, 2);
        sep.setForeground(new Color(200, 200, 200));
        panel.add(sep);

        // قسم حذف عميل
        JLabel lblDeleteTitle = new JLabel("حذف عميل من النظام", SwingConstants.CENTER);
        lblDeleteTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDeleteTitle.setForeground(new Color(220, 20, 60));
        lblDeleteTitle.setBounds(50, 255, 350, 30);
        panel.add(lblDeleteTitle);

        JLabel lblSelect = new JLabel("اختر العميل:");
        lblSelect.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSelect.setForeground(new Color(55, 71, 79));
        lblSelect.setBounds(50, 295, 350, 25);
        panel.add(lblSelect);

        comboDeleteCustomer = new JComboBox<>();
        comboDeleteCustomer.setBounds(50, 322, 350, 40);
        comboDeleteCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboDeleteCustomer.setBackground(new Color(250, 250, 250));
        panel.add(comboDeleteCustomer);

        JButton btnDelete = new JButton("حذف العميل نهائياً");
        btnDelete.setBounds(50, 380, 350, 45);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> deleteCustomer());
        panel.add(btnDelete);

        JLabel warning = new JLabel("هذا الاجراء لا يمكن التراجع عنه!", SwingConstants.CENTER);
        warning.setForeground(Color.GRAY);
        warning.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        warning.setBounds(50, 435, 350, 20);
        panel.add(warning);

        add(panel);
        loadCustomersForDelete();
        setVisible(true);
    }

    private void loadCurrentPrice() {
        try {
            Connection conn = Database.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT unit_price FROM settings LIMIT 1");
            if (rs.next()) {
                txtPrice.setText(String.valueOf(rs.getDouble("unit_price")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void savePrice() {
        try {
            double price = Double.parseDouble(txtPrice.getText().trim());
            Connection conn = Database.getInstance().getConnection();
            String sql = "UPDATE settings SET unit_price = ? WHERE setting_id = 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, price);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "تم تحديث السعر الى: " + price);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "السعر غير صالح!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "خطأ: " + ex.getMessage());
        }
    }

    private void loadCustomersForDelete() {
        try {
            Connection conn = Database.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customers ORDER BY full_name");
            comboDeleteCustomer.removeAllItems();
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setFullName(rs.getString("full_name"));
                c.setPhone(rs.getString("phone"));
                c.setApartmentNumber(rs.getString("apartment_number"));
                c.setBuildingName(rs.getString("building_name"));
                comboDeleteCustomer.addItem(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "خطأ في تحميل العملاء: " + ex.getMessage());
        }
    }

    private void deleteCustomer() {
        Customer c = (Customer) comboDeleteCustomer.getSelectedItem();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "الرجاء اختيار عميل اولاً!", "تنبيه", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "هل انت متأكد من حذف العميل:\n" + c.getFullName() + "\n\n" +
            "سيتم حذف جميع قراءاته ايضاً!",
            "تأكيد الحذف", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Connection conn = Database.getInstance().getConnection();

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM readings WHERE customer_id = ?")) {
                ps.setInt(1, c.getCustomerId());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM customers WHERE customer_id = ?")) {
                ps.setInt(1, c.getCustomerId());
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "تم حذف العميل وقراءاته بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
            
            // <-- تحديث القائمة فوراً
            loadCustomersForDelete();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "خطأ في الحذف:\n" + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }
}

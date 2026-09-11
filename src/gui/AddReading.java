package gui;

import database.Database;
import models.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AddReading extends JFrame {

    private JComboBox<Customer> cmbCustomer;
    private JTextField txtPrev, txtCurrent, txtUnits, txtTotal, txtPrice;
    private JTextField txtPaid, txtRemaining;
    private JLabel lblStatus;
    private double unitPrice = 0;

    public AddReading(JFrame owner) {
        setTitle("اضافة قراءة جديدة");
        setSize(520, 750);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("اضافة قراءة جديدة", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 150, 243));
        lblTitle.setBounds(35, 15, 450, 40);
        panel.add(lblTitle);

        loadUnitPrice();

        JLabel lblCust = new JLabel("اختر العميل:");
        lblCust.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCust.setForeground(new Color(55, 71, 79));
        lblCust.setBounds(35, 70, 450, 25);
        panel.add(lblCust);

        cmbCustomer = new JComboBox<>();
        cmbCustomer.setBounds(35, 97, 450, 42);
        cmbCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCustomer.setBackground(new Color(250, 250, 250));
        panel.add(cmbCustomer);

        JLabel lblDate = new JLabel("التاريخ: " + LocalDate.now());
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDate.setForeground(new Color(100, 100, 100));
        lblDate.setBounds(35, 150, 450, 25);
        panel.add(lblDate);

        JLabel lblPrev = new JLabel("القراءة السابقة (اخر قراءة مسجلة):");
        lblPrev.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPrev.setForeground(new Color(55, 71, 79));
        lblPrev.setBounds(35, 185, 450, 25);
        panel.add(lblPrev);

        txtPrev = new JTextField("0");
        txtPrev.setBounds(35, 212, 450, 42);
        txtPrev.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPrev.setForeground(new Color(84, 110, 122));
        txtPrev.setBackground(new Color(250, 250, 250));
        txtPrev.setEditable(true);
        txtPrev.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 189, 189), 1),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtPrev);

        JLabel lblCurrent = new JLabel("القراءة الحالية:");
        lblCurrent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCurrent.setForeground(new Color(55, 71, 79));
        lblCurrent.setBounds(35, 265, 450, 25);
        panel.add(lblCurrent);

        txtCurrent = new JTextField();
        txtCurrent.setBounds(35, 292, 450, 42);
        txtCurrent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtCurrent.setForeground(new Color(33, 150, 243));
        txtCurrent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        txtCurrent.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculate();
            }
        });
        panel.add(txtCurrent);

        JLabel lblUnits = new JLabel("الاستهلاك (الفرق):");
        lblUnits.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUnits.setForeground(new Color(55, 71, 79));
        lblUnits.setBounds(35, 345, 450, 25);
        panel.add(lblUnits);

        txtUnits = new JTextField("0");
        txtUnits.setBounds(35, 372, 450, 42);
        txtUnits.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtUnits.setForeground(new Color(46, 125, 50));
        txtUnits.setBackground(new Color(232, 245, 233));
        txtUnits.setEditable(false);
        txtUnits.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(129, 199, 132), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtUnits);

        JLabel lblUnitPrice = new JLabel("سعر الوحدة:");
        lblUnitPrice.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUnitPrice.setForeground(new Color(46, 125, 50));
        lblUnitPrice.setBounds(275, 430, 210, 25);
        panel.add(lblUnitPrice);

        txtPrice = new JTextField(String.valueOf(unitPrice));
        txtPrice.setBounds(275, 457, 210, 42);
        txtPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPrice.setForeground(new Color(46, 125, 50));
        txtPrice.setBackground(new Color(232, 245, 233));
        txtPrice.setEditable(false);
        txtPrice.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(129, 199, 132), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtPrice);

        JLabel lblTotal = new JLabel("الاجمالي:");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(183, 28, 28));
        lblTotal.setBounds(35, 430, 210, 25);
        panel.add(lblTotal);

        txtTotal = new JTextField("0.00");
        txtTotal.setBounds(35, 457, 210, 48);
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        txtTotal.setForeground(new Color(183, 28, 28));
        txtTotal.setBackground(new Color(255, 235, 238));
        txtTotal.setEditable(false);
        txtTotal.setHorizontalAlignment(SwingConstants.CENTER);
        txtTotal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(239, 83, 80), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtTotal);

        // ==== المبلغ المسدد (جديد) ====
        JLabel lblPaid = new JLabel("المبلغ المسدد:");
        lblPaid.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPaid.setForeground(new Color(33, 71, 161));
        lblPaid.setBounds(35, 515, 210, 25);
        panel.add(lblPaid);

        txtPaid = new JTextField("0");
        txtPaid.setBounds(35, 542, 210, 42);
        txtPaid.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPaid.setForeground(new Color(33, 71, 161));
        txtPaid.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        txtPaid.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updatePayment();
            }
        });
        panel.add(txtPaid);

        // ==== المتبقي (جديد) ====
        JLabel lblRemaining = new JLabel("المتبقي:");
        lblRemaining.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRemaining.setForeground(new Color(220, 20, 60));
        lblRemaining.setBounds(275, 515, 210, 25);
        panel.add(lblRemaining);

        txtRemaining = new JTextField("0.00");
        txtRemaining.setBounds(275, 542, 210, 42);
        txtRemaining.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtRemaining.setForeground(new Color(220, 20, 60));
        txtRemaining.setBackground(new Color(255, 235, 238));
        txtRemaining.setEditable(false);
        txtRemaining.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        panel.add(txtRemaining);

        // ==== حالة السداد (جديد) ====
        lblStatus = new JLabel("الحالة: غير مسدد", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatus.setForeground(new Color(183, 28, 28));
        lblStatus.setBounds(35, 595, 450, 30);
        panel.add(lblStatus);

        JButton btnCalc = new JButton("احتساب");
        btnCalc.setBounds(35, 640, 220, 45);
        btnCalc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCalc.setBackground(new Color(255, 152, 0));
        btnCalc.setForeground(Color.WHITE);
        btnCalc.setFocusPainted(false);
        btnCalc.setBorderPainted(false);
        btnCalc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalc.addActionListener(e -> calculate());
        panel.add(btnCalc);

        JButton btnSave = new JButton("حفظ الفاتورة");
        btnSave.setBounds(275, 640, 210, 45);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(46, 125, 50));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> saveReading());
        panel.add(btnSave);

        add(panel);
        loadCustomers();
        setVisible(true);
    }

    private void loadUnitPrice() {
        try {
            Connection conn = Database.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT unit_price FROM settings LIMIT 1");
            if (rs.next()) unitPrice = rs.getDouble("unit_price");
        } catch (SQLException ex) {
            System.out.println("خطأ في تحميل السعر: " + ex.getMessage());
        }
    }

    private void loadCustomers() {
        try {
            Connection conn = Database.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customers ORDER BY full_name");

            cmbCustomer.removeAllItems();
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setFullName(rs.getString("full_name"));
                c.setPhone(rs.getString("phone"));
                c.setApartmentNumber(rs.getString("apartment_number"));
                c.setBuildingName(rs.getString("building_name"));
                cmbCustomer.addItem(c);
            }

            cmbCustomer.addActionListener(e -> loadPreviousReading());
            if (cmbCustomer.getItemCount() > 0) {
                loadPreviousReading();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "خطأ في تحميل العملاء: " + ex.getMessage());
        }
    }

    private void loadPreviousReading() {
        Customer c = (Customer) cmbCustomer.getSelectedItem();
        if (c == null) return;

        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "SELECT current_reading FROM readings WHERE customer_id = ? ORDER BY reading_date DESC, reading_id DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, c.getCustomerId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrev.setText(String.valueOf(rs.getInt("current_reading")));
            } else {
                txtPrev.setText("0");
            }
        } catch (SQLException ex) {
            txtPrev.setText("0");
        }
    }

    private void calculate() {
        try {
            int prev = Integer.parseInt(txtPrev.getText().trim().isEmpty() ? "0" : txtPrev.getText().trim());
            int curr = Integer.parseInt(txtCurrent.getText().trim().isEmpty() ? "0" : txtCurrent.getText().trim());

            if (curr < prev) {
                txtUnits.setText("0");
                txtTotal.setText("0.00");
                updatePayment();
                return;
            }

            int units = curr - prev;
            double total = units * unitPrice;

            txtUnits.setText(String.valueOf(units));
            txtTotal.setText(String.format("%.2f", total));
            updatePayment();

        } catch (NumberFormatException e) {
        }
    }

    // ==== تحديث المبلغ المسدد والمتبقي (جديد) ====
    private void updatePayment() {
        try {
            double total = Double.parseDouble(txtTotal.getText().trim().isEmpty() ? "0" : txtTotal.getText().trim());
            double paid = Double.parseDouble(txtPaid.getText().trim().isEmpty() ? "0" : txtPaid.getText().trim());
            double remaining = total - paid;
            if (remaining < 0) remaining = 0;

            txtRemaining.setText(String.format("%.2f", remaining));

            if (paid >= total && total > 0) {
                lblStatus.setText("الحالة: مسدد");
                lblStatus.setForeground(new Color(46, 125, 50));
            } else if (paid > 0) {
                lblStatus.setText("الحالة: دفع جزئي");
                lblStatus.setForeground(new Color(255, 152, 0));
            } else {
                lblStatus.setText("الحالة: غير مسدد");
                lblStatus.setForeground(new Color(183, 28, 28));
            }
        } catch (NumberFormatException e) {
            txtRemaining.setText("0.00");
        }
    }

    private void saveReading() {
        String currentText = txtCurrent.getText().trim();
        if (currentText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "الرجاء ادخال القراءة الحالية!");
            txtCurrent.requestFocus();
            return;
        }

        int curr;
        try {
            curr = Integer.parseInt(currentText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "القراءة الحالية يجب ان تكون رقم!");
            txtCurrent.requestFocus();
            return;
        }

        int prev;
        try {
            prev = Integer.parseInt(txtPrev.getText().trim().isEmpty() ? "0" : txtPrev.getText().trim());
        } catch (NumberFormatException e) {
            prev = 0;
        }

        if (curr < prev) {
            JOptionPane.showMessageDialog(this, "القراءة الحالية يجب ان تكون اكبر من السابقة!");
            return;
        }

        int units = curr - prev;
        double total = units * unitPrice;

        txtUnits.setText(String.valueOf(units));
        txtTotal.setText(String.format("%.2f", total));

        if (units <= 0) {
            JOptionPane.showMessageDialog(this, "الاستهلاك يجب ان يكون اكبر من صفر!");
            return;
        }

        Customer c = (Customer) cmbCustomer.getSelectedItem();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "الرجاء اختيار عميل اولاً!");
            return;
        }

        // منع القراءة قبل 10 ايام
        try {
            Connection conn = Database.getInstance().getConnection();
            String checkSql = "SELECT reading_date FROM readings WHERE customer_id = ? ORDER BY reading_date DESC, reading_id DESC LIMIT 1";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, c.getCustomerId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                Date lastDate = rs.getDate("reading_date");
                if (lastDate != null) {
                    LocalDate last = lastDate.toLocalDate();
                    LocalDate today = LocalDate.now();
                    long daysBetween = ChronoUnit.DAYS.between(last, today);

                    if (daysBetween < 10) {
                        JOptionPane.showMessageDialog(this,
                            "لا يمكن ادخال قراءة جديدة لهذا العميل!\n\n" +
                            "آخر قراءة كانت بتاريخ: " + last + "\n" +
                            "يجب الانتظار " + (10 - daysBetween) + " يوم/ايام اخرى.",
                            "منع الادخال", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "خطأ في التحقق من التاريخ: " + ex.getMessage());
            return;
        }

        // حساب المسدد والمتبقي
        double paid;
        try {
            paid = Double.parseDouble(txtPaid.getText().trim().isEmpty() ? "0" : txtPaid.getText().trim());
        } catch (NumberFormatException e) {
            paid = 0;
        }
        if (paid < 0) paid = 0;
        if (paid > total) paid = total;

        double remaining = total - paid;
        String status;
        if (remaining <= 0.01) {
            status = "مسدد";
            remaining = 0;
        } else if (paid > 0) {
            status = "دفع جزئي";
        } else {
            status = "غير مسدد";
        }

        // الحفظ مع المسدد والمتبقي والحالة
        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "INSERT INTO readings (customer_id, reading_date, previous_reading, current_reading, consumed_units, unit_price, total_amount, paid_amount, remaining_amount, `status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, c.getCustomerId());
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setInt(3, prev);
            stmt.setInt(4, curr);
            stmt.setInt(5, units);
            stmt.setDouble(6, unitPrice);
            stmt.setDouble(7, total);
            stmt.setDouble(8, paid);
            stmt.setDouble(9, remaining);
            stmt.setString(10, status);
            stmt.executeUpdate();

            // عرض الفاتورة
            showInvoice(c, prev, curr, units, total, paid, remaining, status);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "خطأ في قاعدة البيانات: " + ex.getMessage());
        }
    }

    private void showInvoice(Customer c, int prev, int curr, int units, double total, double paid, double remaining, String status) {
        StringBuilder inv = new StringBuilder();
        inv.append("========================================\n");
        inv.append("         فاتورة عداد المياه          \n");
        inv.append("========================================\n\n");
        inv.append("العميل: ").append(c.getFullName()).append("\n");
        inv.append("التاريخ: ").append(LocalDate.now()).append("\n");
        inv.append("----------------------------------------\n");
        inv.append("القراءة السابقة: ").append(prev).append("\n");
        inv.append("القراءة الحالية: ").append(curr).append("\n");
        inv.append("الاستهلاك: ").append(units).append(" وحدة\n");
        inv.append("سعر الوحدة: ").append(String.format("%.2f", unitPrice)).append(" ريال\n");
        inv.append("----------------------------------------\n");
        inv.append("الاجمالي:  ").append(String.format("%.2f", total)).append(" ريال\n");
        inv.append("المسدد:    ").append(String.format("%.2f", paid)).append(" ريال\n");
        inv.append("المتبقي:   ").append(String.format("%.2f", remaining)).append(" ريال\n");
        inv.append("الحالة:    ").append(status).append("\n");
        inv.append("========================================\n");

        JTextArea area = new JTextArea(inv.toString());
        area.setFont(new Font("Courier New", Font.BOLD, 14));
        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 350));

        Object[] options = {"طباعة", "حسناً"};
        int result = JOptionPane.showOptionDialog(this, scroll, "فاتورة",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

        if (result == 0) {
            try {
                area.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "خطأ في الطباعة: " + ex.getMessage());
            }
        }
        dispose();
    }
}

package gui;

import database.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {

    private String userName;
    private JLabel lblCustomers, lblTotalBills, lblTotalUnits, lblAvgUnit;
    private DefaultTableModel recentModel;

    public Dashboard(String userName) {
        this.userName = userName;

        setTitle("🚰 لوحة التحكم - " + userName);
        setSize(1150, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bg = new JPanel(new BorderLayout());
        bg.setBackground(new Color(236, 239, 241));

        // ===== Sidebar =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(21, 101, 192));
        sidebar.setPreferredSize(new Dimension(270, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 15, 20, 15));

        JLabel lblLogo = new JLabel("🚰", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 55));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);

        JLabel lblSys = new JLabel("عدادات المياه", SwingConstants.CENTER);
        lblSys.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSys.setForeground(Color.WHITE);
        lblSys.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblSys);

        JLabel lblName = new JLabel(userName, SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblName.setForeground(new Color(187, 222, 251));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblName);
        sidebar.add(Box.createVerticalStrut(35));

        // ⬇️⬇️⬇️ الأزرار - action واضح ومضمون
        addSidebarButton(sidebar, "🏠 الرئيسية", "home");
        addSidebarButton(sidebar, "👤 إضافة عميل", "customer");
        addSidebarButton(sidebar, "📝 إضافة قراءة", "reading");
        addSidebarButton(sidebar, "📋 قائمة العملاء", "list");
        addSidebarButton(sidebar, "📊 التقارير", "report");
        addSidebarButton(sidebar, "⚙️ الإعدادات", "settings");
        addSidebarButton(sidebar, "🚪 تسجيل خروج", "logout");

        bg.add(sidebar, BorderLayout.WEST);

        // ===== Content =====
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblHeader = new JLabel("🏠 لوحة التحكم");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeader.setForeground(new Color(13, 71, 161));
        content.add(lblHeader, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 0, 20));
        center.setOpaque(false);

        // Stats
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        cardsPanel.setOpaque(false);

        lblCustomers = new JLabel("0", SwingConstants.CENTER);
        lblTotalBills = new JLabel("0.00", SwingConstants.CENTER);
        lblTotalUnits = new JLabel("0", SwingConstants.CENTER);
        lblAvgUnit = new JLabel("0.00", SwingConstants.CENTER);

        cardsPanel.add(createCard("👥 العملاء", lblCustomers, new Color(33, 150, 243)));
        cardsPanel.add(createCard("💵 الفواتير", lblTotalBills, new Color(46, 125, 50)));
        cardsPanel.add(createCard("⚡ الاستهلاك", lblTotalUnits, new Color(239, 108, 0)));
        cardsPanel.add(createCard("📊 متوسط الوحدة", lblAvgUnit, new Color(123, 31, 162)));

        center.add(cardsPanel);

        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        JLabel lblRecent = new JLabel("📝 آخر القراءات");
        lblRecent.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRecent.setForeground(new Color(13, 71, 161));
        lblRecent.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        tablePanel.add(lblRecent, BorderLayout.NORTH);

        String[] cols = {"#", "العميل", "الشقة", "السابقة", "الحالية", "الوحدات", "الإجمالي", "التاريخ"};
        recentModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(recentModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(187, 222, 251), 1));
        tablePanel.add(scroll, BorderLayout.CENTER);

        center.add(tablePanel);
        content.add(center, BorderLayout.CENTER);
        bg.add(content, BorderLayout.CENTER);

        setContentPane(bg);
        setVisible(true);

        loadStats();
        loadRecentReadings();
    }

    private void addSidebarButton(JPanel sidebar, String text, String action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(new Color(227, 242, 253));
        btn.setBackground(new Color(21, 101, 192));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(240, 46));
        btn.setPreferredSize(new Dimension(240, 46));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.RIGHT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(25, 118, 210));
                btn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(21, 101, 192));
                btn.setForeground(new Color(227, 242, 253));
            }
        });

        // ⬇️⬇️⬇️ هنا التفعيل الصحيح
        btn.addActionListener(e -> {
            System.out.println("تم الضغط على: " + action); // للتصحيح

            if (action.equals("customer")) {
                new AddCustomer(this);
            } else if (action.equals("reading")) {
                System.out.println("=== فتح نافذة القراءة ===");
                SwingUtilities.invokeLater(() -> {
                    AddReading ar = new AddReading(Dashboard.this);
                    ar.setVisible(true);
                });

                new AddReading(this);
            } else if (action.equals("list")) {
                new CustomerList(this);
            } else if (action.equals("report")) {
                new Reports(this);
            } else if (action.equals("settings")) {
                new SettingsFrame(this);
            } else if (action.equals("logout")) {
                dispose();
                new Login();
            }
        });

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(6));
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, accent),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(100, 116, 139));
        card.add(lblTitle, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accent);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setForeground(new Color(30, 41, 59));
        table.setBackground(Color.WHITE);
        table.setRowHeight(40);
        table.setGridColor(new Color(227, 242, 253));
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(227, 242, 253));
        table.getTableHeader().setForeground(new Color(13, 71, 161));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
    }

    private void loadStats() {
        try {
            Connection conn = Database.getInstance().getConnection();
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM customers");
            rs1.next();
            lblCustomers.setText(rs1.getString(1));

            ResultSet rs2 = conn.createStatement().executeQuery("SELECT COALESCE(SUM(total_amount),0) FROM readings");
            rs2.next();
            lblTotalBills.setText(String.format("%.2f", rs2.getDouble(1)));

            ResultSet rs3 = conn.createStatement().executeQuery("SELECT COALESCE(SUM(consumed_units),0) FROM readings");
            rs3.next();
            lblTotalUnits.setText(rs3.getString(1));

            ResultSet rs4 = conn.createStatement().executeQuery("SELECT COALESCE(AVG(unit_price),0) FROM readings");
            rs4.next();
            lblAvgUnit.setText(String.format("%.2f", rs4.getDouble(1)));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadRecentReadings() {
        recentModel.setRowCount(0);
        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "SELECT r.*, c.full_name, c.apartment_number FROM readings r "
                    + "JOIN customers c ON r.customer_id = c.customer_id ORDER BY r.reading_id DESC LIMIT 10";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            int i = 1;
            while (rs.next()) {
                recentModel.addRow(new Object[]{
                    i++, rs.getString("full_name"), rs.getString("apartment_number"),
                    rs.getInt("previous_reading"), rs.getInt("current_reading"),
                    rs.getInt("consumed_units"), String.format("%.2f", rs.getDouble("total_amount")),
                    rs.getDate("reading_date")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

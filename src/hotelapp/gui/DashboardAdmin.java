package hotelapp.gui;

import hotelapp.model.*;
import hotelapp.service.DataManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardAdmin extends JFrame {

    // ===== COLOR PALETTE (sama dengan DashboardTamu) =====
    private final Color c1 = Color.decode("#3E3232"); // hover
    private final Color c2 = Color.decode("#503C3C"); // button
    private final Color c3 = Color.decode("#7E6363"); // border
    private final Color bg = Color.decode("#EEE4E1"); // background

    private JPanel panelTamu;
    private JPanel panelBooking;
    private JPanel panelActive;

    public DashboardAdmin() {
        setTitle("Hotel Sariz - Dashboard Admin");
        setSize(1100, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel root = new JPanel(null);
        root.setBackground(bg);

        JLabel title = new JLabel("Dashboard Admin - HOTEL SARIZ");
        title.setBounds(30, 5, 600, 35);
        title.setFont(new Font("Poppins", Font.BOLD,22));
        title.setForeground(c2);
        root.add(title);

        // ===== TITLE PANEL =====
        root.add(sectionTitle("DATA TAMU", 30));
        root.add(sectionTitle("DATA BOOKING", 380));
        root.add(sectionTitle("TAMU SEDANG CHECK-IN", 730));

        panelTamu    = createListPanel();
        panelBooking = createListPanel();
        panelActive  = createListPanel();

        root.add(scroll(panelTamu, 30));
        root.add(scroll(panelBooking, 380));
        root.add(scroll(panelActive, 730));

        // ===== BUTTONS =====
        RoundedButton addBtn     = new RoundedButton("TAMBAH TAMU");
        RoundedButton delBtn     = new RoundedButton("HAPUS");
        RoundedButton manageBtn  = new RoundedButton("MANAGE KAMAR");
        RoundedButton saveBtn    = new RoundedButton("SAVE");
        RoundedButton refreshBtn = new RoundedButton("REFRESH");
        RoundedButton logoutBtn  = new RoundedButton("LOGOUT");

        RoundedButton[] btns = { addBtn, delBtn, manageBtn, saveBtn, refreshBtn, logoutBtn };

        int x = 30;
        for (RoundedButton b : btns) {
            b.setBounds(x, 450, 160, 35);
            b.setBackground(c2);
            b.setForeground(Color.WHITE);
            root.add(b);
            b.addMouseListener(new DashboardTamu.HoverEffect(b, c1));
            x += 170;
        }

        // ===== ACTION =====
        addBtn.addActionListener(e -> tambahTamu());
        delBtn.addActionListener(e -> hapusTamu());
        manageBtn.addActionListener(e -> new ManageKamarForm());
        saveBtn.addActionListener(e -> DataManager.saveAll());
        refreshBtn.addActionListener(e -> refreshAll());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        add(root);
        refreshAll();
    }

    // ================= PANEL & CARD =================

    private JLabel sectionTitle(String text, int x) {
        JLabel l = new JLabel(text);
        l.setBounds(x, 45, 300, 20);
        l.setFont(new Font("Poppins", Font.BOLD,14));
        l.setForeground(c2);
        return l;
    }

    private JPanel createListPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bg);
        return p;
    }

    private JScrollPane scroll(JPanel panel, int x) {
        JScrollPane sp = new JScrollPane(panel);
        sp.setBounds(x, 70, 330, 360);
        sp.setBorder(null);
        return sp;
    }

    private JPanel adminCard(String header, String status, Color statusColor, String... lines) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(c3, 2, true),
                new EmptyBorder(10,10,10,10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHeader = new JLabel(header);
        lblHeader.setFont(new Font("Poppins", Font.BOLD,14));
        lblHeader.setForeground(c2);

        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Poppins", Font.BOLD,12));
        lblStatus.setForeground(statusColor);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lblHeader, BorderLayout.WEST);
        top.add(lblStatus, BorderLayout.EAST);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        for (String s : lines) {
            JLabel l = new JLabel(s);
            l.setFont(new Font("Poppins", Font.PLAIN,12));
            l.setForeground(darkRed());
            body.add(l);
        }

        card.add(top, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private Color darkRed() {
        return new Color(62,50,50);
    }

    // ================= DATA =================

    private void refreshAll() {
        panelTamu.removeAll();
        panelBooking.removeAll();
        panelActive.removeAll();

        // DATA TAMU
        for (Tamu t : DataManager.tamuList) {
            panelTamu.add(adminCard(
                    "ID : " + t.getId(),
                    "",
                    c3,
                    "Nama : " + t.getName(),
                    "Username : " + t.getUsername()
            ));
            panelTamu.add(Box.createVerticalStrut(8));
        }

        // DATA BOOKING
        for (Booking b : DataManager.bookingList) {
            String status;
            Color color;

            if (b.isCheckedOut()) {
                status = "[CHECKED-OUT]";
                color = Color.RED;
            } else if (b.isCheckedIn()) {
                status = "[CHECKED-IN]";
                color = new Color(34,139,34);
            } else {
                status = "[PENDING]";
                color = c3;
            }

            panelBooking.add(adminCard(
                    "ID Kamar : " + b.getKamar().getNomor(),
                    status,
                    color,
                    "Nama : " + b.getTamu().getName(),
                    b.getKamar().getTipe() + " | " +
                            b.getCheckIn() + " - " + b.getCheckOut(),
                    "Total : Rp " + String.format("%.0f", b.getTotalHarga())
            ));
            panelBooking.add(Box.createVerticalStrut(8));
        }

        // TAMU CHECK-IN
        for (Booking b : DataManager.bookingList) {
            if (b.isCheckedIn() && !b.isCheckedOut()) {
                panelActive.add(adminCard(
                        b.getTamu().getName(),
                        "[CHECKED-IN]",
                        new Color(34,139,34),
                        "Kamar " + b.getKamar().getNomor(),
                        b.getKamar().getTipe()
                ));
                panelActive.add(Box.createVerticalStrut(8));
            }
        }

        revalidate();
        repaint();
    }

    // ================= ACTION =================

    private void tambahTamu() {
        String nama = JOptionPane.showInputDialog(this, "Nama Tamu:");
        String user = JOptionPane.showInputDialog(this, "Username:");
        String pass = JOptionPane.showInputDialog(this, "Password:");

        if (nama != null && user != null && pass != null) {
            DataManager.tamuList.add(
                    new Tamu(DataManager.nextTamuId(), user, pass, nama)
            );
            refreshAll();
        }
    }

    private void hapusTamu() {
        if (!DataManager.tamuList.isEmpty()) {
            DataManager.tamuList.remove(DataManager.tamuList.size() - 1);
            refreshAll();
        }
    }

    // ================= BUTTON STYLE =================

    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(new Font("Poppins", Font.BOLD,14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g);
        }
    }

    class HoverEffect extends MouseAdapter {
        JButton btn;
        Color base;
        HoverEffect(JButton b, Color c) {
            btn = b;
            base = c;
        }
        public void mouseEntered(MouseEvent e) {
            btn.setBackground(base.darker());
        }
        public void mouseExited(MouseEvent e) {
            btn.setBackground(base);
        }
    }
}

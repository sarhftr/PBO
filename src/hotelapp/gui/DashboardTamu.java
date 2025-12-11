package hotelapp.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import hotelapp.model.*;
import hotelapp.service.DataManager;

public class DashboardTamu extends JFrame {

    private Tamu tamu;

    private final Color navy = new Color(39,76,119);
    private final Color cream = new Color(245,238,220);
    private final Color darkRed = new Color(62,50,50);
    private final Color brown = new Color(126,99,99);

    private final Font titleFont = new Font("Poppins", Font.BOLD, 20);
    private final Font normalFont = new Font("Poppins", Font.PLAIN, 13);

    // footer buttons
    private RoundedButton bookBtn, payBtn, checkInBtn, checkOutBtn, refreshBtn;
    private RoundedButton logoutBtnHeader;

    public DashboardTamu(Tamu tamu){
        this.tamu = tamu;
        setTitle("Hotel Sariz - Dashboard Tamu");
        setSize(1000,680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        init();
        setVisible(true);
    }

    private void init(){
        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(darkRed);
        header.setBorder(BorderFactory.createEmptyBorder(12,18,12,18));
        header.setPreferredSize(new Dimension(getWidth(),64));

        JLabel lblTitle = new JLabel("Dashboard Tamu - " + tamu.getName());
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(titleFont);
        header.add(lblTitle, BorderLayout.WEST);

        logoutBtnHeader = new RoundedButton("Logout");
        logoutBtnHeader.setPreferredSize(new Dimension(100,34));
        logoutBtnHeader.setBackground(navy);
        logoutBtnHeader.setForeground(Color.WHITE);
        logoutBtnHeader.setFont(new Font("Poppins", Font.PLAIN, 12));
        logoutBtnHeader.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        logoutBtnHeader.addMouseListener(new HoverEffect(logoutBtnHeader, navy));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        right.setOpaque(false);
        right.add(logoutBtnHeader);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // BODY
        JPanel body = new JPanel();
        body.setBackground(cream);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(18,24,18,24));

        // Info Card
        JPanel infoCard = new JPanel(new BorderLayout());
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(compoundThinRoundedBorder());
        infoCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        infoCard.setPreferredSize(new Dimension(800,80));
        JLabel info = new JLabel("<html><b>Selamat Datang, " + tamu.getName() + "</b><br><span style='font-size:12px'>Status: Tamu Terdaftar</span></html>");
        info.setFont(normalFont);
        info.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        infoCard.add(info, BorderLayout.WEST);
        body.add(infoCard);
        body.add(Box.createVerticalStrut(20));

        // Cards panel
        JPanel cardsRow = new JPanel();
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,18,0,18);

        JPanel cardBooking = createMenuCard("Riwayat Booking", "Lihat pesanan kamar Anda", e -> openBookingPage());
        JPanel cardKamar = createMenuCard("Daftar Kamar", "Lihat semua kamar hotel", e -> openDaftarKamarPage());

        gbc.gridx = 0; cardsRow.add(cardBooking, gbc);
        gbc.gridx = 1; cardsRow.add(cardKamar, gbc);

        body.add(cardsRow);
        body.add(Box.createVerticalGlue());

        JScrollPane centerWrapper = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        centerWrapper.setBorder(null);
        add(centerWrapper, BorderLayout.CENTER);

        // FOOTER BUTTON BAR
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER,14,12));
        footer.setBackground(cream);
        footer.setBorder(BorderFactory.createEmptyBorder(8,8,12,8));

        bookBtn = new RoundedButton("BOOKING");
        payBtn = new RoundedButton("BAYAR");
        checkInBtn = new RoundedButton("CHECK-IN");
        checkOutBtn = new RoundedButton("CHECK-OUT");
        refreshBtn = new RoundedButton("REFRESH");

        RoundedButton[] arr = {bookBtn, payBtn, checkInBtn, checkOutBtn, refreshBtn};
        for(RoundedButton b : arr){
            b.setPreferredSize(new Dimension(140,42));
            b.setBackground(navy);
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Poppins", Font.BOLD,13));
            b.addMouseListener(new HoverEffect(b, navy));
            footer.add(b);
        }

        bookBtn.addActionListener(e -> new BookingForm(tamu, this));
        payBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Buka Riwayat Booking untuk memilih transaksi (klik Riwayat Booking).");
            openBookingPage();
        });
        checkInBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Buka Riwayat Booking untuk melakukan Check-In.");
            openBookingPage();
        });
        checkOutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Buka Riwayat Booking untuk melakukan Check-Out.");
            openBookingPage();
        });
        refreshBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Dashboard diperbarui."));

        add(footer, BorderLayout.SOUTH);
    }

    private Border compoundThinRoundedBorder(){
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220,220,220),1,true),
            BorderFactory.createEmptyBorder(10,12,10,12)
        );
    }

    private JPanel createMenuCard(String title, String subtitle, ActionListener onClick){
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(420,140));
        card.setBackground(Color.WHITE);
        card.setBorder(compoundThinRoundedBorder());

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Poppins", Font.BOLD,16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(16,12,0,12));

        JLabel lblSub = new JLabel("<html><span style='font-size:12px;color:#333;'>" + subtitle + "</span></html>");
        lblSub.setFont(new Font("Poppins", Font.PLAIN,13));
        lblSub.setBorder(BorderFactory.createEmptyBorder(6,12,12,12));

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(lblTitle, BorderLayout.NORTH);
        left.add(lblSub, BorderLayout.CENTER);
        card.add(left, BorderLayout.CENTER);

        JLabel lblMore = new JLabel("\u25B6");
        lblMore.setFont(new Font("Poppins", Font.BOLD,20));
        lblMore.setBorder(BorderFactory.createEmptyBorder(10,10,10,16));
        card.add(lblMore, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter(){
            @Override public void mouseEntered(MouseEvent e){
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(60,110,170),2,true),
                    BorderFactory.createEmptyBorder(10,12,10,12)
                ));
            }
            @Override public void mouseExited(MouseEvent e){
                card.setBorder(compoundThinRoundedBorder());
            }
            @Override public void mouseClicked(MouseEvent e){
                onClick.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED,"click"));
            }
        });

        return card;
    }

    private void openBookingPage(){
        SwingUtilities.invokeLater(() -> new BookingPage(this, tamu));
    }

    private void openDaftarKamarPage(){
        SwingUtilities.invokeLater(() -> new DaftarKamarPage(this, tamu));
    }

    public void refresh(){ }

    // ===== RoundedButton & Hover =====
    class RoundedButton extends JButton {
        public RoundedButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    class HoverEffect extends MouseAdapter{
        private JButton btn;
        private Color base;
        HoverEffect(JButton btn, Color base){ this.btn = btn; this.base = base; }
        @Override public void mouseEntered(MouseEvent e){ btn.setBackground(base.darker()); }
        @Override public void mouseExited(MouseEvent e){ btn.setBackground(base); }
    }
}

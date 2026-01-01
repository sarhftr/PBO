package hotelapp.gui;

import hotelapp.model.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class DashboardTamu extends JFrame {

    private Tamu tamu;

    private final Color c1 = Color.decode("#3E3232"); // hover
    private final Color c2 = Color.decode("#503C3C"); // button
    private final Color c3 = Color.decode("#7E6363");
    private final Color c4 = Color.decode("#A87C7C");
    private final Color bg = Color.decode("#EEE4E1");

    private final Font titleFont = new Font("Poppins", Font.BOLD, 20);
    private final Font normalFont = new Font("Poppins", Font.PLAIN, 13);

    private RoundedButton bookBtn, payBtn, checkInBtn, checkOutBtn, refreshBtn;
    private RoundedButton logoutBtnHeader;

    public DashboardTamu(Tamu tamu){
        this.tamu = tamu;
        setTitle("Hotel Sariz - Dashboard Tamu");
        setSize(1000,680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } 
        catch(Exception e) { e.printStackTrace(); }

        init();
        setVisible(true);
    }

    private void init(){
        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(c1);
        header.setBorder(BorderFactory.createEmptyBorder(12,18,12,18));
        header.setPreferredSize(new Dimension(getWidth(),64));

        JLabel lblTitle = new JLabel("Dashboard Tamu - " + tamu.getName());
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(titleFont);
        header.add(lblTitle, BorderLayout.WEST);

        logoutBtnHeader = new RoundedButton("Logout");
        logoutBtnHeader.setPreferredSize(new Dimension(100,34));
        logoutBtnHeader.setBackground(c2);
        logoutBtnHeader.setForeground(Color.WHITE);
        logoutBtnHeader.addActionListener(e -> {
            dispose();
            new LoginForm(); // pastikan LoginForm pakai poppins juga
        });
        logoutBtnHeader.addMouseListener(new HoverEffect(logoutBtnHeader, c1));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        right.setOpaque(false);
        right.add(logoutBtnHeader);
        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // BODY
        JPanel body = new JPanel();
        body.setBackground(bg);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(18,24,18,24));

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

        // FOOTER
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER,14,12));
        footer.setBackground(bg);
        footer.setBorder(BorderFactory.createEmptyBorder(8,8,12,8));

        bookBtn = new RoundedButton("BOOKING");
        payBtn = new RoundedButton("BAYAR");
        checkInBtn = new RoundedButton("CHECK-IN");
        checkOutBtn = new RoundedButton("CHECK-OUT");
        refreshBtn = new RoundedButton("REFRESH");

        RoundedButton[] arr = {bookBtn, payBtn, checkInBtn, checkOutBtn, refreshBtn};
        for(RoundedButton b : arr){
            b.setPreferredSize(new Dimension(160,35));
            b.setBackground(c2);
            b.setForeground(Color.WHITE);
            b.addMouseListener(new HoverEffect(b, c1));
            footer.add(b);
        }

        bookBtn.addActionListener(e -> new BookingForm(tamu, this));
        payBtn.addActionListener(e -> { JOptionPane.showMessageDialog(this, "Buka Riwayat Booking untuk memilih transaksi."); openBookingPage(); });
        checkInBtn.addActionListener(e -> { JOptionPane.showMessageDialog(this, "Buka Riwayat Booking untuk check-in."); openBookingPage(); });
        checkOutBtn.addActionListener(e -> { JOptionPane.showMessageDialog(this, "Buka Riwayat Booking untuk check-out."); openBookingPage(); });
        refreshBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Dashboard diperbarui."));

        add(footer, BorderLayout.SOUTH);
    }

    private Border compoundThinRoundedBorder(){
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(c3,1,true),
            BorderFactory.createEmptyBorder(10,12,10,12)
        );
    }

    private JPanel createMenuCard(String title, String subtitle, ActionListener onClick){
        JPanel card = new JPanel(new BorderLayout());
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
                    BorderFactory.createLineBorder(c2,2,true),
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

    private void openBookingPage(){ SwingUtilities.invokeLater(() -> new BookingPage(this, tamu)); }
    private void openDaftarKamarPage(){ SwingUtilities.invokeLater(() -> new DaftarKamarPage(this, tamu)); }

    public void refresh(){ }

    // ===== RoundedButton =====
    class RoundedButton extends JButton {
        private int radius = 25;
        public RoundedButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Poppins", Font.BOLD,13));
        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? getBackground().darker() : getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);
            FontMetrics fm = g2.getFontMetrics();
            String text = getText();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }

    // ===== HoverEffect static =====
    public static class HoverEffect extends MouseAdapter{
        private JButton btn; private Color base;
        public HoverEffect(JButton btn, Color base){ this.btn = btn; this.base = base; }
        @Override public void mouseEntered(MouseEvent e){ btn.setBackground(base); }
        @Override public void mouseExited(MouseEvent e){ btn.setBackground(base.darker()); }
    }
}

package hotelapp.gui;

import hotelapp.model.*;
import hotelapp.service.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class BookingPage extends JFrame {

    private DashboardTamu parent;
    private Tamu tamu;
    private JPanel bookingCardsPanel;

    // ===== samakan palette dengan DashboardTamu =====
    private final Color hoverColor = Color.decode("#3E3232"); // c1
    private final Color btnColor = Color.decode("#503C3C");   // c2
    private final Color borderColor = Color.decode("#7E6363"); // c3
    private final Color bgColor = Color.decode("#EEE4E1");    // bg
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public BookingPage(DashboardTamu parent, Tamu tamu){
        this.parent = parent;
        this.tamu = tamu;
        setTitle("Riwayat Booking - " + tamu.getName());
        setSize(820, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        refresh();
        setVisible(true);
    }

    private void init(){
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(btnColor);
        header.setPreferredSize(new Dimension(getWidth(), 56));
        JLabel title = new JLabel("Riwayat Booking");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Poppins", Font.BOLD, 16));
        header.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // BODY
        JPanel body = new JPanel();
        body.setBackground(bgColor);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        bookingCardsPanel = new JPanel();
        bookingCardsPanel.setLayout(new BoxLayout(bookingCardsPanel, BoxLayout.Y_AXIS));
        bookingCardsPanel.setBackground(bgColor);

        JScrollPane sp = new JScrollPane(bookingCardsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(780, 440));
        sp.setBorder(BorderFactory.createEmptyBorder());
        body.add(sp);

        add(body, BorderLayout.CENTER);

        // FOOTER BUTTONS
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        footer.setBackground(bgColor);

        RoundedButton btnPay = new RoundedButton("BAYAR");
        RoundedButton btnCheckIn = new RoundedButton("CHECK-IN");
        RoundedButton btnCheckOut = new RoundedButton("CHECK-OUT");
        RoundedButton btnBack = new RoundedButton("KEMBALI");

        RoundedButton[] arr = {btnPay, btnCheckIn, btnCheckOut, btnBack};
        for(RoundedButton b : arr){
            b.setPreferredSize(new Dimension(120,38));
            b.setBackground(btnColor);
            b.setForeground(Color.WHITE);
            b.addMouseListener(new DashboardTamu.HoverEffect(b, hoverColor));
            footer.add(b);
        }

        add(footer, BorderLayout.SOUTH);

        // BUTTON ACTIONS
        btnPay.addActionListener(e -> {
            Booking sel = getSelectedBooking();
            if(sel == null){ JOptionPane.showMessageDialog(this, "Pilih booking dulu."); return; }
            if(sel.isCheckedOut()){
                JOptionPane.showMessageDialog(this, "Tidak bisa melakukan pembayaran pada pemesanan yang sudah dicheckout.");
                return;
            }
            new PaymentForm(sel, parent);
            refresh();
            parent.refresh();
        });

        btnCheckIn.addActionListener(e -> {
            Booking sel = getSelectedBooking();
            if(sel == null){ JOptionPane.showMessageDialog(this, "Pilih booking dulu."); return; }
            if(sel.isCheckedOut()){
                JOptionPane.showMessageDialog(this, "Tidak bisa melakukan check-in pada pemesanan yang sudah dicheckout.");
                return;
            }
            if(!sel.isPaid()){ JOptionPane.showMessageDialog(this, "Booking belum dibayar, tidak bisa check-in."); return; }
            sel.doCheckIn();
            DataManager.saveAll();
            refresh();
            parent.refresh();
        });

        btnCheckOut.addActionListener(e -> {
            Booking sel = getSelectedBooking();
            if(sel == null){ JOptionPane.showMessageDialog(this, "Pilih booking dulu."); return; }
            if(sel.isCheckedOut()){
                JOptionPane.showMessageDialog(this, "Booking sudah dicheckout."); 
                return;
            }
            if(!sel.isPaid()){ JOptionPane.showMessageDialog(this, "Booking belum dibayar, tidak bisa check-out."); return; }
            sel.doCheckOut();
            sel.getKamar().setTersedia(true);
            DataManager.saveAll();
            refresh();
            parent.refresh();
        });

        btnBack.addActionListener(e -> dispose());
    }

    private Booking getSelectedBooking(){
        for(Component c : bookingCardsPanel.getComponents()){
            if(c instanceof JPanel){
                JPanel panel = (JPanel) c;
                Object bookingObj = panel.getClientProperty("bookingObj");
                Object sel = panel.getClientProperty("selected");
                if(bookingObj instanceof Booking && sel instanceof Boolean && (Boolean)sel){
                    return (Booking) bookingObj;
                }
            }
        }
        return null;
    }

    public void refresh(){
        bookingCardsPanel.removeAll();
        List<Booking> bookings = DataManager.bookingList;
        for(Booking b : bookings){
            if(b.getTamu().getUsername().equals(tamu.getUsername())){
                JPanel card = bookingCard(b);
                bookingCardsPanel.add(card);
                bookingCardsPanel.add(Box.createVerticalStrut(8));
            }
        }
        if(bookingCardsPanel.getComponentCount() == 0){
            JLabel none = new JLabel("Belum ada booking.");
            none.setFont(new Font("Poppins", Font.PLAIN, 14));
            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
            wrap.setBackground(bgColor);
            wrap.add(none);
            bookingCardsPanel.add(wrap);
        }
        bookingCardsPanel.revalidate();
        bookingCardsPanel.repaint();
    }

    private JPanel bookingCard(Booking b){
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(borderColor,1,true), BorderFactory.createEmptyBorder(10,10,10,10)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        String header = b.getId();
        String status;
        Color statusColor;
        if(b.isCheckedOut()){
            status = "[CHECKED-OUT]";
            statusColor = Color.RED;
        } else if(b.isCheckedIn()){
            status = "[CHECKED-IN]";
            statusColor = new Color(34,139,34);
        } else {
            status = "[PENDING]";
            statusColor = btnColor;
        }

        JLabel lblHeader = new JLabel("<html><b>" + header + "</b></html>");
        lblHeader.setFont(new Font("Poppins", Font.BOLD, 14));

        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Poppins", Font.BOLD, 12));
        lblStatus.setForeground(statusColor);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lblHeader, BorderLayout.WEST);
        top.add(lblStatus, BorderLayout.EAST);

        String tipe = "Tipe Kamar: " + b.getKamar().getTipe() + " (" + b.getKamar().getNomor() + ")";
        String checkin = "Check-in: " + b.getCheckIn().format(dtf);
        String checkout = "Check-out: " + b.getCheckOut().format(dtf);
        String total;
        if(b.isPaid()){
            total = "Rp " + String.format("%.0f", b.getTotalHarga()) + " | Silahkan Check-In";
        } else {
            total = "Belum Melakukan Pembayaran";
        }
        if(b.isCheckedOut()){
            total = "Rp " + String.format("%.0f", b.getTotalHarga()) + " | Booking selesai";
        }

        JLabel lblTipe = new JLabel(tipe);
        JLabel lblCI = new JLabel(checkin);
        JLabel lblCO = new JLabel(checkout);
        JLabel lblTotal = new JLabel(total);

        lblTipe.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblCI.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblCO.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblTotal.setFont(new Font("Poppins", Font.PLAIN, 13));

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);
        details.add(lblTipe);
        details.add(lblCI);
        details.add(lblCO);
        details.add(lblTotal);

        card.add(top, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);

        card.putClientProperty("bookingObj", b);
        card.putClientProperty("selected", false);

        card.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){
                for(Component c : bookingCardsPanel.getComponents()){
                    if(c instanceof JPanel){
                        JPanel panel = (JPanel) c;
                        Object obj = panel.getClientProperty("bookingObj");
                        if(obj != null){
                            panel.putClientProperty("selected", false);
                            panel.setBackground(Color.WHITE);
                        }
                    }
                }
                card.putClientProperty("selected", true);
                card.setBackground(new Color(230,245,255));
            }
        });

        return card;
    }

    // ===== RoundedButton =====
    class RoundedButton extends JButton {
        private int radius = 25;
        public RoundedButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(new Font("Poppins", Font.BOLD, 12));
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
}

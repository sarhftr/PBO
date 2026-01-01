package hotelapp.gui;

import javax.swing.*;
import org.jdesktop.swingx.JXDatePicker;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import hotelapp.model.*;
import hotelapp.service.DataManager;
import hotelapp.gui.components.ModernDialog;

public class BookingForm extends JFrame {

    private Tamu tamu;
    private DashboardTamu parent;

    private JComboBox<Kamar> kamarBox;
    private JXDatePicker checkInPicker, checkOutPicker;

    // ===== Samakan palette dengan DashboardTamu =====
    private final Color hoverColor = Color.decode("#3E3232"); // c1
    private final Color btnColor = Color.decode("#503C3C");   // c2
    private final Color borderColor = Color.decode("#7E6363"); // c3
    private final Color bgColor = Color.decode("#EEE4E1");    // bg

    public BookingForm(Tamu tamu, DashboardTamu parent){
        this.tamu = tamu;
        this.parent = parent;

        setTitle("Booking Kamar");
        setSize(480, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        init();
        setVisible(true);
    }

    private void init(){

        JPanel body = new JPanel();
        body.setBackground(bgColor);
        body.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Form Booking Kamar");
        title.setFont(new Font("Poppins", Font.BOLD, 20));
        title.setForeground(borderColor.darker());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        body.add(title);
        body.add(Box.createVerticalStrut(20));

        kamarBox = new JComboBox<>();
        for(Kamar k : DataManager.kamarList){
            if(k.isTersedia()) kamarBox.addItem(k);
        }
        styleComboBox(kamarBox);

        checkInPicker = new JXDatePicker();
        checkInPicker.setDate(new Date());
        styleDatePicker(checkInPicker);

        checkOutPicker = new JXDatePicker();
        checkOutPicker.setDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        styleDatePicker(checkOutPicker);

        body.add(createField("Pilih Kamar:", kamarBox));
        body.add(createField("Tanggal Check-in:", checkInPicker));
        body.add(createField("Tanggal Check-out:", checkOutPicker));

        RoundedButton btnBook = new RoundedButton("Booking Sekarang");
        btnBook.setPreferredSize(new Dimension(160,38));
        btnBook.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBook.setBackground(btnColor);
        btnBook.setForeground(Color.WHITE);
        // pakai HoverEffect static dari DashboardTamu
        btnBook.addMouseListener(new DashboardTamu.HoverEffect(btnBook, hoverColor));

        body.add(Box.createVerticalStrut(10));
        body.add(btnBook);

        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(null);
        add(sp);

        btnBook.addActionListener(e -> prosesBooking());
    }

    private JPanel createField(String label, JComponent comp){
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBackground(bgColor);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Poppins", Font.PLAIN, 14));
        lbl.setForeground(borderColor.darker());

        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createEmptyBorder(5,0,15,0));

        return p;
    }

    private void styleComboBox(JComboBox<?> box){
        box.setFont(new Font("Poppins", Font.PLAIN, 14));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(4,6,4,6)
        ));
    }

    private void styleDatePicker(JXDatePicker dp){
        dp.getEditor().setFont(new Font("Poppins", Font.PLAIN, 14));
        dp.getEditor().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(4,6,4,6)
        ));
        dp.setBackground(Color.WHITE);
    }

    private void prosesBooking(){
        Kamar kamar = (Kamar) kamarBox.getSelectedItem();
        if(kamar == null){
            ModernDialog.show(this, "Semua kamar sudah penuh!");
            return;
        }

        LocalDate checkIn = checkInPicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate checkOut = checkOutPicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if(!checkOut.isAfter(checkIn)){
            ModernDialog.show(this, "Tanggal check-out harus setelah check-in!");
            return;
        }

        String id = DataManager.nextBookingId();
        Booking booking = new Booking(id, tamu, kamar, checkIn, checkOut);
        kamar.setTersedia(false);

        DataManager.bookingList.add(booking);
        DataManager.saveAll();
        parent.refresh();

        ModernDialog.show(this, "Booking berhasil!\nID Booking: " + id);
        dispose();
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
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Poppins", Font.BOLD, 14));
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

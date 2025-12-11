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

    // === Color Palette sama seperti BookingPage ===
    private final Color cream = new Color(245,238,220);
    private final Color navy = new Color(39,76,119);
    private final Color darkRed = new Color(62,50,50);
    private final Color brown = new Color(126,99,99);

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
        body.setBackground(cream);
        body.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Form Booking Kamar");
        title.setFont(new Font("Poppins", Font.BOLD, 20));
        title.setForeground(darkRed);
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

        JButton btnBook = new JButton("Booking Sekarang");
        styleButton(btnBook);
        btnBook.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(Box.createVerticalStrut(10));
        body.add(btnBook);

        // Tambah scroll
        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(null);
        add(sp);

        // Action
        btnBook.addActionListener(e -> prosesBooking());
    }

    private JPanel createField(String label, JComponent comp){
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBackground(cream);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Poppins", Font.PLAIN, 14));
        lbl.setForeground(Color.DARK_GRAY);

        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createEmptyBorder(5,0,15,0));

        return p;
    }

    private void styleComboBox(JComboBox<?> box){
        box.setFont(new Font("Poppins", Font.PLAIN, 14));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(brown, 1, true),
                BorderFactory.createEmptyBorder(4,6,4,6)
        ));
    }

    private void styleDatePicker(JXDatePicker dp){
        dp.getEditor().setFont(new Font("Poppins", Font.PLAIN, 14));
        dp.getEditor().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(brown, 1, true),
                BorderFactory.createEmptyBorder(4,6,4,6)
        ));
        dp.setBackground(Color.WHITE);
    }

    private void styleButton(JButton b){
        b.setFont(new Font("Poppins", Font.BOLD, 14));
        b.setBackground(navy);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { b.setBackground(navy.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { b.setBackground(navy); }
        });
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
}

package hotelapp.gui;

import hotelapp.gui.components.ModernDialog;
import hotelapp.model.*;
import hotelapp.service.DataManager;
import java.awt.*;
import javax.swing.*;

public class PaymentForm extends JFrame {

    private Booking booking;
    private DashboardTamu parent;

    // ===== color palette konsisten dengan DashboardTamu =====
    private final Color hoverColor = Color.decode("#3E3232"); // c1
    private final Color btnColor = Color.decode("#503C3C");   // c2
    private final Color borderColor = Color.decode("#7E6363"); // c3
    private final Color bgColor = Color.decode("#EEE4E1");    // bg

    public PaymentForm(Booking booking, DashboardTamu parent){
        this.booking = booking;
        this.parent = parent;
        setTitle("Hotel Sariz - Pembayaran");
        setSize(480,380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init(){

        JPanel panel = new JPanel(null);
        panel.setBackground(bgColor);

        JLabel title = new JLabel("PAYMENT - HOTEL SARIZ");
        title.setFont(new Font("Poppins", Font.BOLD, 20));
        title.setForeground(btnColor);
        title.setBounds(30,15,300,30);
        panel.add(title);

        JLabel lblTotal = new JLabel("Total : Rp " + String.format("%.0f", booking.getTotalHarga()));
        lblTotal.setFont(new Font("Poppins", Font.BOLD, 14));
        lblTotal.setBounds(30,60,300,25);
        panel.add(lblTotal);

        JLabel lblMetode = new JLabel("Metode Pembayaran");
        JLabel lblBayar = new JLabel("Jumlah Bayar");
        JLabel lblKembali = new JLabel("Kembalian : Rp 0");

        lblMetode.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblBayar.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblKembali.setFont(new Font("Poppins", Font.BOLD, 13));

        lblMetode.setBounds(30,100,150,20);
        lblBayar.setBounds(30,150,150,20);
        lblKembali.setBounds(30,260,300,25);

        JComboBox<String> metodeBox = new JComboBox<>(new String[]{"Cash","Transfer"});
        metodeBox.setFont(new Font("Poppins", Font.PLAIN, 13));
        metodeBox.setBounds(190,95,200,30);

        RoundedTextField txtBayar = new RoundedTextField();
        txtBayar.setFont(new Font("Poppins", Font.PLAIN, 13));
        txtBayar.setBounds(190,145,200,30);

        panel.add(lblMetode);
        panel.add(lblBayar);
        panel.add(lblKembali);
        panel.add(metodeBox);
        panel.add(txtBayar);

        RoundedButton btnBayar = new RoundedButton("PROSES PEMBAYARAN");
        btnBayar.setBounds(30,210,360,40);
        btnBayar.setBackground(btnColor);
        btnBayar.setForeground(Color.WHITE);
        btnBayar.addMouseListener(new DashboardTamu.HoverEffect(btnBayar, btnColor));
        panel.add(btnBayar);

        btnBayar.addActionListener(e -> {
            try{
                double total = booking.getTotalHarga();
                double bayar = Double.parseDouble(txtBayar.getText().trim());
                String method = metodeBox.getSelectedItem().toString();

                if(bayar < total){
                    ModernDialog.show(this,"Uang kurang!");
                    return;
                }

                double kembali = bayar - total;
                lblKembali.setText("Kembalian : Rp " + String.format("%.0f", kembali));

                Payment p = new Payment(method, total, bayar);
                booking.setPayment(p);

                DataManager.saveAll();
                parent.refresh();

                ModernDialog.show(this,
                        "Pembayaran berhasil\nKembalian : Rp " + String.format("%.0f", kembali));
                dispose();

            }catch(Exception ex){
                ModernDialog.show(this,"Input tidak valid!");
            }
        });

        add(panel);
    }

    // ========== ROUNDED BUTTON ==========
    class RoundedButton extends JButton{
        RoundedButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(new Font("Poppins", Font.BOLD, 13));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
            super.paintComponent(g);
        }
    }

    // ========== ROUNDED TEXTFIELD ==========
    class RoundedTextField extends JTextField{
        RoundedTextField(){
            setBorder(null);
            setOpaque(false);
        }
        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
            super.paintComponent(g);
        }
        protected void paintBorder(Graphics g){
            Graphics2D g2=(Graphics2D)g;
            g2.setColor(borderColor);
            g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }
}

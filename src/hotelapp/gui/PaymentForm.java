package hotelapp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import hotelapp.model.*;
import hotelapp.service.DataManager;
import hotelapp.gui.components.ModernDialog;

public class PaymentForm extends JFrame {

    private Booking booking;
    private DashboardTamu parent;

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
        Color cream = new Color(245,238,220);
        Color blue = new Color(39,76,119);

        JPanel panel = new JPanel(null);
        panel.setBackground(cream);

        JLabel title = new JLabel("PAYMENT - HOTEL SARIZ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(blue);
        title.setBounds(30,15,300,30);
        panel.add(title);

        JLabel lblTotal = new JLabel("Total : Rp " + String.format("%.0f", booking.getTotalHarga()));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setBounds(30,60,300,25);
        panel.add(lblTotal);

        JLabel lblMetode = new JLabel("Metode Pembayaran");
        JLabel lblBayar = new JLabel("Jumlah Bayar");
        JLabel lblKembali = new JLabel("Kembalian : Rp 0");

        lblMetode.setBounds(30,100,150,20);
        lblBayar.setBounds(30,150,150,20);
        lblKembali.setBounds(30,260,300,25);
        lblKembali.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JComboBox<String> metodeBox = new JComboBox<>(new String[]{"Cash","Transfer"});
        metodeBox.setBounds(190,95,200,30);

        RoundedTextField txtBayar = new RoundedTextField();
        txtBayar.setBounds(190,145,200,30);

        panel.add(lblMetode);
        panel.add(lblBayar);
        panel.add(lblKembali);
        panel.add(metodeBox);
        panel.add(txtBayar);

        RoundedButton btnBayar = new RoundedButton("PROSES PEMBAYARAN");
        btnBayar.setBounds(30,210,360,40);
        btnBayar.setBackground(blue);
        btnBayar.setForeground(Color.WHITE);
        btnBayar.addMouseListener(new HoverEffect(btnBayar,blue));
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
            setFont(new Font("Segoe UI", Font.BOLD, 13));
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
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
            g2.setColor(new Color(200,200,200));
            g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }

    // ========== HOVER EFFECT ==========
    class HoverEffect extends MouseAdapter{
        JButton btn;
        Color base;
        HoverEffect(JButton btn,Color base){
            this.btn=btn;
            this.base=base;
        }
        public void mouseEntered(MouseEvent e){ btn.setBackground(base.darker()); }
        public void mouseExited(MouseEvent e){ btn.setBackground(base); }
    }
}

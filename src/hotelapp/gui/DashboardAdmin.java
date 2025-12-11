package hotelapp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import hotelapp.service.DataManager;
import hotelapp.model.*;
import hotelapp.gui.components.ModernDialog;

public class DashboardAdmin extends JFrame {

    private DefaultListModel<Tamu> modelTamu;
    private DefaultListModel<Booking> modelBooking;
    private DefaultListModel<String> modelActive;

    public DashboardAdmin(){
        setTitle("Hotel Sariz - Dashboard Admin");
        setSize(1100,560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init(){
        Color cream = new Color(245,238,220);
        Color blue = new Color(39,76,119);

        JPanel panel = new JPanel(null);
        panel.setBackground(cream);

        JLabel title = new JLabel("Dashboard Admin - HOTEL SARIZ");
        title.setBounds(30,15,600,35);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(blue);
        panel.add(title);

        // ===== JUDUL PANEL =====
        JLabel lblTamu = new JLabel("DATA TAMU");
        lblTamu.setBounds(30,45,300,20);
        lblTamu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTamu.setForeground(blue);

        JLabel lblBooking = new JLabel("DATA BOOKING");
        lblBooking.setBounds(380,45,300,20);
        lblBooking.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBooking.setForeground(blue);

        JLabel lblActive = new JLabel("TAMU SEDANG CHECK-IN");
        lblActive.setBounds(730,45,300,20);
        lblActive.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblActive.setForeground(blue);

        panel.add(lblTamu);
        panel.add(lblBooking);
        panel.add(lblActive);

        modelTamu = new DefaultListModel<>();
        modelBooking = new DefaultListModel<>();
        modelActive = new DefaultListModel<>();

        JList<Tamu> listTamu = new JList<>(modelTamu);
        JList<Booking> listBooking = new JList<>(modelBooking);
        JList<String> listActive = new JList<>(modelActive);

        JScrollPane spT = new JScrollPane(listTamu);
        JScrollPane spB = new JScrollPane(listBooking);
        JScrollPane spA = new JScrollPane(listActive);

        spT.setBounds(30,70,330,360);
        spB.setBounds(380,70,330,360);
        spA.setBounds(730,70,330,360);

        panel.add(spT);
        panel.add(spB);
        panel.add(spA);

        RoundedButton addBtn = new RoundedButton("TAMBAH TAMU");
        RoundedButton delBtn = new RoundedButton("HAPUS");
        RoundedButton manageBtn = new RoundedButton("MANAGE KAMAR");
        RoundedButton saveBtn = new RoundedButton("SAVE");
        RoundedButton refreshBtn = new RoundedButton("REFRESH");
        RoundedButton logoutBtn = new RoundedButton("LOGOUT");

        RoundedButton[] btns = {addBtn,delBtn,manageBtn,saveBtn,refreshBtn,logoutBtn};
        int x=30;

        for(RoundedButton b:btns){
            b.setBounds(x,450,160,35);
            b.setBackground(blue);
            b.setForeground(Color.WHITE);
            panel.add(b);
            x+=170;
            b.addMouseListener(new HoverEffect(b,blue));
        }

        addBtn.addActionListener(e -> {
            String name=JOptionPane.showInputDialog(this,"Nama:");
            String user=JOptionPane.showInputDialog(this,"Username:");
            String pass=JOptionPane.showInputDialog(this,"Password:");
            if(name!=null && user!=null && pass!=null){
                DataManager.tamuList.add(new Tamu(DataManager.nextTamuId(),user,pass,name));
                refreshAll();
            }
        });

        delBtn.addActionListener(e->{
            Tamu t=listTamu.getSelectedValue();
            if(t!=null){ DataManager.tamuList.remove(t); refreshAll(); }
        });

        manageBtn.addActionListener(e-> new ManageKamarForm());

        saveBtn.addActionListener(e->{
            DataManager.saveAll();
            ModernDialog.show(this,"Data tersimpan");
        });

        refreshBtn.addActionListener(e->refreshAll());
        logoutBtn.addActionListener(e->{ dispose(); new LoginForm(); });

        refreshAll();
        add(panel);
    }

    private void refreshAll(){
        modelTamu.clear();
        modelBooking.clear();
        modelActive.clear();

        for(Tamu t:DataManager.tamuList) modelTamu.addElement(t);
        for(Booking b:DataManager.bookingList) modelBooking.addElement(b);

        for(Booking b:DataManager.bookingList){
            if(b.isCheckedIn() && !b.isCheckedOut()){
                modelActive.addElement(
                    b.getTamu().getName()+" | Kamar "+
                    b.getKamar().getNomor()+" ("+b.getKamar().getTipe()+")"
                );
            }
        }
    }

    // ================= CUSTOM BUTTON =================

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
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

    class HoverEffect extends MouseAdapter{
        private JButton btn;
        private Color base;
        HoverEffect(JButton btn,Color base){
            this.btn=btn;
            this.base=base;
        }
        public void mouseEntered(MouseEvent e){
            btn.setBackground(base.darker());
        }
        public void mouseExited(MouseEvent e){
            btn.setBackground(base);
        }
    }
}

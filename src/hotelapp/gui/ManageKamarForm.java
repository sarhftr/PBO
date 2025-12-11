package hotelapp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import hotelapp.model.*;
import hotelapp.service.DataManager;
import hotelapp.gui.components.ModernDialog;

public class ManageKamarForm extends JFrame {

    private DefaultListModel<Kamar> kmModel;
    private JList<Kamar> list;

    public ManageKamarForm(){
        setTitle("Hotel Sariz - Manage Kamar");
        setSize(650,480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init(){
        Color cream = new Color(245,238,220);
        Color blue = new Color(39,76,119);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(cream);

        JLabel title = new JLabel("MANAGE KAMAR - HOTEL SARIZ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(blue);
        title.setBounds(30,15,400,30);
        mainPanel.add(title);

        // ===== FORM INPUT =====
        JLabel lblNo = new JLabel("Nomor Kamar");
        JLabel lblType = new JLabel("Tipe Kamar");
        JLabel lblPrice = new JLabel("Harga");

        lblNo.setBounds(30,60,120,20);
        lblType.setBounds(30,100,120,20);
        lblPrice.setBounds(30,140,120,20);

        RoundedTextField tfNo = new RoundedTextField();
        RoundedTextField tfType = new RoundedTextField();
        RoundedTextField tfPrice = new RoundedTextField();

        tfNo.setBounds(150,55,200,30);
        tfType.setBounds(150,95,200,30);
        tfPrice.setBounds(150,135,200,30);

        mainPanel.add(lblNo);
        mainPanel.add(lblType);
        mainPanel.add(lblPrice);
        mainPanel.add(tfNo);
        mainPanel.add(tfType);
        mainPanel.add(tfPrice);

        // ===== LIST KAMAR =====
        kmModel = new DefaultListModel<>();
        list = new JList<>(kmModel);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(380,55,230,290);
        mainPanel.add(scroll);

        // ===== BUTTON =====
        RoundedButton addBtn = new RoundedButton("TAMBAH");
        RoundedButton delBtn = new RoundedButton("HAPUS");
        RoundedButton toggleBtn = new RoundedButton("TOGGLE STATUS");

        addBtn.setBounds(30,190,150,35);
        delBtn.setBounds(190,190,150,35);
        toggleBtn.setBounds(30,240,310,35);

        for(JButton btn : new JButton[]{addBtn,delBtn,toggleBtn}){
            btn.setBackground(blue);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.addMouseListener(new HoverEffect(btn,blue));
            mainPanel.add(btn);
        }

        addBtn.addActionListener(e -> {
            try{
                String nomor = tfNo.getText();
                String tipe = tfType.getText();
                double harga = Double.parseDouble(tfPrice.getText());

                DataManager.kamarList.add(new Kamar(nomor, tipe, harga));
                clearInput(tfNo,tfType,tfPrice);
                refresh();
            }catch(Exception ex){
                ModernDialog.show(this,"Input tidak valid!");
            }
        });

        delBtn.addActionListener(e -> {
            Kamar k = list.getSelectedValue();
            if(k!=null){
                DataManager.kamarList.remove(k);
                refresh();
            }
        });

        toggleBtn.addActionListener(e -> {
            Kamar k = list.getSelectedValue();
            if(k!=null){
                k.setTersedia(!k.isTersedia());
                refresh();
            }
        });

        refresh();
        add(mainPanel);
    }

    private void refresh(){
        kmModel.clear();
        for(Kamar k : DataManager.kamarList){
            kmModel.addElement(k);
        }
    }

    private void clearInput(JTextField... fields){
        for(JTextField f : fields) f.setText("");
    }

    // ================= ROUNDED BUTTON =================
    class RoundedButton extends JButton{
        RoundedButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
            super.paintComponent(g);
        }
    }

    // ================= ROUNDED TEXTFIELD =================
    class RoundedTextField extends JTextField{
        RoundedTextField(){
            setBorder(null);
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(200,200,200));
            g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }

    // ================= HOVER EFFECT =================
    class HoverEffect extends MouseAdapter{
        private JButton btn;
        private Color base;

        HoverEffect(JButton btn, Color base){
            this.btn = btn;
            this.base = base;
        }

        public void mouseEntered(MouseEvent e){
            btn.setBackground(base.darker());
        }
        public void mouseExited(MouseEvent e){
            btn.setBackground(base);
        }
    }
}

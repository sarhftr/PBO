package hotelapp.gui;

import hotelapp.gui.components.ModernDialog;
import hotelapp.model.*;
import hotelapp.service.DataManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ManageKamarForm extends JFrame {

    private JPanel cardPanel;
    
    // ==== PALETTE & FONT SAMA DASHBOARDADMIN / BOOKINGPAGE ====
    private final Color hoverColor = Color.decode("#3E3232");  // tombol hover
    private final Color btnColor   = Color.decode("#503C3C");  // tombol utama
    private final Color borderColor= Color.decode("#7E6363");  // border card
    private final Color bgColor    = Color.decode("#EEE4E1");  // background panel
    private final Color textColor  = new Color(62,50,50);      // teks card

    public ManageKamarForm(){
        setTitle("Hotel Sariz - Manage Kamar");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // ===== HEADER =====
        JLabel title = new JLabel("MANAGE KAMAR - HOTEL SARIZ");
        title.setFont(new Font("Poppins", Font.BOLD, 22));
        title.setForeground(btnColor);
        title.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        mainPanel.add(title, BorderLayout.NORTH);

        // ===== BODY =====
        cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(bgColor);

        JScrollPane sp = new JScrollPane(cardPanel);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(sp, BorderLayout.CENTER);

        // ===== FORM INPUT & BUTTONS =====
        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new Dimension(680, 140));
        formPanel.setBackground(bgColor);

        JLabel lblNo = new JLabel("Nomor Kamar");
        JLabel lblType = new JLabel("Tipe Kamar");
        JLabel lblPrice = new JLabel("Harga");
        lblNo.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblType.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblPrice.setFont(new Font("Poppins", Font.PLAIN, 13));

        lblNo.setBounds(10,10,100,25);
        lblType.setBounds(10,40,100,25);
        lblPrice.setBounds(10,70,100,25);

        RoundedTextField tfNo = new RoundedTextField();
        RoundedTextField tfType = new RoundedTextField();
        RoundedTextField tfPrice = new RoundedTextField();

        tfNo.setBounds(120,10,150,25);
        tfType.setBounds(120,40,150,25);
        tfPrice.setBounds(120,70,150,25);

        formPanel.add(lblNo); formPanel.add(lblType); formPanel.add(lblPrice);
        formPanel.add(tfNo); formPanel.add(tfType); formPanel.add(tfPrice);

        RoundedButton addBtn = new RoundedButton("TAMBAH");
        RoundedButton delBtn = new RoundedButton("HAPUS");
        RoundedButton toggleBtn = new RoundedButton("TOGGLE STATUS");

        addBtn.setBounds(300,10,120,30);
        delBtn.setBounds(430,10,120,30);
        toggleBtn.setBounds(300,50,250,30);

        for(RoundedButton btn : new RoundedButton[]{addBtn, delBtn, toggleBtn}){
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Poppins", Font.BOLD, 12));
            btn.addMouseListener(new HoverEffect(btn, hoverColor));
            formPanel.add(btn);
        }

        mainPanel.add(formPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====
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
            Kamar k = getSelectedKamar();
            if(k!=null){
                DataManager.kamarList.remove(k);
                refresh();
            }
        });

        toggleBtn.addActionListener(e -> {
            Kamar k = getSelectedKamar();
            if(k!=null){
                k.setTersedia(!k.isTersedia());
                refresh();
            }
        });

        add(mainPanel);
        refresh();
    }

    private void refresh(){
        cardPanel.removeAll();
        for(Kamar k : DataManager.kamarList){
            cardPanel.add(kamarCard(k));
            cardPanel.add(Box.createVerticalStrut(8));
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private JPanel kamarCard(Kamar k){
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor,2,true),
                BorderFactory.createEmptyBorder(8,8,8,8)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblHeader = new JLabel(k.getTipe() + " - No " + k.getNomor());
        lblHeader.setFont(new Font("Poppins", Font.BOLD,14));
        lblHeader.setForeground(btnColor);

        JLabel lblPrice = new JLabel("Rp " + String.format("%.0f", k.getHarga()));
        lblPrice.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblPrice.setForeground(textColor);

        JLabel lblStatus = new JLabel(k.isTersedia() ? "AVAILABLE" : "NOT AVAILABLE");
        lblStatus.setFont(new Font("Poppins", Font.BOLD,12));
        lblStatus.setForeground(k.isTersedia() ? new Color(34,139,34) : Color.RED);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.add(lblHeader);
        info.add(lblPrice);
        info.add(lblStatus);

        card.add(info, BorderLayout.CENTER);

        // ===== CLICK SELECTION =====
        card.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){
                for(Component c : cardPanel.getComponents()){
                    if(c instanceof JPanel){
                        JPanel panel = (JPanel)c;
                        panel.putClientProperty("selected", false);
                        panel.setBackground(Color.WHITE);
                    }
                }
                card.putClientProperty("selected", true);
                card.setBackground(new Color(230,245,255));
            }
        });

        card.putClientProperty("kamarObj", k);
        card.putClientProperty("selected", false);
        return card;
    }

    private Kamar getSelectedKamar(){
        for(Component c : cardPanel.getComponents()){
            if(c instanceof JPanel){
                JPanel panel = (JPanel)c;
                Object obj = panel.getClientProperty("kamarObj");
                Object sel = panel.getClientProperty("selected");
                if(obj instanceof Kamar && sel instanceof Boolean && (Boolean)sel){
                    return (Kamar)obj;
                }
            }
        }
        return null;
    }

    private void clearInput(JTextField... fields){
        for(JTextField f : fields) f.setText("");
    }

    // ===== ROUNDED BUTTON =====
    class RoundedButton extends JButton{
        private int radius = 25;
        RoundedButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? getBackground().darker() : getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);
            FontMetrics fm = g2.getFontMetrics();
            String text = getText();
            int x = (getWidth() - fm.stringWidth(text))/2;
            int y = (getHeight() - fm.getHeight())/2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.drawString(text,x,y);
            g2.dispose();
        }
    }

    class RoundedTextField extends JTextField{
        RoundedTextField(){
            setBorder(null);
            setOpaque(false);
            setFont(new Font("Poppins", Font.PLAIN,13));
        }

        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
            super.paintComponent(g2);
            g2.dispose();
        }

        protected void paintBorder(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(borderColor);
            g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
            g2.dispose();
        }
    }

    class HoverEffect extends MouseAdapter{
        private JButton btn;
        private Color base;

        HoverEffect(JButton btn, Color base){
            this.btn = btn;
            this.base = base;
        }

        @Override public void mouseEntered(MouseEvent e){ btn.setBackground(base.darker()); }
        @Override public void mouseExited(MouseEvent e){ btn.setBackground(base); }
    }
}

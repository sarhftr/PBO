package hotelapp.gui;

import hotelapp.model.*;
import hotelapp.service.DataManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class DaftarKamarPage extends JFrame {

    private DashboardTamu parent;
    private Tamu tamu;
    private JPanel grid;

    // ==== SAMAKAN PALETTE DENGAN BookingPage ====
    private final Color hoverColor = Color.decode("#3E3232");  // c1
    private final Color btnColor = Color.decode("#503C3C");    // c2
    private final Color borderColor = Color.decode("#7E6363"); // c3
    private final Color bgColor = Color.decode("#EEE4E1");     // bg

    public DaftarKamarPage(DashboardTamu parent, Tamu tamu){
        this.parent = parent;
        this.tamu = tamu;

        setTitle("Daftar Kamar - " + tamu.getName());
        setSize(820, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        init();
        loadKamar();

        setVisible(true);
    }

    private void init(){
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(btnColor);
        header.setPreferredSize(new Dimension(getWidth(), 56));
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel title = new JLabel("Daftar Kamar Hotel");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Poppins", Font.BOLD, 16));
        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= BODY =================
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(bgColor);
        body.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        grid = new JPanel(new GridLayout(0,2,12,12));
        grid.setBackground(bgColor);

        JScrollPane sp = new JScrollPane(grid);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);

        body.add(sp, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        // ================= FOOTER =================
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(bgColor);

        RoundedButton btnBack = new RoundedButton("KEMBALI");
        btnBack.setPreferredSize(new Dimension(120,38));
        btnBack.setBackground(btnColor);
        btnBack.setForeground(Color.WHITE);
        btnBack.addMouseListener(new HoverEffect(btnBack, hoverColor));
        btnBack.addActionListener(e -> dispose());

        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadKamar(){
        grid.removeAll();

        for(Kamar k : DataManager.kamarList){
            grid.add(kamarCard(k));
        }

        grid.revalidate();
        grid.repaint();
    }

    private JPanel kamarCard(Kamar k){
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        // ===== Nama, harga, status =====
        JLabel name = new JLabel("<html><b>" + k.getTipe() + " - No " + k.getNomor() + "</b></html>");
        name.setFont(new Font("Poppins", Font.BOLD, 14));

        JLabel price = new JLabel("Rp " + String.format("%.0f", k.getHarga()));
        price.setFont(new Font("Poppins", Font.PLAIN, 13));

        JLabel status = new JLabel(k.isTersedia() ? "AVAILABLE" : "NOT AVAILABLE");
        status.setFont(new Font("Poppins", Font.BOLD, 12));
        status.setForeground(k.isTersedia() ? new Color(34,139,34) : Color.RED);

        JPanel info = new JPanel(new GridLayout(0,1));
        info.setOpaque(false);
        info.add(name);
        info.add(price);
        info.add(status);

        c.add(info, BorderLayout.CENTER);

        // ================= CLICK CARD =================
        c.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){
                String txt = 
                        k.getNomor() + " - " + k.getTipe() +
                        "\nHarga: Rp " + String.format("%.0f", k.getHarga()) +
                        "\nStatus: " + (k.isTersedia() ? "AVAILABLE" : "NOT AVAILABLE");

                int x = JOptionPane.showOptionDialog(
                        DaftarKamarPage.this,
                        txt,
                        "Detail Kamar",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Pesan", "Tutup"},
                        "Pesan");

                if(x == 0 && k.isTersedia()){
                    new BookingForm(tamu, parent);
                    dispose();
                }
            }
        });

        return c;
    }

    // ================= BUTTON STYLES =================
    class RoundedButton extends JButton {
        private int radius = 25;
        RoundedButton(String text){
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
            int x = (getWidth() - fm.stringWidth(text))/2;
            int y = (getHeight() - fm.getHeight())/2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.drawString(text,x,y);
            g2.dispose();
        }
    }

    class HoverEffect extends MouseAdapter {
        private JButton btn;
        private Color base;
        HoverEffect(JButton b, Color c){ btn = b; base = c; }
        @Override public void mouseEntered(MouseEvent e){ btn.setBackground(base.darker()); }
        @Override public void mouseExited(MouseEvent e){ btn.setBackground(base); }
    }
}

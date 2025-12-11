package hotelapp.gui.components;

import javax.swing.*;
import java.awt.*;

public class ModernDialog extends JDialog {

    public ModernDialog(JFrame parent, String message, Color bgColor) {
        super(parent, true);
        setUndecorated(true);
        setSize(360,160);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel lbl = new JLabel(message, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btn = new JButton("OK");
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(bgColor);
        btn.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
        btn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(btn);

        panel.add(lbl, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    // Static method biar gampang dipanggil
    public static void show(JFrame parent, String msg){
        Color elegantBlue = new Color(52, 95, 164); // biru elegant Hotel Sariz
        new ModernDialog(parent, msg, elegantBlue).setVisible(true);
    }
}

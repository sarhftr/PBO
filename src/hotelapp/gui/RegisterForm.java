package hotelapp.gui;

import javax.swing.*;
import java.awt.*;
import hotelapp.service.AuthService;
import hotelapp.gui.components.ModernDialog; // <<< pastikan import ini ada

public class RegisterForm extends JFrame {

    private JTextField txtUser, txtName;
    private JPasswordField txtPass;
    private JFrame parent;

    private final Color CREAM = new Color(245, 238, 220);
    private final Color BLUE = new Color(39, 76, 119);
    private final Color BLUE_HOVER = new Color(30, 60, 95);

    public RegisterForm(JFrame parent){
        this.parent = parent;
        setTitle("Hotel Sariz - Register");
        setSize(450,360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(CREAM);

        JLabel title = new JLabel("REGISTRASI TAMU", SwingConstants.CENTER);
        title.setBounds(90,20,260,40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(BLUE);
        panel.add(title);

        JLabel lblName = new JLabel("Nama Lengkap");
        lblName.setBounds(60,90,100,25);
        panel.add(lblName);

        txtName = new RoundedTextField(20);
        txtName.setBounds(160,90,200,32);
        panel.add(txtName);

        JLabel lblUser = new JLabel("Username");
        lblUser.setBounds(60,135,100,25);
        panel.add(lblUser);

        txtUser = new RoundedTextField(20);
        txtUser.setBounds(160,135,200,32);
        panel.add(txtUser);

        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(60,180,100,25);
        panel.add(lblPass);

        txtPass = new RoundedPasswordField(20);
        txtPass.setBounds(160,180,200,32);
        panel.add(txtPass);

        RoundedButton btnRegister = new RoundedButton("REGISTER");
        btnRegister.setBounds(160,230,200,35);
        btnRegister.setBackground(BLUE);
        btnRegister.setForeground(Color.WHITE);
        panel.add(btnRegister);

        RoundedButton btnBack = new RoundedButton("BACK TO LOGIN");
        btnBack.setBounds(160,275,200,35);
        btnBack.setBackground(new Color(200,200,200));
        btnBack.setForeground(Color.BLACK);
        panel.add(btnBack);

        // HOVER REGISTER
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnRegister.setBackground(BLUE_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnRegister.setBackground(BLUE); }
        });

        // HOVER BACK
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnBack.setBackground(new Color(180,180,180)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnBack.setBackground(new Color(200,200,200)); }
        });

        btnRegister.addActionListener(e -> doRegister());
        btnBack.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void doRegister(){
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if(name.isEmpty() || user.isEmpty() || pass.isEmpty()){
            // pakai ModernDialog (2-arg)
            ModernDialog.show(this, "Lengkapi semua data!");
            return;
        }

        if(AuthService.registerTamu(user, pass, name) == null){
            // pakai ModernDialog (3-arg)
            ModernDialog.show(this, "Username sudah digunakan! Gagal Registrasi");
            return;
        }

        // sukses
        ModernDialog.show(this, "Registrasi berhasil. Silakan login.");
        parent.setVisible(true);
        dispose();
    }

    // ================= COMPONENTS (sama style dengan LoginForm) =================

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        }

        protected void paintBorder(Graphics g) {
            g.setColor(new Color(39,76,119));
            g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }

    class RoundedPasswordField extends JPasswordField {
        public RoundedPasswordField(int size) {
            super(size);
            setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        }

        protected void paintBorder(Graphics g) {
            g.setColor(new Color(39,76,119));
            g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }
}

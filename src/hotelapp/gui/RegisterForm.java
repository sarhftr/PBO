package hotelapp.gui;

import hotelapp.gui.components.ModernDialog;
import hotelapp.service.AuthService;
import java.awt.*;
import javax.swing.*;

public class RegisterForm extends JFrame {

    private JTextField txtUser, txtName;
    private JPasswordField txtPass;
    private JFrame parent;

    // ===== SAME COLOR PALETTE AS LoginForm =====
    private final Color c1 = Color.decode("#3E3232"); // dark red / title / hover login
    private final Color c2 = Color.decode("#503C3C"); // main login button
    private final Color c3 = Color.decode("#7E6363"); // hover register
    private final Color c4 = Color.decode("#A87C7C"); // register button
    private final Color bg = Color.decode("#EEE4E1"); // background panel

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
        panel.setBackground(bg);

        // ===== TITLE =====
        JLabel title = new JLabel("REGISTRASI TAMU", SwingConstants.CENTER);
        title.setBounds(90,20,260,40);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(c1);
        panel.add(title);

        // ===== LABELS =====
        JLabel lblName = new JLabel("Nama Lengkap");
        lblName.setBounds(60,90,100,25);
        lblName.setFont(new Font("Poppins", Font.PLAIN, 14));
        lblName.setForeground(c1);
        panel.add(lblName);

        JLabel lblUser = new JLabel("Username");
        lblUser.setBounds(60,135,100,25);
        lblUser.setFont(new Font("Poppins", Font.PLAIN, 14));
        lblUser.setForeground(c1);
        panel.add(lblUser);

        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(60,180,100,25);
        lblPass.setFont(new Font("Poppins", Font.PLAIN, 14));
        lblPass.setForeground(c1);
        panel.add(lblPass);

        // ===== FIELDS =====
        txtName = new RoundedTextField(20);
        txtName.setBounds(160,90,200,32);
        panel.add(txtName);

        txtUser = new RoundedTextField(20);
        txtUser.setBounds(160,135,200,32);
        panel.add(txtUser);

        txtPass = new RoundedPasswordField(20);
        txtPass.setBounds(160,180,200,32);
        panel.add(txtPass);

        // ===== BUTTONS =====
        RoundedButton btnRegister = new RoundedButton("REGISTER");
        btnRegister.setBounds(160,230,200,35);
        btnRegister.setBackground(c2);
        btnRegister.setForeground(Color.WHITE);
        panel.add(btnRegister);

        RoundedButton btnBack = new RoundedButton("BACK TO LOGIN");
        btnBack.setBounds(160,275,200,35);
        btnBack.setBackground(c4);
        btnBack.setForeground(Color.BLACK);
        panel.add(btnBack);

        // ===== HOVER EFFECTS =====
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnRegister.setBackground(c1); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnRegister.setBackground(c2); }
        });

        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnBack.setBackground(c3); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnBack.setBackground(c4); }
        });

        // ===== ACTION =====
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
            ModernDialog.show(this, "Lengkapi semua data!");
            return;
        }

        if(AuthService.registerTamu(user, pass, name) == null){
            ModernDialog.show(this, "Username sudah digunakan! Gagal Registrasi");
            return;
        }

        ModernDialog.show(this, "Registrasi berhasil. Silakan login.");
        parent.setVisible(true);
        dispose();
    }

    // ================= COMPONENTS (Poppins + Rounded) =================

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(new Font("Poppins", Font.BOLD, 14));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
            setFont(new Font("Poppins", Font.PLAIN, 14));
        }

        protected void paintBorder(Graphics g) {
            g.setColor(c2);
            g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }

    class RoundedPasswordField extends JPasswordField {
        public RoundedPasswordField(int size) {
            super(size);
            setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
            setFont(new Font("Poppins", Font.PLAIN, 14));
        }

        protected void paintBorder(Graphics g) {
            g.setColor(c2);
            g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }
}

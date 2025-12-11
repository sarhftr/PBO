package hotelapp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import hotelapp.service.AuthService;
import hotelapp.model.User;
import hotelapp.gui.components.ModernDialog;

public class LoginForm extends JFrame {

    // ===== COLOR PALETTE =====
    private final Color c1 = Color.decode("#3E3232");
    private final Color c2 = Color.decode("#503C3C");
    private final Color c3 = Color.decode("#7E6363");
    private final Color c4 = Color.decode("#A87C7C");
    private final Color bg = Color.decode("#EEE4E1");

    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginForm(){
        setTitle("Hotel Sariz - Login");
        setSize(450,320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(bg);

        JLabel title = new JLabel("HOTEL SARIZ", SwingConstants.CENTER);
        title.setBounds(90,20,260,40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(c1);
        panel.add(title);

        JLabel lblUser = new JLabel("Username");
        lblUser.setBounds(60,90,100,25);
        lblUser.setForeground(c1);
        panel.add(lblUser);

        txtUser = new RoundedTextField(20);
        txtUser.setBounds(160,90,200,32);
        panel.add(txtUser);

        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(60,135,100,25);
        lblPass.setForeground(c1);
        panel.add(lblPass);

        txtPass = new RoundedPasswordField(20);
        txtPass.setBounds(160,135,200,32);
        panel.add(txtPass);

        RoundedButton btnLogin = new RoundedButton("LOGIN");
        btnLogin.setBounds(160,185,200,35);
        btnLogin.setBackground(c2);
        btnLogin.setForeground(Color.WHITE);
        panel.add(btnLogin);

        RoundedButton btnRegister = new RoundedButton("REGISTER");
        btnRegister.setBounds(160,230,200,35);
        btnRegister.setBackground(c4);
        btnRegister.setForeground(Color.WHITE);
        panel.add(btnRegister);

        // Hover LOGIN
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(c1);
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(c2);
            }
        });

        // Hover REGISTER
        btnRegister.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnRegister.setBackground(c3);
            }
            public void mouseExited(MouseEvent e) {
                btnRegister.setBackground(c4);
            }
        });

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> {
            new RegisterForm(this);
            setVisible(false);
        });

        add(panel);
    }

    private void doLogin(){
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword()).trim();

        User user = AuthService.login(u,p);
        if(user == null){
            ModernDialog.show(this,"Login gagal!");
            return;
        }

        if(user.getRole().equals("ADMIN")){
            new DashboardAdmin();
        }else{
            new DashboardTamu((hotelapp.model.Tamu) user);
        }
        dispose();
    }

    // ================= COMPONENT CUSTOM =================

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
            g.setColor(c2);
            g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }

    class RoundedPasswordField extends JPasswordField {
        public RoundedPasswordField(int size) {
            super(size);
            setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        }

        protected void paintBorder(Graphics g) {
            g.setColor(c2);
            g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
        }
    }
}

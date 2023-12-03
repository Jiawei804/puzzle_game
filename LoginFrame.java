import javax.swing.*;
import java.awt.*;
import java.io.*;

public class LoginFrame extends JFrame {
    JButton login_button;
    JButton register_button;
    JLabel user_label;
    JLabel passWord_label;
    JLabel tip_label;
    JTextField userName_field;
    JPasswordField userPassWord_field;
    String username;
    String user_password;


    public LoginFrame(String title)
    {
        super(title);
        setSize(600,400);
        addComponent();
        addListener();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addComponent() {
        ImageIcon imageIcon = new ImageIcon("img\\LoginPicture.png");

        JLabel label = new JLabel(imageIcon);
        label.setBounds(0,0,this.getWidth(),this.getHeight());
        login_button = new JButton("登录");
        register_button = new JButton("注册");
        user_label = new JLabel("账号:");
        passWord_label = new JLabel("密码:");
        userName_field = new JTextField(15);
        userPassWord_field = new JPasswordField(15);
        JPanel panel = new JPanel(null);
        user_label.setBounds(150,100,50,30);
        passWord_label.setBounds(150,170,50,30);
        login_button.setBounds(220,220,60,50);
        register_button.setBounds(300,220,60,50);
        userName_field.setBounds(200,100,200,30);
        userPassWord_field.setBounds(200,170,200,30);
        tip_label = new JLabel();
        tip_label.setVisible(false);
        tip_label.setFont(new Font("",Font.PLAIN,30));

        tip_label.setBounds(400,120,180,50);
        panel.add(tip_label);
        panel.add(login_button);
        panel.add(register_button);
        panel.add(userName_field);
        panel.add(user_label);
        panel.add(passWord_label);
        panel.add(userPassWord_field);
        panel.add(label);
        add(panel);

    }

    private void addListener() {
        login_button.addActionListener(e -> {       //登录按钮事件处理
            try {

                username = userName_field.getText();
                user_password = String.valueOf(userPassWord_field.getPassword());
                if(test())
                {    tip_label.setText("登陆成功！");
                    tip_label.setForeground(Color.GREEN);
                    tip_label.setVisible(true);
                    new GameFrame(username);

                }
                else
                {
                    tip_label.setText("登陆失败！");
                    tip_label.setForeground(Color.RED);
                    tip_label.setVisible(true);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        register_button.addActionListener(e -> registerFrame());       //注册按钮事件处理
    }

    public boolean test() throws IOException {
        File user_message = new File("user_message.txt");
        BufferedReader bf = new BufferedReader(new FileReader(user_message));
        String user,password;
        while((user = bf.readLine()) != null)
        {
            password = bf.readLine();
            if (username.equals(user) && user_password.equals(password)) {
                return true;
            }
        }
        return  false;
    }

    public void registerFrame()
    {
        JFrame frame = new JFrame();
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        ImageIcon imageIcon = new ImageIcon("img\\LoginPicture.png");
        JLabel bk = new JLabel(imageIcon);
        bk.setBounds(0,0,frame.getWidth(),frame.getHeight());
        JLabel jLabel = new JLabel("账号：");
        JLabel jLabel1 = new JLabel("密码:");
        JLabel jLabel2 = new JLabel("确认:");
        jLabel.setBounds(150,50,50,30);
        jLabel1.setBounds(150,120,50,30);
        jLabel2.setBounds(150,190,50,30);
        JButton register_button = new JButton("注册");
        register_button.setBounds(230,250,60,40);

        JTextField t1 = new JTextField();
        JTextField t2 = new JTextField();
        JTextField t3 = new JTextField();
        t1.setBounds(200,50,150,40);
        t2.setBounds(200,120,150,40);
        t3.setBounds(200,190,150,40);
        JLabel l = new JLabel("");
        l.setBounds(400,140,150,50);

        l.setVisible(false);
        panel.add(l);
        panel.add(jLabel);
        panel.add(jLabel1);
        panel.add(jLabel2);
        panel.add(t1);
        panel.add(t2);
        panel.add(t3);
        panel.add(register_button);
        panel.add(bk);

        frame.add(panel);

        register_button.addActionListener(e -> {
            String temp1 = t1.getText();
            String temp2 = t2.getText();
            String temp3 = t3.getText();
            System.out.println(temp1+ " " + temp2 + " " + temp3);
            if(temp2.equals(temp3)) {
                l.setText("注册成功！");
                l.setForeground(Color.GREEN);
                l.setFont(new Font("",Font.PLAIN,20));
                l.setVisible(true);
                FileWriter fw = null;
                try {
                    fw = new FileWriter("user_message.txt", true);
                    fw.write(temp1 + "\n");
                    fw.write(temp2 + "\n");
                } catch (Exception m) {
                    m.printStackTrace();
                } finally {
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                l.setText("密码不一致！");
                l.setForeground(Color.red);
                l.setFont(new Font("",Font.PLAIN,20));
                l.setVisible(true);
            }
        });
    }
}

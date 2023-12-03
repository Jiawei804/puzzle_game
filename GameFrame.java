import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.*;

public class GameFrame extends JFrame {
    JButton easy_button;
    JButton normal_button;
    JButton hard_button;
    JButton limit_button;
    JButton top_button;
    JButton rule_button;
    String user_name;


    public GameFrame(String user) {
        this.user_name = user;
        gameOptions();
        addListener();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void gameOptions() {
        setSize(200, 300);

        JPanel panel_bk = new JPanel();
        panel_bk.setLayout(new GridLayout(6,1));
        JPanel easy_panel = new JPanel();
        JPanel normal_panel = new JPanel();
        JPanel hard_panel = new JPanel();
        JPanel limit_panel = new JPanel();
        JPanel top_panel = new JPanel();
        JPanel rule_panel = new JPanel();

        easy_button = new JButton("简单模式");
        normal_button = new JButton("普通模式");
        hard_button = new JButton("困难模式");
        limit_button = new JButton("限时模式");
        top_button = new JButton("历史排名");
        rule_button = new JButton("游戏规则");
        easy_panel.add(easy_button);
        normal_panel.add(normal_button);
        hard_panel.add(hard_button);
        limit_panel.add(limit_button);
        top_panel.add(top_button);
        rule_panel.add(rule_button);

        panel_bk.add(easy_panel);
        panel_bk.add(normal_panel);
        panel_bk.add(hard_panel);
        panel_bk.add(limit_panel);
        panel_bk.add(top_panel);
        panel_bk.add(rule_panel);
        add(panel_bk);
    }


    private void addListener() {
        easy_button.addActionListener(e -> new EasyGame(this.user_name,false));

        normal_button.addActionListener(e -> new NormalGame(this.user_name,false));

        hard_button.addActionListener(e -> new HardGame(this.user_name,false));

        limit_button.addActionListener(e -> limit());
        top_button.addActionListener(e -> {
            try {
                top();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        rule_button.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "通过鼠标点击移动，图片复原时即为游戏胜利。",
                "游戏规则",JOptionPane.PLAIN_MESSAGE));
    }


    public void top() throws IOException {

        JFrame frame = new JFrame("排行榜");
        frame.setSize(250, 400);
        frame.setLayout(new GridLayout(11,1));

        JPanel[] bk_panels = new JPanel[11];
        for(int i = 0; i < 11; i++) {
            bk_panels[i] = new JPanel();
        }
        JLabel label = new JLabel("top   user    score");
        bk_panels[0].add(label);
        frame.add(bk_panels[0]);
        JLabel[] labels = new JLabel[10];
        for(int i = 0; i < 10; i++) {
            labels[i] = new JLabel();
        }
        BufferedReader bf = new BufferedReader(new FileReader("top.txt"));
        for(int i = -1; i < 10; i++) {
            if(i == -1) {
                bf.readLine();
            }else {
                String s = bf.readLine();
                if(s != null) {
                    labels[i].setText("" + (i+1)  + "   "+ s);
                    bk_panels[i + 1].add(labels[i]);
                    frame.add(bk_panels[i + 1]);
                }else break;
            }

        }


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void  limit()
    {
        JFrame frame = new JFrame();
        frame.setSize(200, 300);

        JPanel panel_bk = new JPanel();
        panel_bk.setLayout(new GridLayout(3,1));
        JPanel easy_panel = new JPanel();
        JPanel normal_panel = new JPanel();
        JPanel hard_panel = new JPanel();


        easy_button = new JButton("简单模式");
        normal_button = new JButton("普通模式");
        hard_button = new JButton("困难模式");

        easy_panel.add(easy_button);
        normal_panel.add(normal_button);
        hard_panel.add(hard_button);

        panel_bk.add(easy_panel);
        panel_bk.add(normal_panel);
        panel_bk.add(hard_panel);

        frame.add(panel_bk);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        easy_button.addActionListener(e -> new EasyGame(this.user_name,true));

        normal_button.addActionListener(e -> new NormalGame(this.user_name,true));

        hard_button.addActionListener(e -> new HardGame(this.user_name,true));
    }
}

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HardGame extends JFrame{
    int step_counts;
    JLabel step_label;
    int stopwatch;
    JLabel stopwatch_label;
    int score;
    JLabel score_label;
    GameTimer gameTimer;
    TopPanel topPanel;
    GamePanel gamePanel;
    String user_name;
    boolean limit;
    public HardGame(String user_name,boolean limit) {
        this.user_name = user_name;
        this.limit = limit;
        setSize(1000,570);
        setLayout(new BorderLayout());
        //加入菜单面板和主面板
        addComponentNorth();
        addComponentCenter();
        addActionListener();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public void addComponentNorth() {       //菜单面板，包含选项及游戏记录
        topPanel = new HardGame.TopPanel();
        this.add(BorderLayout.NORTH,topPanel);
    }

    public void addComponentCenter() {     //主面板，包含拼图区域和浏览区域
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        //创建拼图区和预览区
        int num = (int) (Math.random()*4);
        gamePanel = new HardGame.GamePanel("img\\person" + num +".jpg");
        PreviewPanel preview_panel = new PreviewPanel("img\\person" + num +".jpg");
        //在主面板中加入拼图区和预览区
        panel.add(gamePanel);
        panel.add(preview_panel);
        //将主面板加入顶层容器中
        this.add(BorderLayout.CENTER,panel);
    }

    public void addActionListener() {
        this.topPanel.start_button.addActionListener(e -> start());
    }

    public void start() {

        this.score = 0;
        this.step_counts = 0;
        this.stopwatch = 0;
        step_label.setText("步数：" + step_counts);
        stopwatch_label.setText("时间：" + stopwatch + "s");
        score_label.setText("得分：");
        gameTimer = new HardGame.GameTimer(limit);
        this.gamePanel.createGame();
        for(int i = 0; i < gamePanel.rank* gamePanel.rank - 1; i++) {
            this.gamePanel.buttons[i].addActionListener(this.gamePanel);
        }
        gameTimer.start();
        this.repaint();
    }

    //主面板设计
    class GamePanel extends JLabel implements ActionListener {

        Image[] split_images;
        ArrayList<JButton> images_list = new ArrayList<>();
        int rank = 6;
        int image_width;
        int image_height;
        JButton[] buttons;
        String picture_path;
        JButton gray_button;
        JButton clicked_button;
        public GamePanel(String picture_path) {
            this.setBorder(new TitledBorder("拼图区"));
            this.picture_path = picture_path;
            //分割图像
            splitImage(picture_path);
        }

        public void splitImage(String picture_path) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(picture_path));
                this.split_images = new Image[rank*rank];
                this.image_width = bufferedImage.getWidth()/rank;     //分割 单元图像宽度
                this.image_height = bufferedImage.getHeight()/rank;   //分割 单元图像高度
                for(int y = 0; y < rank; y++){
                    for(int x =0; x < rank; x++){
                        //分割模型
                        CropImageFilter cropImageFilter = new CropImageFilter(
                                x*image_width,y*image_height,image_width,image_height);
                        //将分割后产生的新图片保存到一个一维数组中
                        this.split_images[y*rank+x] = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(bufferedImage.getSource(),cropImageFilter));

                    }
                }
                //用分割好的图像创建按钮
                buttons = new JButton[rank*rank];
                for(int i = 0; i < rank*rank -1; i++) {     //此处已处理，只对rank*rank - 1个按钮初始化
                    buttons[i] = new JButton(new ImageIcon(split_images[i]));
                    buttons[i].setSize(image_width,image_height);
                }

                //将按钮写入集合，用集合的方式打乱分割后的图像
                for(int i = 0; i < rank*rank -1; i++) {
                    images_list.add(buttons[i]);
                }

                /*打乱图像的顺序
                  在这里要判断序列，拼图随机打乱存在无解的情况
                  序列中存在的逆序为偶数时，拼图才可被正确还原
                 */
                Collections.shuffle(images_list);

                while(true) {
                    int[] var = new int[rank*rank - 1];
                    for(int i = 0; i < rank*rank-1; i++){
                        for(int j = 0; j < rank*rank-1; j++){
                            if(images_list.get(i).equals(buttons[j])) {
                                var[i] = j;
                                System.out.print(var[i] + " ");
                            }
                        }
                    }
                    System.out.println();
                    int count = 0;
                    for(int i = 0; i < rank*rank-2; i++) {
                        for(int j = i+1; j < rank*rank-1; j++) {
                            if(var[i] > var[j]) {
                                count++;
                            }
                        }
                    }
                    System.out.println(count);
                    if(count%2 == 0) break;
                    Collections.shuffle(images_list);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void createGame() {
            this.setLayout(null);

            for(int i = 0; i < rank*rank -1; i++) {
                images_list.get(i).setLocation(20+image_width*(i%rank),20+image_height*(i/rank));
                this.add(images_list.get(i));
            }
            buttons[rank*rank-1] = new JButton();
            buttons[rank*rank-1].setBackground(new Color(0xd9d9d9));
            buttons[rank*rank-1].setSize(image_width,image_height);
            buttons[rank*rank-1].setLocation(20+image_width*(rank-1),20+image_height*(rank-1));
            this.add(buttons[rank*rank-1]);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gray_button = buttons[rank*rank-1];
            int gray_button_x = gray_button.getBounds().x;
            int gray_button_y = gray_button.getBounds().y;
            clicked_button = (JButton) e.getSource();
            int clicked_button_x = clicked_button.getBounds().x;
            int clicked_button_y = clicked_button.getBounds().y;
            if(clicked_button_x == gray_button_x && clicked_button_y - gray_button_y == -image_height) {
                swap("up");      //UP
            } else if(clicked_button_x == gray_button_x && clicked_button_y - gray_button_y == image_height){
                swap("down");      //DOWN
            }else if(clicked_button_y == gray_button_y && clicked_button_x - gray_button_x == image_width){
                swap("right");      //RIGHT
            }else if(clicked_button_y == gray_button_y && clicked_button_x - gray_button_x == -image_height){
                swap("left");      //LEFT
            }else return;
            this.repaint();
            step_counts++;
            step_label.setText("步数：" + step_counts);
            if(isFinish()) {
                gameTimer.stopRunning();
                score = 100 - step_counts/10 - stopwatch/10;
                score_label.setText("得分：" + score);
                JOptionPane.showMessageDialog(null,"恭喜你，游戏胜利！",
                        "提示",JOptionPane.PLAIN_MESSAGE);
                for(int i = 0; i < gamePanel.rank* gamePanel.rank - 1; i++) {
                    this.buttons[i].removeActionListener(this);
                }
                //设计排行榜写入文件
                FileWriter fw = null;
                try {
                    fw = new FileWriter("top.txt", true);
                    String record = "" + "    " + user_name + "    " + score;
                    fw.write("\n"+ record);
                } catch (Exception m) {
                    m.printStackTrace();
                } finally {
                    if (fw != null)
                        try {
                            fw.close();
                        } catch (IOException m) {
                            m.printStackTrace();
                        }
                }
            }
        }

        //交换按钮
        public void swap(String direction) {
            if("up".equals(direction)) {
                clicked_button.setLocation(clicked_button.getBounds().x,clicked_button.getBounds().y += image_height);
                gray_button.setLocation(gray_button.getBounds().x,gray_button.getBounds().y -= image_height);
            }else if("down".equals(direction)) {
                clicked_button.setLocation(clicked_button.getBounds().x,clicked_button.getBounds().y -= image_height);
                gray_button.setLocation(gray_button.getBounds().x,gray_button.getBounds().y += image_height);
            }else if("right".equals(direction)) {
                clicked_button.setLocation(clicked_button.getBounds().x -= image_width,clicked_button.getBounds().y);
                gray_button.setLocation(gray_button.getBounds().x += image_width,gray_button.getBounds().y);
            }else if("left".equals(direction)) {
                clicked_button.setLocation(clicked_button.getBounds().x += image_width,clicked_button.getBounds().y);
                gray_button.setLocation(gray_button.getBounds().x -= image_width,gray_button.getBounds().y);
            }
        }

    }

    //菜单面板设计
    class TopPanel extends JPanel {      //开始按钮及游戏记录

        JButton start_button;

        public TopPanel() {
            //初始化组件布局
            setBorder(new TitledBorder("菜单区"));
            setLayout(new GridLayout(1,4));
            JPanel panel1 =new JPanel();
            JPanel panel2 =new JPanel();
            JPanel panel3 =new JPanel();
            JPanel panel4 =new JPanel();
            step_counts = 0;
            stopwatch = 0;
            score = 0;
            this.start_button = new JButton("开始");
            step_label = new JLabel("步数：" + step_counts);
            stopwatch_label = new JLabel("时间：" + stopwatch + "s");
            score_label = new JLabel("得分：");
            panel1.add(start_button);
            panel2.add(step_label);
            panel3.add(stopwatch_label);
            panel4.add(score_label);
            add(panel1);
            add(panel2);
            add(panel3);
            add(panel4);
            setBackground(new Color(0xBEB3B3));
        }


    }

    //游戏计时器
    class GameTimer extends Thread {
        boolean flag = true;
        boolean limit;

        public GameTimer(boolean limit) {
            this.limit = limit;
        }

        public void run() {
            try {
                if(!limit) {
                    while(flag) {
                        sleep(1000);
                        stopwatch++;
                        stopwatch_label.setText("时间：" + stopwatch + "s");
                    }
                }else {
                    while(flag) {
                        sleep(1000);
                        stopwatch++;
                        if(stopwatch > 200){
                            JOptionPane.showMessageDialog(null, "很遗憾，你没有完成！",
                                    "游戏结束",JOptionPane.PLAIN_MESSAGE);
                            stopRunning();
                            for(int i = 0; i < gamePanel.rank* gamePanel.rank - 1; i++) {
                                gamePanel.buttons[i].removeActionListener(gamePanel);
                            }
                        }
                        stopwatch_label.setText("时间：" + stopwatch + "s");
                    }
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        //终止计时器线程
        public void stopRunning() {
            flag = false;
        }
    }


    //判断游戏是否结束
    public boolean isFinish() {
        for(int i = 0; i < (this.gamePanel.rank * this.gamePanel.rank - 1); i++) {
            int x = this.gamePanel.buttons[i].getBounds().x;
            int y = this.gamePanel.buttons[i].getBounds().y;
            if(x != 20 + this.gamePanel.image_width*(i%this.gamePanel.rank)
                    || y != 20 + this.gamePanel.image_height*(i/this.gamePanel.rank))
                return false;
        }
        return true;
    }
}
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PreviewPanel extends JPanel{
    //图片预览区设计
    String picture_path;

    public PreviewPanel(String picture_path) {
        this.picture_path = picture_path;
        setBorder(new TitledBorder("预览区"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(picture_path));
            g.drawImage(bufferedImage,20,20,null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

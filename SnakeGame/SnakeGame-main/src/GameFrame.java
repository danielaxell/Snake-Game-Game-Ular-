//Main Frame
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
    final  static Image icon = new ImageIcon("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/baru.jpg").getImage();
    GameFrame(){
        add(new GamePanel());

        setTitle("Snake Game");
        setSize(1200,600);
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


    }
}

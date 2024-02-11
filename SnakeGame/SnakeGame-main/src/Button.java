/* Button class

 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Button extends JButton implements MouseListener{

    Button(String text){
        setText(text);
        setFont(new Font("RETROPIX", Font.PLAIN, 20));
        setFocusable(false);
        setBackground(new Color(255, 166, 57));
        setForeground(Color.WHITE);
        addMouseListener(this);
       // setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Audio clicked = new Audio("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/sounds/click.wav");
        clicked.audio.start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

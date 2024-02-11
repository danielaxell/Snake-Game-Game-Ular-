import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameOption extends JFrame {
    public GameOption() {
        setTitle("Game Option");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/baru.jpg").getImage());

        JPanel panel = new JPanel(new GridBagLayout());
        Color backgroundColor = new Color(93, 56, 145);
        panel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel newGameLabel = new JLabel("MAIN MENU");
        newGameLabel.setFont(new Font("Retropix", Font.PLAIN, 85));
        newGameLabel.setForeground(Color.WHITE);

        JButton newGameButton = createStyledButton("NEW GAME");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tutup GameOption
                dispose();

                // Buka dan tampilkan SnakeGame
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        SnakeGame snakeGame = new SnakeGame();
                        new GameFrame();
                        snakeGame.setVisible(true);
                    }
                });
            }
        });

        JButton scoreButton = createStyledButton("SCORES");
        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tutup GameOption
                dispose();

                // Buka HighScores
                showHighScores();
            }
        });

        JButton quitButton = createStyledButton("EXIT");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit this game?", "Yes", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(newGameLabel, gbc);

        gbc.gridy = 1;
        panel.add(newGameButton, gbc);

        gbc.gridy = 2;
        panel.add(scoreButton, gbc);

        gbc.gridy = 3;
        panel.add(quitButton, gbc);

        add(panel);
        setVisible(true);
    }


    private void showHighScores() {
    new HighScores();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new java.awt.Dimension(250, 100));
        button.setFont(new Font("Retropix", Font.PLAIN, 50));
        button.setBackground(new Color(255, 166, 57));
        button.setForeground(Color.WHITE);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameOption();
        });
    }
}

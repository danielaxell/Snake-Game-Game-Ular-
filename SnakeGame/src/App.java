import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

public class App extends JFrame {
    public App() {
        setTitle("Selamat Datang Di Game Ular");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.5; // Set bobot gambar ular

        // Menambahkan gambar ular di tengah
        ImageIcon snakeImage = new ImageIcon("icon-ular.png");
        JLabel imageLabel = new JLabel(snakeImage);
        panel.add(imageLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.1; // Set bobot judul
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Game Ular");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.1; // Set bobot tombol
        gbc.anchor = GridBagConstraints.CENTER;

        JButton startButton = new JButton("Mulai Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGamePage();
            }
        });
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.RED);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setOpaque(true);
        startButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(150, 50));
        panel.add(startButton, gbc);

        add(panel);
        setVisible(true);
        Audio clicked = new Audio("Theme.wav");
        clicked.audio.start();
    }

    private void openGamePage() {
        // Mendapatkan seluruh perangkat grafis
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        // Menggunakan perangkat pertama (utama) untuk mendapatkan ukuran layar
        GraphicsDevice primaryDevice = devices[0];
        Dimension screenSize = primaryDevice.getDefaultConfiguration().getBounds().getSize();

        // Mengatur ukuran frame sesuai dengan ukuran layar
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        JFrame frame = new JFrame("Game Ular");
        frame.setSize(screenWidth, screenHeight); // Mengatur ukuran frame
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameUlar gameUlar = new GameUlar(screenWidth, screenHeight);
        gameUlar.setBackground(Color.BLACK);
        frame.add(gameUlar);
        frame.pack();
        gameUlar.requestFocus();

        setVisible(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App();
            }

        });
    }
}

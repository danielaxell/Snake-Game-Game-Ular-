import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class App extends JFrame {
    public App() {
        setTitle("Snake Games");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/baru.jpg").getImage());

        JPanel panel = new JPanel(new GridBagLayout());
        Color backgroundColor = new Color(93, 56, 145);
        panel.setBackground(backgroundColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1; // Set bobot gambar ular
        gbc.anchor = GridBagConstraints.CENTER;

        // Menambahkan gambar ular di tengah
        ImageIcon snakeImage = new ImageIcon("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/logorawr.png");
        Image originalImage = snakeImage.getImage(); // Ambil gambar asli
        Image resizedImage = originalImage.getScaledInstance(900, 350, Image.SCALE_SMOOTH);
        // Buat ImageIcon baru dengan gambar yang sudah diperkecil
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel imageLabel = new JLabel(resizedIcon); // Menggunakan resizedIcon
        panel.add(imageLabel, gbc);

        // Menambahkan tombol "START" di bawah gambar ular
        gbc.gridy = 1;
        gbc.weighty = 0.1; // Set bobot tombol
        gbc.insets = new java.awt.Insets(5, 0, 100, 0); // Menambahkan jarak di atas tombol
        gbc.anchor = GridBagConstraints.CENTER;

        JButton startButton = new JButton("START");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGamePage();
            }
        });
        startButton.setBackground(new Color(255, 166, 57)); // Warna latar belakang
        startButton.setForeground(Color.WHITE); // Warna teks
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setOpaque(true);
        startButton.setFont(new Font("Retropix", Font.BOLD, 58));
        startButton.setPreferredSize(new Dimension(250, 90));
        panel.add(startButton, gbc);

        add(panel);
        setVisible(true);
        Audio clicked = new Audio("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/sounds/musik.wav");
        clicked.audio.start();
    }

    private void openGamePage() {
        setVisible(false);

        // Mendapatkan seluruh perangkat grafis
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        // Menggunakan perangkat pertama (utama) untuk mendapatkan ukuran layar
        GraphicsDevice primaryDevice = devices[0];
        Dimension screenSize = primaryDevice.getDefaultConfiguration().getBounds().getSize();

        // Mengatur ukuran frame sesuai dengan ukuran layar
        screenSize.getWidth();
        screenSize.getHeight();

        JFrame frame = new JFrame("SNAKE GAME");
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); // Menambahkan baris ini untuk menghentikan pengguna untuk mengubah ukuran frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameOption gameOption = new GameOption();
        gameOption.setBackground(Color.BLACK);
        frame.add(gameOption);
        frame.pack();
        gameOption.requestFocus();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }
}

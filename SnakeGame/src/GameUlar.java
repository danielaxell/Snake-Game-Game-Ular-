// Import paket yang diperlukan
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.BorderFactory;

// Antarmuka Drawable untuk menggambar objek pada layar
interface Drawable {
    void draw(Graphics g, int tileSize);
}

// Kelas Food yang mengimplementasikan Drawable
class Food implements Drawable {
    int x;
    int y;

    // Konstruktor untuk inisialisasi posisi Food
    Food(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Metode untuk menempatkan Food secara acak
    void placeFood(int boardWidth, int boardHeight) {
        Random random = new Random();
        x = random.nextInt(boardWidth);
        y = random.nextInt(boardHeight);
    }

    // Implementasi metode draw dari antarmuka Drawable
    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.red);
        g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}

// Kelas Tile yang merupakan subclass dari Food
class Tile extends Food {
    Tile(int x, int y) {
        super(x, y);
    }
}

// Kelas utama GameUlar yang merupakan JPanel dan mengimplementasikan ActionListener dan KeyListener
public class GameUlar extends JPanel implements ActionListener, KeyListener {
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    private Drawable food; // Menggunakan antarmuka Drawable

    Random random;

    Timer gameLoop;
    int velocityX;
    int velocityY;
    JButton retryButton;
    boolean gameOver = false;

    // Konstruktor GameUlar
    GameUlar(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        // Inisialisasi objek snakeHead dan snakeBody
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        // Inisialisasi objek food sebagai Food
        food = new Food(10, 10);
        random = new Random();
        ((Food) food).placeFood(boardWidth / tileSize, boardHeight / tileSize);

        velocityX = 0;
        velocityY = 0;

        // Konfigurasi tombol Retry
        retryButton = new JButton("Coba Lagi");
        retryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        retryButton.setBackground(Color.BLUE);
        retryButton.setForeground(Color.WHITE);
        retryButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20)); // Font dan ukuran yang baru
        retryButton.setPreferredSize(new Dimension(150, 75)); // Ukuran tombol yang baru
        retryButton.setFocusPainted(false);
        retryButton.setBorderPainted(false);
        retryButton.setOpaque(true);
        retryButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        retryButton.setVisible(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2; // Menempatkan tombol di baris ke-2
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(retryButton, gbc);

        // Inisialisasi game loop dengan ActionListener
        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    // Metode paintComponent untuk menggambar elemen-elemen game
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

        if (gameOver) {
            retryButton.setVisible(true);
        }
    }

    // Metode untuk menggambar elemen-elemen game
    public void draw(Graphics g) {
        // Menggambar grid pada layar
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Menggambar Food menggunakan metode draw dari antarmuka Drawable
        if (food instanceof Drawable) {
            ((Drawable) food).draw(g, tileSize);
        }

        // Menggambar snakeHead dan snakeBody
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        for (Tile snakePart : snakeBody) {
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        // Menampilkan skor atau pesan game over
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Anda Menabrak, Skor anda :" + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);

            // Menentukan posisi tengah
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            // Menggambar teks di posisi tengah


            retryButton.setVisible(true);

        } else {
            g.drawString("SKOR :" + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    // Metode untuk menempatkan Food secara acak
    public void placeFood() {
        ((Food) food).placeFood(boardWidth / tileSize, boardHeight / tileSize);
    }

    // Metode untuk mendeteksi tabrakan antara snakeHead dan Food
    public boolean collision(Tile tile, Food food) {
        return tile.x == food.x && tile.y == food.y;
    }

    // Metode untuk menggerakkan snake
    public void move() {
        if (collision(snakeHead, (Food) food)) {
            snakeBody.add(new Tile(((Food) food).x, ((Food) food).y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Deteksi tabrakan antara snakeHead dan bagian tubuh snake
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        // Deteksi jika snakeHead keluar dari layar
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth ||
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
            gameOver = true;
        }
    }

    // Metode untuk mengulang permainan
    public void restartGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        retryButton.setVisible(false);
        gameLoop.restart();
    }

    // Implementasi ActionListener untuk game loop
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    // Implementasi KeyListener untuk mengendalikan arah snake
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    // Implementasi KeyListener (tidak digunakan)
    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Implementasi KeyListener (tidak digunakan)
    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Metode main untuk menjalankan permainan
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Ular");
        GameUlar game = new GameUlar(1650, 710);
        frame.add(game);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maksimalkan frame
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}



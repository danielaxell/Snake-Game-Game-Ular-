import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

    static final Button start = new Button("START"), save = new Button("SAVE"), new_game = new Button("NEW GAME"), quit = new Button("MAIN MENU"), pausee = new Button("PAUSE"), play = new Button("PLAY");
    static final JComboBox<String> mode = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
    static final int Width = 900;
    static final int Height = 600;
    static final int UNIT_Size = 35;
    static final int Game_Unit = (Width*Height)/ UNIT_Size;
    int delay = 150;         //Kontrol Kecepatan Ular
    final int x [] = new int[50];          //Posisi Ulra pada sumbu x
    final int y[] = new int[50];          //Posisi Ulra pada sumbu y
    int bodyParts = 5;                   //Badan Ular
    int fruitX, fruitY;                 //Posisi Makanan
    Image food;                        //Gambar Makanan
    char direction = 'R';             //Kontrol Arah
    boolean running, pause = false;
    int Score = 0;                  //Skor
    Timer timer;
    final static Image logo = new ImageIcon("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/baru.jpg").getImage();

    GamePanel(){
        setPreferredSize(new Dimension(1200, Height));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        setBackground(new Color(93, 56, 145));
        setLayout(null);

        //Buttons action listener
        start.addActionListener(this);
        new_game.addActionListener(this);
        save.addActionListener(this);
        quit.addActionListener(this);
        pausee.addActionListener(this);
        play.addActionListener(this);
        mode.addActionListener(this);

        //Buuttons Positions
        start.setBounds(1000, 100,150,50);
        pausee.setBounds(1000, 175,150,50);
        play.setBounds(1000, 175,150,50);
        new_game.setBounds(1000, 250,150,50);
        save.setBounds(1000, 325,150,50);
        quit.setBounds(1000,400,150,50);
        mode.setBounds(1000,475,150,50);
        mode.setFont(new Font("Retropix", Font.PLAIN, 16));
        mode.setForeground(Color.white);
        mode.setBackground(new Color(255, 153, 2));
        mode.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });


        new_game.setFont(new Font("Retropix", Font.PLAIN, 18));
        new_game.setVisible(false);
        pausee.setVisible(false);
        play.setVisible(false);

        add(start);
        add(save);
        add(new_game);
        add(quit);
        add(pausee);
        add(play);
        add(mode);
    }

    //Metode Start Game
    public void startGame(){
        newfruit();
        running  = true;
        timer = new Timer(delay, this);
        timer.start();
        start.setVisible(true);
        pausee.setVisible(true);
        new_game.setVisible(true);
        quit.setVisible(true);
        mode.setVisible(true);
    }

    //Metode New Game
    public void New_game(){
        timer.stop();
        running = false;
        Score = 0;
        bodyParts = 5;
        Arrays.fill(x, 0);
        Arrays.fill(y, 0);
        direction = 'R';
        start.setVisible(false);
        startGame();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;
        //Rows Drawing Screen
        for(int i=0; i<Width/UNIT_Size+1; i++) {
            for (int j = 0; j < Height/UNIT_Size; j++) {
                if ((i+j) % 2 == 0)
                    gd.setColor(new Color(92, 75, 153));
                else
                    gd.setColor(new Color(160, 132, 232));

                gd.fillRect(i * UNIT_Size, j * UNIT_Size, UNIT_Size, UNIT_Size);

            }
        }
        mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMode = (String) mode.getSelectedItem();
                switch (Objects.requireNonNull(selectedMode)) {
                    case "Easy":
                        delay = 250;
                        break;
                    case "Medium":
                        delay = 150;
                        break;
                    case "Hard":
                        delay = 75;
                        break;
                }
                if (running) {
                    timer.stop();
                    timer.setDelay(delay);
                    timer.start();
                }
                requestFocusInWindow();
            }
        });



        //Pembatas Garis antara wilayah Game dan field
        gd.setColor(Color.red);
        gd.setStroke(new BasicStroke(3));
        gd.drawLine(Width+10,0,Width+10,Height);

        //Logika Penggambaran Ular
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0 ){
                gd.setColor(new Color(248, 55, 55));
                gd.fillRect(x[i], y[i], UNIT_Size - 3, UNIT_Size - 3);
                gd.setColor(Color.white);
                gd.setStroke(new BasicStroke(3));
                gd.drawOval(x[i] + 4, y[i] + 8, 8, 8);
                gd.drawOval(x[i] + 20, y[i] + 8, 8, 8);
                gd.setStroke(new BasicStroke(2));
                gd.drawLine(x[i] + 4, y[i] + 25, x[i] + 28, y[i] + 25);
            } else {
                gd.setColor(new Color(255, 153, 2));
                gd.fillRect(x[i], y[i], UNIT_Size - 3, UNIT_Size - 3);

            }
        }

        //Metode Penggambaran Makanan ular
        gd.drawImage(food, fruitX, fruitY, null);

        //Score Updating
        gd.setColor(new Color(255, 255, 255));
        gd.setFont(new Font(Font.MONOSPACED, Font.BOLD, 35));
        gd.drawString("SCORE:" + Score, 1000,40);

        gd.drawImage(logo, 880,0,null);

        if(!running && new_game.isVisible()){
            Gameover(g);
        }
    }

    //Metode gerakan ular
    public  void move(){
        for(int i=bodyParts; i>0; i--){
            x[i]  = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'R':
                x[0] = x[0] + UNIT_Size;
                break;
            case 'L':
                x[0] = x[0] - UNIT_Size;
                break;
            case 'U':
                y[0] = y[0] - UNIT_Size;
                break;
            case 'D':
                y[0] = y[0] + UNIT_Size;
                break;
        }

    }

    //Metode Makanan Ular
    public void newfruit() {
        final String[] Food_Images = new String[]{"C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_orange.png",
                "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_fruit.png", "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_cherry.png",
                "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_berry.png", "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_coconut_.png",
                "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_peach.png", "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_watermelon.png",
                "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_orange.png",
                "C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/ic_pomegranate.png"};
        Random random = new Random();
        fruitX = random.nextInt(Width / UNIT_Size) * UNIT_Size;
        fruitY = random.nextInt(Height / UNIT_Size) * UNIT_Size;
        food = new ImageIcon(Food_Images[random.nextInt(9)]).getImage().getScaledInstance(35,35,5);

    }

    //Metode pengecekan apakah ular memakanan makanan
    public void checkFruit(){
        if(x[0]==fruitX && y[0] ==fruitY){
            Audio clicked = new Audio("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/sounds/eat.wav");
            clicked.audio.start();
            newfruit();
            bodyParts++;
            Score+=1;
        }

    }
    //Memeriksa kondisi apakah game over
    public void checkCollision(){
        for(int i=bodyParts; i>0; i--) {
            //If Body Touches itself
            if (x[0] == x[i] && y[0] == y[i])
                running = false;

            //apabila menabrak dinding
            if (x[0] >= Width - 35)        //Right Border
                running = false;
            if (x[0] < 0)              //Left Border
                running = false;
            if (y[0] < 0)              //Up Border
                running = false;
            if (y[0] >= Height)
                running = false;    //Down Border

            if (!running) {
                Audio clicked = new Audio("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/sounds/die.wav");
                clicked.audio.start();
                timer.stop();




            }
        }
    }

    //Pesan Game Over
    public void Gameover(Graphics g){
        g.setColor(new Color(255, 222, 89));
        g.setFont(new Font("comic sans ms", Font.BOLD, 75));
        FontMetrics fontMetrics  = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (Width-fontMetrics.stringWidth("Game Over"))/2, (int) ((Height-fontMetrics.stringWidth("Game Over"))/1.2));
    }
    //Logika Tombol Play
    void play(){
        play.setVisible(false);
        pausee.setVisible(true);
        timer.start();

    }
    //Logika Tombol Pause
    void pause() {
        play.setVisible(true);
        pausee.setVisible(false);
        timer.stop();

    }

    void audio(){
        Audio clicked = new Audio("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/sounds/click.wav");
        clicked.audio.start();
    }

    //Metode Action Performed
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkCollision();
            checkFruit();
        }
        repaint();

        if(e.getSource() == start)
            startGame();

        if(e.getSource() == new_game){
            New_game();
            repaint();
        }

        if (e.getSource() == pausee)
            pause();

        if (e.getSource() == play)
            play();

        if (e.getSource() == quit) {
            quit();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit this game?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Tombol "Quit" ditekan, kembali ke halaman GameOption
                backToGameOption();

            }
        }

        if (e.getSource() == save) {
            save();

            // Meminta pengguna untuk memasukkan nama
            String playerName = JOptionPane.showInputDialog("Enter your name:");

            // Pastikan nama tidak kosong dan pengguna tidak membatalkan dialog
            if (playerName != null && !playerName.isEmpty()) {
                if (!isPlayerNameExists(playerName)) {
                    // Simpan skor ke database
                    DatabaseManager dbManager = new DatabaseManager();
                    dbManager.saveScore(playerName, Score);
                    JOptionPane.showMessageDialog(this, "Your score has been saved!", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "The name you entered already exists. " +
                                    "Do you sure you want to change the score?",
                            "Confirm",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        DatabaseManager dbManager = new DatabaseManager();

                        dbManager.getCurrentScore(playerName);
                        dbManager.updateScore(playerName, Score);
                        ImageIcon originalIcon = new ImageIcon("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/baru.jpg");

                        // Mengambil gambar asli dari ImageIcon
                        Image originalImage = originalIcon.getImage();

                        // Meresize gambar sesuai kebutuhan
                        Image resizedImage = originalImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

                        // Membuat ImageIcon dengan gambar yang sudah diresize
                        ImageIcon resizedIcon = new ImageIcon(resizedImage);

                        // Membuat JLabel dengan gambar yang sudah diresize
                        JLabel label = new JLabel("Your score has been updated!", resizedIcon, JLabel.CENTER);

                        // Menampilkan JOptionPane dengan JLabel
                        JOptionPane.showMessageDialog(this, label, "Information", JOptionPane.INFORMATION_MESSAGE);
                    } else {

                        JOptionPane.showMessageDialog(this, "Score not changed.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }

    private boolean isPlayerNameExists(String playerName) {
        try {
            // Database connection details
            String url = "jdbc:mysql://localhost:3306/gameular";
            String username = "root"; // Ubah sesuai dengan pengaturan MySQL Anda
            String password = ""; // Kosongkan jika tidak ada password

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            // Check if the player name already exists in the database
            String query = "SELECT COUNT(*) FROM skor_tertinggi WHERE Nama = '" + playerName + "'";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            int count = resultSet.getInt(1);

            resultSet.close();
            statement.close();
            connection.close();

            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // menampilkan pesan error
            return false;
        }
    }
    private void quit() {


    }
    private void backToGameOption() {
        // Tutup GamePanel
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        currentFrame.dispose();

        // Buka GameOption
        JFrame gameOptionFrame = new GameOption();
        gameOptionFrame.setVisible(true);







    }


    private void save() {
    }

    //kontrol keyboard
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction!='R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L')
                        direction = 'R';
                    break;

                case KeyEvent.VK_UP:
                    if(direction!='D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U')
                        direction = 'D';
                    break;
                case KeyEvent.VK_N:
                    if(!start.isVisible()) {
                        audio();
                        New_game();
                    }

                    break;
                case KeyEvent.VK_ENTER:
                    if(!new_game.isVisible()) {
                        audio();
                        startGame();
                    }
                    break;
                case KeyEvent.VK_P:
                    if(pausee.isVisible()){
                        audio();
                        pause();
                    }
                    else{
                        audio();
                        play();
                    }
                    break;
            }
        }
    }

}
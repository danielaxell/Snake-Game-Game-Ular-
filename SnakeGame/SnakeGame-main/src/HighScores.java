import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class HighScores extends JFrame {
    private JButton backButton;
    private JTable highScoresTable;
    private JButton deleteButton;
    private JTextField searchField;

    public HighScores() {
        setTitle("High Scores");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("C:/Users/FEBRIYAN/Documents/Intellij/snakegame/snakegame/snakegame/src/img/baru.jpg").getImage());

        JPanel panel = new JPanel(new GridBagLayout());
        Color backgroundColor = new Color(93, 56, 145);
        panel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("HIGHEST SCORE");
        titleLabel.setFont(new Font("Retropix", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 1;
        panel.add(titleLabel, gbc);

        try {
            String url = "jdbc:mysql://localhost:3306/gameular";
            String username = "root";
            String password = "";

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            String query = "SELECT Nama, Skor FROM skor_tertinggi ORDER BY Skor DESC";
            ResultSet resultSet = statement.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("Nama");
            model.addColumn("Skor");

            int position = 1;
            while (resultSet.next()) {
                String name = resultSet.getString("Nama");
                int score = resultSet.getInt("Skor");
                model.addRow(new Object[]{position, name, score});
                position++;
            }

            highScoresTable = new JTable(model);
            highScoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            highScoresTable.setBackground(new Color(255, 166, 57));
            highScoresTable.setForeground(Color.WHITE);
            highScoresTable.setFont(new Font("Retropix", Font.BOLD,14));
            highScoresTable.setPreferredScrollableViewportSize(new Dimension(700, 300));

            JScrollPane tableScrollPane = new JScrollPane(highScoresTable);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(tableScrollPane, gbc);

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setForeground(Color.WHITE);
            renderer.setFont(new Font("Retro pix", Font.PLAIN, 15));
            renderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < highScoresTable.getColumnCount(); i++) {
                highScoresTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve the highest score", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JLabel searchLabel = new JLabel("SEARCH SCORE");
        searchLabel.setFont(new Font("Retropix", Font.PLAIN, 16));
        searchLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets.top = 5;
        gbc.insets.left = 5;
        panel.add(searchLabel, gbc);
        // Menambahkan JTextField untuk pencarian
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 30));
        searchField.setFont(new Font("Retropix", Font.PLAIN, 16));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets.top = 5;
        panel.add(searchField, gbc);

        // Menambahkan listener ke JTextField untuk mencari data berdasarkan kata kunci
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        // Menambahkan tombol "Delete"
        deleteButton = new JButton("DELETE");
        deleteButton.setFont(new Font("Retropix", Font.PLAIN, 25));
        deleteButton.setBackground(new Color(255, 166, 57));
        deleteButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.insets.top = 10;
        panel.add(deleteButton, gbc);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = highScoresTable.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) highScoresTable.getModel();
                    String nameToDelete = (String) model.getValueAt(selectedRow, 1);

                    int confirmResult = JOptionPane.showConfirmDialog(
                            HighScores.this,
                            "Are you sure to delete this score data?",
                            "Yes, I'm sure",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmResult == JOptionPane.YES_OPTION) {
                        try {
                            String url = "jdbc:mysql://localhost:3306/gameular";
                            String username = "root";
                            String password = "";

                            Connection connection = DriverManager.getConnection(url, username, password);

                            String deleteQuery = "DELETE FROM skor_tertinggi WHERE Nama=?";
                            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                            preparedStatement.setString(1, nameToDelete);
                            preparedStatement.executeUpdate();

                            model.removeRow(selectedRow);

                            preparedStatement.close();
                            connection.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(HighScores.this, "Failed to delete the data", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(HighScores.this, "Select the rows to delete", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Menambahkan tombol "Kembali"
        backButton = new JButton("MAIN MENU");
        backButton.setFont(new Font("Retropix", Font.PLAIN, 19));
        backButton.setBackground(new Color(255, 166, 57));
        backButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.insets.top = 10;
        panel.add(backButton, gbc);

        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GameOption());
        });

        add(panel);

        setVisible(true);
    }

    private void filterTable() {
        String searchText = searchField.getText();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) highScoresTable.getModel());
        highScoresTable.setRowSorter(sorter);
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + searchText);
        sorter.setRowFilter(rowFilter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HighScores());
    }
}
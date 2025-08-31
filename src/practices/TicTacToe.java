package practices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe {

    private static String currentPlayer = "X";
    private static String currentWinner = null;
    private static boolean isDraw = false;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final Color BACKGROUND = Color.WHITE;
    private static final Color LINE_COLOR = Color.BLACK;

    private static String[][] grid = new String[3][3];
    private static JFrame frame;
    private static JPanel panel;
    private static JLabel statusLabel;

    public static void main(String[] args) {
        grid = new String[3][3]; // Initialize the grid with null values

        // Create the main frame
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT + 100);
        frame.setResizable(false);

        // Create the main panel
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGameBoard(g);
            }
        };
        panel.setBackground(BACKGROUND);
        panel.setLayout(null);

        // Add mouse listener to the panel for user clicks
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleUserClick(e);
            }
        });

        // Add status bar
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setBounds(0, HEIGHT, WIDTH, 100);
        statusLabel.setBackground(Color.BLACK);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        panel.add(statusLabel);

        frame.add(panel);
        frame.setVisible(true);

        initializeGameBoard();
        updateStatus();
    }

    private static void initializeGameBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = null;
            }
        }
    }

    private static void drawGameBoard(Graphics g) {
        // Draw grid lines
        g.setColor(LINE_COLOR);
        g.fillRect(WIDTH / 3, 0, 4, HEIGHT);
        g.fillRect(WIDTH / 3 * 2, 0, 4, HEIGHT);
        g.fillRect(0, HEIGHT / 3, WIDTH, 4);
        g.fillRect(0, HEIGHT / 3 * 2, WIDTH, 4);

        // Draw X's and O's
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row][col] != null) {
                    drawXO(g, row, col, grid[row][col]);
                }
            }
        }

        // Check if there's a winner or draw
        checkWinner(g);
    }

    private static void drawXO(Graphics g, int row, int col, String player) {
        int x = col * WIDTH / 3 + WIDTH / 6;
        int y = row * HEIGHT / 3 + HEIGHT / 6;
        int size = 80;

        if (player.equals("X")) {
            g.setColor(LINE_COLOR);
            g.drawLine(x - size / 2, y - size / 2, x + size / 2, y + size / 2);
            g.drawLine(x - size / 2, y + size / 2, x + size / 2, y - size / 2);
        } else {
            g.setColor(LINE_COLOR);
            g.drawOval(x - size / 2, y - size / 2, size, size);
        }
    }

    private static void updateStatus() {
        String message = "";
        if (currentWinner == null) {
            message = currentPlayer + "'s Turn";
        } else {
            message = currentWinner + " won!";
        }
        if (isDraw) {
            message = "Game Draw!";
        }
        statusLabel.setText(message);
    }

    private static void handleUserClick(MouseEvent e) {
        int col = e.getX() / (WIDTH / 3);
        int row = e.getY() / (HEIGHT / 3);

        if (row < 3 && col < 3 && grid[row][col] == null) {
            grid[row][col] = currentPlayer;
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
            panel.repaint();

            checkWinner(panel.getGraphics());

            if (currentWinner != null || isDraw) {
                resetGame();
            }
        }
    }

    private static void checkWinner(Graphics g) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] != null && grid[i][0].equals(grid[i][1]) && grid[i][0].equals(grid[i][2])) {
                currentWinner = grid[i][0];
                g.setColor(Color.RED);
                g.fillRect(0, (i + 1) * HEIGHT / 3 - HEIGHT / 6, WIDTH, 4);
                return;
            }

            if (grid[0][i] != null && grid[0][i].equals(grid[1][i]) && grid[0][i].equals(grid[2][i])) {
                currentWinner = grid[0][i];
                g.setColor(Color.RED);
                g.fillRect((i + 1) * WIDTH / 3 - WIDTH / 6, 0, 4, HEIGHT);
                return;
            }
        }

        // Check diagonals
        if (grid[0][0] != null && grid[0][0].equals(grid[1][1]) && grid[0][0].equals(grid[2][2])) {
            currentWinner = grid[0][0];
            g.setColor(Color.RED);
            g.drawLine(50, 50, 350, 350);
            return;
        }

        if (grid[0][2] != null && grid[0][2].equals(grid[1][1]) && grid[0][2].equals(grid[2][0])) {
            currentWinner = grid[0][2];
            g.setColor(Color.RED);
            g.drawLine(350, 50, 50, 350);
            return;
        }

        // Check for draw
        boolean isFull = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row][col] == null) {
                    isFull = false;
                    break;
                }
            }
        }

        if (isFull && currentWinner == null) {
            isDraw = true;
        }

        updateStatus();
    }

    private static void resetGame() {
        Timer timer = new Timer(3000, e -> {
            currentPlayer = "X";
            isDraw = false;
            currentWinner = null;
            initializeGameBoard();
            panel.repaint();
            updateStatus();
        });
        timer.setRepeats(false);
        timer.start();
    }
}

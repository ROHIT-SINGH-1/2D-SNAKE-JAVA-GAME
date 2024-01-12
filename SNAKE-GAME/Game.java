import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Rectangle2D;

public class Game extends JFrame implements ActionListener, KeyListener {
    private final int PANEL_WIDTH = 850;
    private final int PANEL_HEIGHT = 500;
    private final int UNIT_SIZE = 25;
    private final int GAME_UNITS = (PANEL_WIDTH * PANEL_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100; // Adjust this value as needed

    private ArrayList<Point> SNAKE;
    private Point FOOD;
    private char DIRECTION = 'R';
    private boolean RUNNING = false;
    private Timer TIMER;
    private JLabel GAMEOVERLABEL;
    private JLabel COUNTDOWNLABEL;
    private JLabel SCORELABEL;
    private JLabel HIGHSCORELABEL;
    private JPanel GAMEPANEL;
    private int SCORE = 0;
    private int HIGHSCORE = 0;
    private int countdownValue = 3;
    private boolean PAUSED = false;
    private JLabel PAUSELABEL;

    public Game() {
        setTitle("SNAKE GAME");
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        GAMEPANEL = new JPanel();
        GAMEPANEL.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        GAMEPANEL.setBackground(Color.BLACK);
        GAMEPANEL.setLayout(null);
        add(GAMEPANEL);

        GAMEOVERLABEL = new JLabel("GAME OVER ");
        GAMEOVERLABEL.setBounds(PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 - 75, 200, 50);
        GAMEOVERLABEL.setFont(new Font("Arial", Font.BOLD, 30));
        GAMEOVERLABEL.setForeground(Color.RED);
        GAMEOVERLABEL.setHorizontalAlignment(SwingConstants.CENTER);
        GAMEOVERLABEL.setVisible(false);
        GAMEPANEL.add(GAMEOVERLABEL);

        COUNTDOWNLABEL = new JLabel("");
        COUNTDOWNLABEL.setBounds(PANEL_WIDTH / 2 - 120, PANEL_HEIGHT / 2 - 30, 240, 60);
        COUNTDOWNLABEL.setFont(new Font("Arial", Font.BOLD, 20));
        COUNTDOWNLABEL.setForeground(Color.RED);
        COUNTDOWNLABEL.setHorizontalAlignment(SwingConstants.CENTER);
        GAMEPANEL.add(COUNTDOWNLABEL);

        PAUSELABEL = new JLabel("PAUSED");
        PAUSELABEL.setBounds(PANEL_WIDTH / 2 - 50, PANEL_HEIGHT / 2 - 25, 100, 50);
        PAUSELABEL.setFont(new Font("Arial", Font.BOLD, 20));
        PAUSELABEL.setForeground(Color.YELLOW);
        PAUSELABEL.setHorizontalAlignment(SwingConstants.CENTER);
        PAUSELABEL.setVisible(false);
        GAMEPANEL.add(PAUSELABEL);

        SCORELABEL = new JLabel("SCORE: 0");
        SCORELABEL.setBounds(10, 10, 100, 30);
        SCORELABEL.setFont(new Font("Arial", Font.BOLD, 16));
        SCORELABEL.setForeground(Color.GREEN);
        GAMEPANEL.add(SCORELABEL);

        HIGHSCORELABEL = new JLabel("HIGH SCORE: " + HIGHSCORE);
        HIGHSCORELABEL.setBounds(PANEL_WIDTH - 150, 10, 150, 30);
        HIGHSCORELABEL.setFont(new Font("Arial", Font.BOLD, 16));
        HIGHSCORELABEL.setForeground(Color.GREEN);
        GAMEPANEL.add(HIGHSCORELABEL);

        addKeyListener(this);
        setFocusable(true);
        ImageIcon customIcon = new ImageIcon("RESOURCES\\ICON.png");
        setIconImage(customIcon.getImage());

        initGame();
        startGame();
        // startCountdown();
    }

    private void initGame() {
        SNAKE = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SNAKE.add(new Point(PANEL_WIDTH / 2 - i * UNIT_SIZE, PANEL_HEIGHT / 2));
        }
        spawnFood();
        TIMER = new Timer(DELAY, this);
        SCORE = 0;
        SCORELABEL.setText("SCORE: 0");
    }

private void spawnFood() {
    Random random = new Random();
    int x, y;
    do {
        x = random.nextInt(PANEL_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        y = random.nextInt(PANEL_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    } while (SNAKE.contains(new Point(x, y)));

    FOOD = new Point(x, y);
}

    private void move() {
        Point head = SNAKE.get(0);
        Point newHead = new Point(head);
    
        switch (DIRECTION) {
          case 'U':
            newHead.y -= UNIT_SIZE;
            break;
          case 'D':
            newHead.y += UNIT_SIZE;
            break;
          case 'L':
            newHead.x -= UNIT_SIZE;
            break;
          case 'R':
            newHead.x += UNIT_SIZE;
            break;
        }
    
        if (SNAKE.size() > GAME_UNITS) {
          stopGame();
          return;
        }
    
        if (newHead.equals(FOOD)) {
          SNAKE.add(0, newHead);
          spawnFood();
          SCORE += 10;
          SCORELABEL.setText("SCORE: " + SCORE);
        } else {
          SNAKE.remove(SNAKE.size() - 1);
          SNAKE.add(0, newHead);
        }
    
        if (newHead.x < 0 || newHead.x >= PANEL_WIDTH || newHead.y < 0 || newHead.y >= PANEL_HEIGHT
            || SNAKE.subList(1, SNAKE.size()).contains(newHead)) {
          stopGame();
          return;
        }
      }

      private void stopGame() {
        RUNNING = false;
        TIMER.stop();
        GAMEOVERLABEL.setVisible(true);
        COUNTDOWNLABEL.setVisible(false);
        SCORELABEL.setVisible(false);
        HIGHSCORELABEL.setVisible(true);
    
        if (SCORE > HIGHSCORE) {
            HIGHSCORE = SCORE;
            HIGHSCORELABEL.setText("HIGH SCORE: " + HIGHSCORE);
        }
    
        GAMEOVERLABEL.setText("GAME OVER");
    
        // Start countdown after a delay
        Timer delayTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Timer) e.getSource()).stop();
                startCountdown();
            }
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }
    
    private void startGame() {
        RUNNING = true;
        initGame();
        TIMER.start();
        GAMEOVERLABEL.setVisible(false);
        COUNTDOWNLABEL.setVisible(false);
        SCORELABEL.setVisible(true);
        HIGHSCORELABEL.setVisible(false);
    }
    private void startCountdown() {
        countdownValue = 3;
        COUNTDOWNLABEL.setText(Integer.toString(countdownValue));
        COUNTDOWNLABEL.setVisible(true);
    
        Timer countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdownValue > 1) {
                    countdownValue--;
                    COUNTDOWNLABEL.setText(Integer.toString(countdownValue));
                } else {
                    ((Timer) e.getSource()).stop();
                    COUNTDOWNLABEL.setVisible(false);
                    restartGame();
                }
            }
        });
    
        countdownTimer.setRepeats(true);
        countdownTimer.start();
    }
    
    private void restartGame() {
      initGame();
      startGame();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (RUNNING) {
            g.setColor(Color.RED);
            g.drawRect(FOOD.x, FOOD.y, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.GREEN);
            for (Point point : SNAKE) {
                g.drawRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }

            if (PAUSED) {
                Graphics2D g2d = (Graphics2D) g;
                FontMetrics fontMetrics = g2d.getFontMetrics();
                Rectangle2D bounds = fontMetrics.getStringBounds("PAUSED", g2d);

                int x = (PANEL_WIDTH - (int) bounds.getWidth()) / 2;
                int y = (PANEL_HEIGHT - (int) bounds.getHeight()) / 2;

                g.setColor(new Color(0, 0, 0, 128));
                g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

                g.setColor(Color.YELLOW);
                g.drawString("PAUSED", x, y);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (RUNNING) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (DIRECTION != 'R' && !PAUSED) {
                    DIRECTION = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (DIRECTION != 'L' && !PAUSED) {
                    DIRECTION = 'R';
                }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (DIRECTION != 'D' && !PAUSED) {
                    DIRECTION = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (DIRECTION != 'U' && !PAUSED) {
                    DIRECTION = 'D';
                }
                break;
            case KeyEvent.VK_P:
                togglePause();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
    }
    


    private void togglePause() {
        if (RUNNING) {
            PAUSED = !PAUSED;
            PAUSELABEL.setVisible(PAUSED);

            if (PAUSED) {
                TIMER.stop();
            } else {
                TIMER.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }
}
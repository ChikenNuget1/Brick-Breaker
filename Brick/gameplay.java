package Brick;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    
    private int ballPositionX = 120;
    private int ballPositionY = 350;

    Random random = new Random();
    int n = (random.nextInt(3) + 1) * -1;
    private int ballXDir = n;
    private int ballYDir = -2;

    int won = 0;

    private mapGenerator map;

    public gameplay(){
        map = new mapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    @Override
    public void paint(Graphics g){
        // Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(1, 1, 692, 592);

        // Bricks
        map.draw((Graphics2D)g);

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);

        // Border
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 530, 100, 8);

        // Ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);

        // Check if all bricks are broken
        if(totalBricks <= 0){
            play = false;
            won = 1;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You won, Score: " + score, 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Enter to Restart: ", 230, 350);
        }

        // Check game over
        if(ballPositionY > 570) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            won = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Enter to Restart: ", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        // Code for ball
        if(play){

            // Detecting intersection with paddle
            if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerX, 530, 100, 8))){
                ballYDir = -ballYDir;
            }

            // Interaction between ball and bricks
            A: for(int i = 0; i < map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width){
                                ballXDir = -ballXDir;
                            }
                            else {
                                ballYDir = -ballYDir;
                            }
                            break A;
                        }
                        
                    }
                }
            }

            ballPositionX += ballXDir;
            ballPositionY += ballYDir;
            if(ballPositionX < 0){
                ballXDir = -ballXDir;
            }
            if(ballPositionY < 0){
                ballYDir = -ballYDir;
            }
            if(ballPositionX > 670){
                ballXDir = -ballXDir;
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX = 600;
            }
            else {
                moveRight();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }
            else {
                moveLeft();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballPositionX = 120;
                ballPositionY = 350;
                
                int k = (random.nextInt(3) + 1) *-1;
                ballXDir = k;

                int p = (random.nextInt(6)+ 1) * -1;
                ballYDir = p;
                playerX = 310;
                if(won != 1){
                    score = 0;
                    won = 0;
                }
                totalBricks = 21;
                map = new mapGenerator(3, 7);

                repaint();
            }
        }
    }

    // Determines paddle movement to the right
    public void moveRight(){
        play = true;
        playerX += 15;
    }

    // Determines paddle movement to the left
    public void moveLeft(){
        play = true;
        playerX -= 15;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}

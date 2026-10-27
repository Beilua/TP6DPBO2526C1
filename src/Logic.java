import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    // Tambahkan atribut posisi dan ukuran pipa
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -2; // Tambahkan ini

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    boolean gameOver = false;

    View view;
    Image birdImage;
    Player player;

    // Tambahkan list pipa dan gambarnya
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pipe");
                placePipes();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void setView(View view){
        this.view = view;
    }

    public Player getPlayer(){
        return player;
    }

    public ArrayList<Pipe> getPipes(){
        return pipes;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void placePipes(){
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        if(view != null){
            view.repaint();
        }
    }

    public void move(){
        if(gameOver){
            return;
        }

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);
        }

        // Game over jika menyentuh bagian bawah layar
        if(player.getPosY() + player.getHeight() >= frameHeight){
            gameOver = true;
            gameLoop.stop();
            pipesCooldown.stop();
        }

        // Game over jika menabrak pipa
        Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            Rectangle pipeRect = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight());
            if(playerRect.intersects(pipeRect)){
                gameOver = true;
                gameLoop.stop();
                pipesCooldown.stop();
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
    }

    @Override
    public void keyPressed(KeyEvent e){
        if(gameOver){
            if(e.getKeyCode() == KeyEvent.VK_R){
                restartGame();
            }
            return;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.setVelocityY(-10);
        }
    }

    public void restartGame(){
        gameOver = false;
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);

        pipes.clear();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                placePipes();
            }
        });
        pipesCooldown.start();

        if(view != null){
            view.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
    }
}

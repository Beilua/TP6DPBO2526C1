import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 64;
    int playerHeight = 48;

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
    boolean gameStarted = false;
    int score = 0;

    Clip jumpSound;
    Clip scoreSound;
    Clip gameOverSound;
    Clip bgm;

    View view;
    Image birdImage;
    Image charJumpImage;
    Image charFallImage;
    Image backgroundImage;
    Player player;

    // Tambahkan list pipa dan gambarnya
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        charJumpImage = new ImageIcon(getClass().getResource("assets/charJump.png")).getImage();
        charFallImage = new ImageIcon(getClass().getResource("assets/charFall.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, charFallImage);

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

        loadSounds();
        playBGM();
    }

    public void setView(View view){
        this.view = view;
    }

    public Player getPlayer(){
        return player;
    }

    public Image getBackgroundImage(){
        return backgroundImage;
    }

    public ArrayList<Pipe> getPipes(){
        return pipes;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public boolean isGameStarted(){
        return gameStarted;
    }

    public int getScore(){
        return score;
    }

    public void placePipes(){
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = 220;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    private void loadSounds(){
        try{
            jumpSound = getClip("sounds/sfx_jump.wav");
            scoreSound = getClip("sounds/sfx_coin.wav");
            gameOverSound = getClip("sounds/sfx_disappear.wav");
            bgm = getClip("sounds/garden.wav");
        } catch(Exception e){
            System.out.println("Failed to load sounds: " + e.getMessage());
        }
    }

    private Clip getClip(String path) throws Exception{
        URL url = getClass().getResource(path);
        if (url == null) {
            throw new Exception("Sound resource not found: " + path);
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        return clip;
    }

    private void playSound(Clip clip){
        if(clip != null){
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void playBGM(){
        if(bgm != null){
            bgm.setFramePosition(0);
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        if(view != null){
            view.repaint();
        }
    }

    public void move(){
        if(!gameStarted || gameOver){
            return;
        }

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // Switch character sprite based on vertical movement
        if (player.getVelocityY() < 0) {
            player.setImage(charJumpImage);
        } else {
            player.setImage(charFallImage);
        }

        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);
        }

        // Increment score when bird passes through a pipe gap (count once per pair)
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            if(!pipe.isPassed() && pipe.getPosY() < 0 && pipe.getPosX() + pipe.getWidth() <= player.getPosX()){
                score++;
                pipe.setPassed(true);
                playSound(scoreSound);
            }
        }

        // Game over jika menyentuh bagian bawah layar
        if(player.getPosY() + player.getHeight() >= frameHeight){
            gameOver = true;
            gameLoop.stop();
            pipesCooldown.stop();
            playSound(gameOverSound);
            if(bgm != null){
                bgm.stop();
            }
            if(view != null){
                view.paintImmediately(0, 0, frameWidth, frameHeight);
            }
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
                playSound(gameOverSound);
                if(bgm != null){
                    bgm.stop();
                }
                if(view != null){
                    view.paintImmediately(0, 0, frameWidth, frameHeight);
                }
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

        if(!gameStarted){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                gameStarted = true;
                pipes.clear();
                placePipes();
            } else if(e.getKeyCode() == KeyEvent.VK_Q){
                System.exit(0);
            }
            return;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.setVelocityY(-10);
            player.setImage(charJumpImage);
            playSound(jumpSound);
        }
    }

    public void restartGame(){
        gameOver = false;
        gameStarted = true;
        score = 0;
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);

        pipes.clear();

        // Stop old timers before replacing them
        if(gameLoop != null){
            gameLoop.stop();
        }
        if(pipesCooldown != null){
            pipesCooldown.stop();
        }

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                placePipes();
            }
        });
        pipesCooldown.start();

        // Spawn a pipe pair immediately on restart
        placePipes();

        if(view != null){
            view.repaint();
        }

        // Restart BGM loop on restart
        playBGM();
    }

    @Override
    public void keyReleased(KeyEvent e){
    }
}

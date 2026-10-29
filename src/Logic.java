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

// class that handles game logic and input for flappy alien
public class Logic implements ActionListener, KeyListener {
    // window dimensions
    int frameWidth = 360;
    int frameHeight = 640;

    // player starting position and size
    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 64;
    int playerHeight = 48;

    // pipe position size and velocity attributes
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -2;

    // game timers for loop and pipe spawning
    Timer gameLoop;
    Timer pipesCooldown;
    // gravity value for player movement
    int gravity = 1;

    // game state flags and score
    boolean gameOver = false;
    boolean gameStarted = false;
    int score = 0;

    // sound clips for game audio
    Clip jumpSound;
    Clip scoreSound;
    Clip gameOverSound;
    Clip bgm;

    // reference to the view
    View view;
    // player sprite images
    Image birdImage;
    Image charJumpImage;
    Image charFallImage;
    Image backgroundImage;
    // player object
    Player player;

    // pipe images and list of pipes
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    // constructor to initialize game assets and start timers
    public Logic(){
        // load player and background images
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        charJumpImage = new ImageIcon(getClass().getResource("assets/charJump.png")).getImage();
        charFallImage = new ImageIcon(getClass().getResource("assets/charFall.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        // create player object
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, charFallImage);

        // load pipe images and initialize pipe list
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        // timer to spawn pipes every 1500 milliseconds
        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pipe");
                placePipes();
            }
        });
        pipesCooldown.start();

        // game loop timer running at 60 frames per second
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        // load and play background music
        loadSounds();
        playBGM();
    }

    // setter to link view with logic
    public void setView(View view){
        this.view = view;
    }

    // getter for player object
    public Player getPlayer(){
        return player;
    }

    // getter for background image
    public Image getBackgroundImage(){
        return backgroundImage;
    }

    // getter for pipes list
    public ArrayList<Pipe> getPipes(){
        return pipes;
    }

    // getter for game over state
    public boolean isGameOver(){
        return gameOver;
    }

    // getter for game started state
    public boolean isGameStarted(){
        return gameStarted;
    }

    // getter for current score
    public int getScore(){
        return score;
    }

    // function to place a pair of pipes at random height
    public void placePipes(){
        // calculate random vertical position for upper pipe
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        // gap space between upper and lower pipe
        int openingSpace = 220;

        // create and add upper pipe
        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        // create and add lower pipe below the gap
        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // function to load all sound clips
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

    // function to get audio clip from resource path
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

    // function to play a sound clip from the beginning
    private void playSound(Clip clip){
        if(clip != null){
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // function to loop background music continuously
    private void playBGM(){
        if(bgm != null){
            bgm.setFramePosition(0);
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // called by game loop timer each frame
    @Override
    public void actionPerformed(ActionEvent e){
        move();
        // repaint view if available
        if(view != null){
            view.repaint();
        }
    }

    // function to update game state each frame
    public void move(){
        // do nothing if game has not started or is over
        if(!gameStarted || gameOver){
            return;
        }

        // apply gravity and update player vertical position
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        // prevent player from going above the screen
        player.setPosY(Math.max(player.getPosY(), 0));

        // switch character sprite based on vertical movement direction
        if (player.getVelocityY() < 0) {
            player.setImage(charJumpImage);
        } else {
            player.setImage(charFallImage);
        }

        // move all pipes horizontally
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);
        }

        // increment score when player passes through a pipe gap once per pair
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            if(!pipe.isPassed() && pipe.getPosY() < 0 && pipe.getPosX() + pipe.getWidth() <= player.getPosX()){
                score++;
                pipe.setPassed(true);
                playSound(scoreSound);
            }
        }

        // end game if player touches the bottom of the screen
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

        // end game if player collides with a pipe
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

    // unused key typed event
    @Override
    public void keyTyped(KeyEvent e){
    }

    // function to handle key press events
    @Override
    public void keyPressed(KeyEvent e){
        // handle input when game is over
        if(gameOver){
            if(e.getKeyCode() == KeyEvent.VK_R){
                restartGame();
            } else if(e.getKeyCode() == KeyEvent.VK_Q){
                System.exit(0);
            }
            return;
        }

        // handle input before game starts
        if(!gameStarted){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                gameStarted = true;
                pipes.clear();
                placePipes();
                // restart cooldown so next pipe spawns after full interval
                pipesCooldown.restart();
            } else if(e.getKeyCode() == KeyEvent.VK_Q){
                System.exit(0);
            }
            return;
        }

        // make player jump when space is pressed
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.setVelocityY(-10);
            player.setImage(charJumpImage);
            playSound(jumpSound);
        }
    }

    // function to restart the game after game over
    public void restartGame(){
        gameOver = false;
        gameStarted = true;
        score = 0;
        // reset player position and velocity
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);

        // clear existing pipes
        pipes.clear();

        // stop old timers before replacing them
        if(gameLoop != null){
            gameLoop.stop();
        }
        if(pipesCooldown != null){
            pipesCooldown.stop();
        }

        // create and start new game loop timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        // create and start new pipe cooldown timer
        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                placePipes();
            }
        });
        pipesCooldown.start();

        // spawn a pipe pair immediately on restart
        placePipes();

        if(view != null){
            view.repaint();
        }

        // restart background music loop
        playBGM();
    }

    // unused key released event
    @Override
    public void keyReleased(KeyEvent e){
    }
}

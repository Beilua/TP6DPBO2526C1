import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

// class that renders the flappy alien game view
public class View extends JPanel {

    // view dimensions
    int width = 360;
    int height = 640;

    // base font loaded from resource
    private static Font baseFont;

    // static block to load custom font from resources
    static {
        try (InputStream is = View.class.getResourceAsStream("/fonts/MatchaWorld.ttf")) {
            if (is != null) {
                baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            } else {
                baseFont = new Font("Arial", Font.BOLD, 1);
            }
        } catch (Exception e) {
            baseFont = new Font("Arial", Font.BOLD, 1);
        }
    }

    // function to get font with given size and style
    private Font getFont(int size, int style) {
        return baseFont.deriveFont(style, (float) size);
    }

    // draws white text with a chunky outline matching game assets
    private void drawOutlinedString(Graphics2D g2, String text, int x, int y) {
        int offset = 2;
        // outline color for the text
        g2.setColor(new Color(72, 36, 92));
        for (int dx = -offset; dx <= offset; dx++) {
            for (int dy = -offset; dy <= offset; dy++) {
                if (dx == 0 && dy == 0) continue;
                g2.drawString(text, x + dx, y + dy);
            }
        }
        // draw white text on top
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    // reference to game logic
    private Logic logic;
    // label to display score
    private JLabel scoreLabel;

    // constructor to set up view and link with logic
    public View(Logic logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        setLayout(null);

        // score label in top left corner
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(getFont(24, Font.BOLD));
        scoreLabel.setForeground(new Color(165, 81, 213));
        scoreLabel.setBounds(10, 10, 150, 30);
        add(scoreLabel);

        // make panel focusable and add key listener for input
        setFocusable(true);
        addKeyListener(logic);
    }

    // repaint component and call draw function
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // main drawing function for game graphics
    public void draw(Graphics g){
        // draw background image
        Image background = logic.getBackgroundImage();
        if(background != null){
            g.drawImage(background, 0, 0, width, height, null);
        }

        // draw player image
        Player player = logic.getPlayer();
        if(player != null){
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        }

        // draw all pipe images
        ArrayList<Pipe> pipes = logic.getPipes();
        if(pipes != null){
            for(int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        Graphics2D g2 = (Graphics2D) g;

        // draw game over screen if game is over
        if(logic.isGameOver()){
            g2.setFont(getFont(40, Font.BOLD));
            String msg = "GAME OVER";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            drawOutlinedString(g2, msg, (width - msgWidth) / 2, height / 2 - 20);

            g2.setFont(getFont(24, Font.PLAIN));
            String restartMsg = "Press R to restart";
            int restartWidth = g2.getFontMetrics().stringWidth(restartMsg);
            drawOutlinedString(g2, restartMsg, (width - restartWidth) / 2, height / 2 + 40);

            String quitMsg = "Press Q to quit";
            int quitWidth = g2.getFontMetrics().stringWidth(quitMsg);
            drawOutlinedString(g2, quitMsg, (width - quitWidth) / 2, height / 2 + 80);
        } else if(!logic.isGameStarted()){
            // draw main menu screen
            g2.setFont(getFont(40, Font.BOLD));
            String title = "FLAPPY ALIEN";
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            drawOutlinedString(g2, title, (width - titleWidth) / 2, height / 2 - 60);

            g2.setFont(getFont(24, Font.PLAIN));
            String startMsg = "Press SPACE to start";
            int startWidth = g2.getFontMetrics().stringWidth(startMsg);
            drawOutlinedString(g2, startMsg, (width - startWidth) / 2, height / 2);

            String quitMsg = "Press Q to quit";
            int quitWidth = g2.getFontMetrics().stringWidth(quitMsg);
            drawOutlinedString(g2, quitMsg, (width - quitWidth) / 2, height / 2 + 40);
        }

        // keep score label text in sync with current score
        scoreLabel.setText("Score: " + logic.getScore());
    }
}

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

public class View extends JPanel {

    int width = 360;
    int height = 640;

    private static Font baseFont;

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

    private Font getFont(int size, int style) {
        return baseFont.deriveFont(style, (float) size);
    }

    // Draws white text with a chunky outline, matching the other game assets
    private void drawOutlinedString(Graphics2D g2, String text, int x, int y) {
        int offset = 2;
        // Chunky outline color
        g2.setColor(new Color(72, 36, 92));
        for (int dx = -offset; dx <= offset; dx++) {
            for (int dy = -offset; dy <= offset; dy++) {
                if (dx == 0 && dy == 0) continue;
                g2.drawString(text, x + dx, y + dy);
            }
        }
        // White text on top
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    private Logic logic;
    private JLabel scoreLabel;

    public View(Logic logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        setLayout(null);

        // Score label in top-left corner (kept as JLabel, black text)
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(getFont(24, Font.BOLD));
        scoreLabel.setForeground(new Color(165, 81, 213));
        scoreLabel.setBounds(10, 10, 150, 30);
        add(scoreLabel);

        // Tambahkan fokus dan Key Listener
        // Untuk menerima input
        setFocusable(true);
        addKeyListener(logic);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        Image background = logic.getBackgroundImage();
        if(background != null){
            g.drawImage(background, 0, 0, width, height, null);
        }

        Player player = logic.getPlayer();
        if(player != null){
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        }

        ArrayList<Pipe> pipes = logic.getPipes();
        if(pipes != null){
            for(int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        Graphics2D g2 = (Graphics2D) g;

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
            // Main menu
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

        // Keep score label in sync
        scoreLabel.setText("Score: " + logic.getScore());
    }
}

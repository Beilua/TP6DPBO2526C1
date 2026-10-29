import java.awt.*;

// class to represent a pipe obstacle in the game
public class Pipe {
    // pipe position and size attributes
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private int velocityX;
    // flag to check if player has passed the pipe
    boolean passed;

    // constructor to initialize pipe attributes
    public Pipe(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        // initial velocity is zero
        this.velocityX = 0;
        // pipe is not passed yet
        this.passed = false;
    }

    // getter and setter for horizontal position
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    // getter and setter for vertical position
    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    // getter and setter for width
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    // getter and setter for height
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // getter and setter for image
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // getter and setter for horizontal velocity
    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    // getter and setter for passed flag
    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}

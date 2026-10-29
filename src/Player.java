import java.awt.*;

// class to represent the player character in the game
public class Player {
    // player position size image and velocity attributes
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private int velocityY = -0;

    // constructor to initialize player attributes
    public Player(int posX, int posY, int width, int height, Image image){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        // initial velocity is zero
        this.velocityY = -0;
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

    // getter and setter for vertical velocity
    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
}

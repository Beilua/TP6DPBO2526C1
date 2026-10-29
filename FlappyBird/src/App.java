import javax.swing.*;

// main application class to run the flappy alien game
public class App {
    // entry point of the program
    public static void main(String[] args) {
        // create the game window with title
        JFrame frame = new JFrame("Flappy Alien");
        // exit the program when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set window size
        frame.setSize(360, 640);
        // center window on screen
        frame.setLocationRelativeTo(null);
        // make window not resizable
        frame.setResizable(false);
        // create logic object for game mechanics
        Logic logika = new Logic();
        // create view and link it with logic so view can communicate with logic
        View tampilan = new View(logika);
        // set the view in logic
        logika.setView(tampilan);

        tampilan.requestFocus();

        // add view to the frame
        frame.add(tampilan);
        frame.pack();
        // show the window
        frame.setVisible(true);
    }
}

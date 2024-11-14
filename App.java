import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth=340;
        int boardHeight=640;
        JFrame frame = new JFrame("FlappyBird");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);  
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappybird = new FlappyBird();

        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus(); //to allow it to receive keyboard input
        frame.setVisible(true);
    }
}

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class FlappyBird extends JPanel implements ActionListener,KeyListener{
    int boardWidth=360;
    int boardHeight=640;

    //for images
    Image toppipeImg;
    Image flappybirdImg;
    Image flappybirdbgImg;
    Image bottompipeImg;

    //Bird
    int birdx = boardWidth/8;
    int birdy = boardHeight/2;
    int birdwidth = 34;
    int birdheight = 24;

    class Bird{
        int x = birdx;
        int y = birdy;
        int width = birdwidth;
        int height = birdheight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipewidth = 64; //scaled by 1/6 
    int pipeheight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }

    //Game logic
    Bird bird;
    int velocityY = 0;
    int gravity = 1;
    int velocityX = -4;
    
    ArrayList<Pipe> pipes;
    Random random = new Random();
    


    //timer
    Timer gameloop;
    Timer placepipesTimer;
    boolean gameover = false;
    double score =0;
    double highscore=0;

    
    private JButton replay;
    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
    
        //to load images
        flappybirdbgImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        flappybirdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        toppipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //birdobj
        bird = new Bird(flappybirdImg);
        pipes = new ArrayList<Pipe>();

        //placceppipetimer
        placepipesTimer = new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });

        //game loop
        gameloop = new Timer(1000/60,this);

        //restart
        replay = new JButton("replay");
        replay.setVisible(true);
        replay.setBounds(150,100,120,40);
        replay.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                replaygame();
            }
        });

    }

    public void placePipes(){
        int randompipeY = (int)(pipeY - pipeheight/4 - Math.random()*(pipeheight/2));
        int openingspace = boardHeight/4;

        Pipe toppipe = new Pipe(toppipeImg);
        toppipe.y = randompipeY;
        pipes.add(toppipe);

        Pipe bottompipe = new Pipe(bottompipeImg);
        bottompipe.y = toppipe.y + pipeheight + openingspace;
        pipes.add(bottompipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(flappybirdbgImg,0,0,boardWidth,boardHeight,null);

        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Calibri",Font.PLAIN,32));
        if(gameover){
            g.drawString("Game Over! Your Score:" + String.valueOf((int) score),10,35);
        }
        else{
            g.drawString(String.valueOf((int) score),10,35);
        }
    }

    public void move(){
        //bird 
        velocityY+=gravity;
        bird.y +=velocityY;
        bird.y = Math.max(bird.y,0);

        //pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x +=velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score+=0.5; // for 1 pipe 0.5 so for 2 pipes 2*0.5
            }
        
            if(collision(bird,pipe)){
                gameover = true;
            }
        }
        if(bird.y>boardHeight){
            gameover = true;
        }

    }

    public boolean collision(Bird a,Pipe b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x && 
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // runs 60 times per second
        move(); 
        repaint();
        if(gameover){
            placepipesTimer.stop();
            gameloop.stop();
            replay.setVisible(true);

        }

    }

    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9; 
            gameloop.start();
            placepipesTimer.start();
        }
        if(gameover){
            //restart the game by resetting the conditions
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                replaygame();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void replaygame(){
        bird.y = birdy;
        velocityY = 0;
        pipes.clear();
        score=0;
        gameover = false;
        gameloop.start();
        placepipesTimer.start();
    }

}  
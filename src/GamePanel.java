import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //Screen settings
    final int originalTieSize = 16;
    final int scale = 3;
    public final int tileSize = originalTieSize * scale; //48 * 48 tile

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight= tileSize * maxScreenRow; //576 pixels

    //World Setting
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;


    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();
    Sound se = new Sound();

    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter((this));
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public SuperObject obj[] = new SuperObject[10];

    //Constructor
    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);//for better Rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true); //this gamePanel can be focused to receive key input


    }

    //call set object method from AssetSetter Class
    public void setupGame(){
        aSetter.setObject();
        playMusic(0);
    }

    public void startGameThread(){
        gameThread =new Thread(this);
        gameThread.start();//This will call run method
    }

    @Override
    public void run() {
        //GameLoop

        double drawInterval = 1000000000/FPS; //1 second = 1 billion nanosecod
        // 0.0166666 second
        double nextDrawTime = System.nanoTime() + drawInterval;



        while (gameThread != null){


            //Update
            update();

            //Draw
            repaint();

            try {
                double remainingTime= nextDrawTime -  System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime); //it paused the game loop untill the sleep time is over

                nextDrawTime +=drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void update(){
        player.update(); //call the update method from the Player class
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //Tile
        tileM.draw(g2);

        //Object
        for(int i = 0; i<obj.length; i++){
            if(obj[i] != null){
                obj[i].draw(g2, this);
            }
        }

        //Player
        player.draw(g2); //call the draw method from the Player class
        g2.dispose();
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    //Sound Effect
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}

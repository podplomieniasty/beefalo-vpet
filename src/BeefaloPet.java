import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class BeefaloPet {

    private final int FRAMEWIDTH = 256;
    private final int FRAMEHEIGHT = 256;

    private JFrame petFrame;
    private JLabel imgContainer;
    private Dimension screenSize;
    private Point petLocation;
    private String animPath = "src/assets/";

    private Random rng;

    public void start() {
        //launch the pet and set up all the stuff
        rng = new Random();
        this.getScreenSize();
        this.imgIconInit();
        this.frameInit();
        this.addImgToFrame();
        this.updateImage();
        System.out.println(petLocation.x + " " + petLocation.y);
        movePet.start();

    }
    private void getScreenSize() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }
    private void frameInit() {
        //frame configuration
        petFrame = new JFrame();
        petFrame.setTitle("Beefalo");
        petFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        petFrame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        petFrame.setLocationRelativeTo(null);
        petFrame.setUndecorated(true);
        petFrame.setBackground(new Color(0,0,0,0));
        petFrame.setVisible(true);
        petLocation = new Point(petFrame.getX(), petFrame.getY());
    }

    private void imgIconInit() {
        //icon configuration
        imgContainer = new JLabel();
        imgContainer.setSize(256, 256);
        imgContainer.setOpaque(false);
    }

    private void addImgToFrame() {
        petFrame.add(imgContainer);
    }

    private void updateImage() {
        //ImageIcon i = new ImageIcon(animPath+"beefalo/idle/happybeef.png");
        ImageIcon i = new ImageIcon(BeefaloPet.this.getClass().getResource("beef.png"));
        Image ig = i.getImage().getScaledInstance(256,256, Image.SCALE_DEFAULT);
        i = new ImageIcon(ig);
        imgContainer.setIcon(i);
    }

    private void updateLocation(int _x, int _y) {

        petLocation.x = _x;
        petLocation.y = _y;

        petFrame.setLocation(petLocation.x, petLocation.y);
    }

    private Thread movePet = new Thread(() -> {

        try {

            int dirX = 1;
            int dirY = 1;
            int step = 1;
            // beef velocity

            int petWaitTime, newX, newY;

            // main movement loop
            while(true) {

                petWaitTime = rng.nextInt(4) * 1000;
                newX = rng.nextInt(screenSize.width);
                newX = newX > screenSize.width - petFrame.getWidth() ? newX - petFrame.getWidth() : newX;
                newY = rng.nextInt(screenSize.height);
                newY = newY > screenSize.height - petFrame.getHeight() ? newY - petFrame.getHeight() : newY;

                Thread.sleep(petWaitTime);

                dirX = petLocation.x < newX ? step : -step;
                dirY = petLocation.y < newY ? step : -step;

                System.out.println(newX + " " + newY);

                while(newX != petLocation.x || newY != petLocation.y) {

                    // movement delay time
                    dirX = petLocation.x == newX ? 0 : dirX;
                    dirY = petLocation.y == newY ? 0 : dirY;

                    Thread.sleep(10l);
                    updateLocation(petLocation.x+dirX, petLocation.y+dirY);
                }
            }
        }
        catch(InterruptedException e) {
            System.out.println("Fatal error! Exiting...");
            System.exit(-1);
        }

    });



}



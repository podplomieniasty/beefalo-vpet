import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

public class BeefaloPet {

    private final int FRAMEWIDTH = 256;
    private final int FRAMEHEIGHT = 256;

    private JFrame petFrame;
    private JPopupMenu petPopUpMenu;
    private JLabel imgContainer;
    private Dimension screenSize;
    private Point petLocation;
    private int petStep = 1;
    private Random rng;

    private enum State {
        IDLE,
        HOVER
    }

    private Point mouseLocation;
    private State petState;
    private int petClicks;
    private int maxPetClicks;
    private long petMovementSpeed;

    private void init() {
        //launch the pet and set up all the stuff
        rng = new Random();
        this.getScreenSize();
        this.imgIconInit();
        this.frameInit();
        this.popUpMenuInit();
        this.addImgToFrame();
        this.updateImage();

        this.maxPetClicks = 3;
        this.petMovementSpeed = 10; // updates after some ms
    }

    private void popUpMenuInit() {

        petPopUpMenu = new JPopupMenu();

        // option to pet the beefalo
        // he should be pet as often as possible
        // because beef's such a good beefalo
        JMenuItem menuItem = new JMenuItem("Pet the beefalo");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(petFrame, "You've pet the beefalo!");
            }
        });
        petPopUpMenu.add(menuItem);

    }

    private void updatePetClicks() {
        this.petClicks++;
        if (petClicks > maxPetClicks)
            this.petClicks = 1;
    }

    public void start() {
        this.init();
        this.petMovementLoop.start();
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
        petFrame.setBackground(new Color(0, 0, 0, 0));
        petFrame.setVisible(true);
        petLocation = new Point(petFrame.getX(), petFrame.getY());

        // mouse listeners for Beef
        petFrame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent mouseEvent) {

                setState(State.HOVER);
            }

            public void mouseExited(MouseEvent mouseEvent) {

                setState(State.IDLE);
            }

            public void mouseClicked(MouseEvent mouseEvent) {
                switch(mouseEvent.getButton()) {
                    case MouseEvent.BUTTON1: // left click
                        updatePetClicks();
                        System.out.println("You've clicked Beefalo " + petClicks + " times!");
                        break;
                    case MouseEvent.BUTTON3:
                        petPopUpMenu.show(petFrame, mouseEvent.getX(), mouseEvent.getY());
                        break;
                }

            }

        });
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
        Image ig = i.getImage().getScaledInstance(256, 256, Image.SCALE_DEFAULT);
        i = new ImageIcon(ig);
        imgContainer.setIcon(i);
    }

    private void updateLocation(int _x, int _y) {

        petLocation.x = _x;
        petLocation.y = _y;
        petFrame.setLocation(petLocation.x, petLocation.y);
    }

    private void setState(State _state) {
        this.petState = _state;
        System.out.println("STATE: Changed state to " + petState.toString());
    }

    private Thread petMovementLoop = new Thread(() -> {

        setState(State.IDLE);
        System.out.println("\nMAIN LOOP: Entering the next iteration of Beef's life.");

        while (true) {
            System.out.println("Detecting state: " + petState.toString());
            switch (petState) {
                // the Beef is doing nothing - casual walking,
                // nothing hovers over him, or he wasn't RMB'ed
                case IDLE:

                    System.out.println("The Beefalo is now idle.");
                    try {
                        int dirX = 1;
                        int dirY = 1;

                        int petWaitTime, newX, newY;
                        int maxPetWaitTime = 4; // seconds
                        petWaitTime = rng.nextInt(maxPetWaitTime) * 1000;

                        System.out.println("Trying to generate a path for beefalo...");

                        newX = rng.nextInt(screenSize.width);
                        newX = newX > screenSize.width - petFrame.getWidth() ? newX - petFrame.getWidth() : newX;
                        newY = rng.nextInt(screenSize.height);
                        newY = newY > screenSize.height - petFrame.getHeight() ? newY - petFrame.getHeight() : newY;

                        System.out.printf("Beefalo waits for %ds and moves to X:%d Y:%d\n", (petWaitTime/1000), newX, newY);

                        Thread.sleep(petWaitTime);

                        // assert in which direction should the beef move
                        dirX = petLocation.x < newX ? petStep : -petStep;
                        dirY = petLocation.y < newY ? petStep : -petStep;

                        while(newX != petLocation.x || newY != petLocation.y) {
                            // in case of state changing mid-walking,
                            // the loop breaks
                            if(petState != State.IDLE) break;

                            // if beef reaches target X or Y,
                            // he should stop moving in that direction.
                            dirX = petLocation.x == newX ? 0 : dirX;
                            dirY = petLocation.y == newY ? 0 : dirY;

                            // how often should the pet move?
                            Thread.sleep(petMovementSpeed);
                            updateLocation(petLocation.x+dirX, petLocation.y+dirY);
                        }

                    }
                    catch(InterruptedException e) {
                        System.out.println("Unable to generate and/or move alongside the path.");
                    }

                    System.out.println("Exiting idle switch statement.");
                    break;
                    // end of IDLE condition

                case HOVER:

                    break;
            }
        }

    });

}


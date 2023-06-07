package nl.saxion.ptbc.frog_pilot;

import nl.saxion.ptbc.sockets.SasaCommunicationException;
import nl.saxion.ptbc.sockets.SasaServer;
import nl.saxion.ptbc.sockets.SasaSocketInformation;
import nl.saxion.ptbc.shared.Shared;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * The base class of The Pilot.
 */


public class ThePilot implements SasaSocketInformation, KeyListener, MouseListener {
    ArrayList<String> testCords = new ArrayList<>();
    ArrayList<ObstaclesCords> list = new ArrayList<>();
    private final SasaServer sasaServer;
    private JFrame frame;
    private Container container;
    private JTextArea textArea;

    private int powerIncrease = 1000;
    private int angleIncrease = 1;
    private int power = 0;
    private int angle = 0;
    private int duration = 0;


    public ThePilot(int port) {
        // Setup server
        sasaServer = new SasaServer();
        sasaServer.subscribe(this);
        sasaServer.listen(port);
        System.out.println("The Pilot is listening on port " + port);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Maximize the screen size of the frame
        frame.setSize(1320, 790);
        container = frame.getContentPane();
        container.setLayout(null);
        textArea = new JTextArea();
        textArea.setBounds(10, 10, 470, 470);
        textArea.addKeyListener(this);
        container.add(textArea);

        // Load the image using ImageIcon
        ImageIcon imageIcon = new ImageIcon("shared/resources/MoonMap.png");
        // Create a JLabel and set the image icon
        JLabel imageLabel = new JLabel(imageIcon);
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 750;
        int height = 749;
        imageLabel.setBounds(550, 0, width, height);
        frame.add(imageLabel);
        // Add mouse listener to the frame
        imageLabel.addMouseListener(this);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        ThePilot thePilot = new ThePilot(Shared.COMMUNICATION_PORT); // Setup server to listen to a connecting Frog
        //TODO start The Frog

        File file = new File("TheFrogWinV2023-2.X64/TheFrog.exe");
        if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
        {
            System.out.println("not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (file.exists())//checks file exists or not
        {
            try {
                desktop.open(file);//opens the specified file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //TODO and create The Pilot dashboard
    }

    @Override
    public void receivedData(String message) {
        list = ObstaclesCords.getCords(message);
    }

    @Override
    public void statusUpdate(String client, String status) {
        //TODO what to do when a client like The Frog or Ground Control (dis)connects)?

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        switch (e.getKeyCode()) {
            case 37 -> angle -= angleIncrease;
            case 38 -> power = powerIncrease;
            case 40 -> power = -powerIncrease;
            case 39 -> angle += angleIncrease;
        }


        try {
            sasaServer.send("PILOT DRIVE " + power + " " + angle + " " + duration);
        } catch (SasaCommunicationException ex) {
            ex.printStackTrace();
        }
        if (e.getKeyCode() == 82){
            for (ObstaclesCords oc : list) {
                System.out.println("obstacle cords: " + oc.getBreadthX() + ", " + oc.getHeightY() + ", " + oc.getDepthZ());
            }
            list.clear();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 38, 40 -> power = 0;
            case 37, 39 -> angle = 0;
        }

        try {
            sasaServer.send("PILOT DRIVE " + power + " " + angle + " " + duration);
            sasaServer.send("PILOT RADAR");
        } catch (SasaCommunicationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof JLabel) {
            JLabel imageLabel = (JLabel) e.getSource();
            Icon icon = imageLabel.getIcon();
            if (icon instanceof ImageIcon) {
                ImageIcon imageIcon = (ImageIcon) icon;
                Image image = imageIcon.getImage();
                int imageWidth = image.getWidth(imageLabel);
                int imageHeight = image.getHeight(imageLabel);
                int labelWidth = imageLabel.getWidth();
                int labelHeight = imageLabel.getHeight();
                int x = e.getX();
                int y = e.getY();
                if (x >= 0 && x < labelWidth && y >= 0 && y < labelHeight) {
                    double xScale = (421.29 - (-78.75)) / labelWidth;
                    double yScale = (354.13 - (-145.892)) / labelHeight;
                    double scaledX = -78.75 + (x / (double) labelWidth) * (421.29 - (-78.75));
                    double scaledY = 354.13 - (y / (double) labelHeight) * (354.13 - (-145.892));
                    if (scaledX < -78.75) {
                        scaledX = -78.75;
                    } else if (scaledX > 421.29) {
                        scaledX = 421.29;
                    }
                    if (scaledY < -145.892) {
                        scaledY = -145.892;
                    } else if (scaledY > 354.13) {
                        scaledY = 354.13;
                    }
                    System.out.println("Mouse clicked within the Moon map at (" + scaledX + ", " + scaledY + ")");
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

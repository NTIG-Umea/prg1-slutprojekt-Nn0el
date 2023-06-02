import sun.java2d.loops.DrawRect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class RitSpel extends Canvas implements Runnable{
    private BufferStrategy bs;
    private boolean running = false;
    private Thread thread;

    private BufferedImage image;

    private int button = 0;
    public RitSpel() {
        setSize(600, 400);
        image = new BufferedImage(600,400,BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0x0000ff));
        g.drawString("Reset",5 ,395);
        g.drawRect(0,380,100,19);
        g.drawRect(0,361,100,19);
        g.drawRect(0,342,100,19);
        g.drawRect(0,323,100,19);
        JFrame frame = new JFrame();
        frame.add(this);
        this.addMouseMotionListener(new MyMouseMotionListener());
        this.addMouseListener(new MyMouseListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public class MyMouseMotionListener implements MouseMotionListener {


        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (mouseX > 100) {
                Graphics g = image.getGraphics();

            }
            if (button == 1){
                Graphics g = image.getGraphics();
                g.setColor(Color.blue);
                g.fillOval(mouseX, mouseY, 10, 10);
            }
            else if (button == 2){
                Graphics g = image.getGraphics();
                g.setColor(Color.red);
                g.fillOval(mouseX, mouseY, 10, 10);
            }
        }
    }

    public class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            if (mouseY>370 && mouseX<100){
                Graphics g = image.getGraphics();
                g.setColor(new Color(0xECECEC));
                g.fillRect(101, 0,500,400);
            } else if (mouseX < 100) {

            }
            else {
                Graphics g = image.getGraphics();
                g.setColor(new Color(0xFF0000));
                g.fillOval(mouseX, mouseY, 10, 10);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            button = e.getButton();
            System.out.println("Mousebutton " + button);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            button = 0;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public static void main(String[] args) {
        RitSpel noelsSpel = new RitSpel();
        noelsSpel.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Rita ut den nya bilden
        draw(g);

        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        g.drawImage(image,0,0,null);
    }

    private void update() {
    }


    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        double ns = 1000000000.0 / 60.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }


}

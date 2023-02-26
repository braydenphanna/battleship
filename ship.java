/**BATTLESHIP SHIP
 * 
 * @braydenHanna
 * @2/8
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MouseInputAdapter;

public class ship extends JLayeredPane {
    public String type;
    public Dimension d = new Dimension();
    public String direction;
    public double scale;
    public int size;
    public ImageIcon image = new ImageIcon();
    private JButton ship = new JButton();
    public JLabel hitbox = new JLabel();
    public boolean dropped = false;
    public boolean locked;
    public int health;

    public ship(String type) {
        this.setLayout(null);
        ShipMouseListener drag = new ShipMouseListener();
        ship.addMouseListener(drag);
        ship.addMouseMotionListener(drag);
        this.type = type;
        locked = false;
        direction = "r";
        switch (type) {
            case "ventator":
                scale = 0.37;
                size = 5;
                break;
            case "frigate":
                scale = 0.62;
                size = 4;
                break;
            case "ywing":
                scale = 0.40;
                size = 3;
                break;
            default:
                break;
        }
        health = size;
        hitbox.setBorder(BorderFactory.createLineBorder(Color.green, 1));
        //this.setBorder(BorderFactory.createLineBorder(Color.magenta, 1));
        image = util.scaleImage(new ImageIcon(type +"_" + direction + ".png"), scale);
        this.setLocation(600,600);
        this.setSize(gui.gridTileS * (int) Math.round((double) image.getIconWidth() / gui.gridTileS),
        gui.gridTileS * 3);

        hitbox.setSize((gui.gridTileS * size), gui.gridTileS);
        hitbox.setLocation((this.getWidth() - hitbox.getWidth()) / 2, (this.getHeight() - hitbox.getHeight()) / 2);
        hitbox.setOpaque(false);
        this.add(hitbox, JLayeredPane.PALETTE_LAYER);

        ship.setIcon(image);
        ship.setOpaque(false);
        ship.setContentAreaFilled(false);
        ship.setBorderPainted(false);
        ship.setBounds((getWidth() - image.getIconWidth()) / 2, (getHeight() - image.getIconHeight()) / 2,
        image.getIconWidth(), image.getIconHeight());
        this.add(ship, JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void setLocation(int x, int y) {
        if (!locked) {
            if (dropped == true) {
                super.setLocation(
                        (x + (this.getWidth() - hitbox.getWidth()) / 2),
                        (y + (this.getHeight() - hitbox.getHeight()) / 2));;
            } else {
                super.setLocation(x, y);
            }
        }
    }

    public void rotateShip(String newDir) {
        if (!locked) {
            d = new Dimension((int) getHeight(), (int) getWidth());
            this.setPreferredSize(d);
            this.setBounds(getX(), getY(), (int) d.getWidth(), (int) d.getHeight());
            hitbox.setBorder(BorderFactory.createLineBorder(Color.green, 1));
            hitbox.setSize(hitbox.getHeight(), hitbox.getWidth());

            hitbox.setLocation((this.getWidth() - hitbox.getWidth()) / 2, (this.getHeight() - hitbox.getHeight()) / 2);

            image = util.scaleImage(new ImageIcon((type + "_" + newDir + ".png")), scale);
            ship.setIcon(image);
            ship.setBounds((getWidth() - image.getIconWidth()) / 2, (getHeight() - image.getIconHeight()) / 2,
            image.getIconWidth(), image.getIconHeight());
            direction = newDir;
        }
    }
}

class ShipMouseListener extends MouseInputAdapter {
    int x;
    int y;
    Component component;
    Point location;
    MouseEvent pressed;

    public void mousePressed(MouseEvent me) {
        pressed = me;
        if(game.gameState == 2) return;
        if (SwingUtilities.isRightMouseButton(me)) {
            if (((ship) component).direction.equals("u"))
                ((ship) component).rotateShip("r");
            else if (((ship) component).direction.equals("r"))
                ((ship) component).rotateShip("d");
            else if (((ship) component).direction.equals("d"))
                ((ship) component).rotateShip("l");
            else if (((ship) component).direction.equals("l"))
                ((ship) component).rotateShip("u");
        }
    }

    public void mouseDragged(MouseEvent me) {
        if(game.gameState == 2) return;
        if (SwingUtilities.isLeftMouseButton(me)) {
            component = me.getComponent().getParent();
            location = component.getLocation(location);
            x = (location.x + - pressed.getX() + me.getX());
            y = (location.y + - pressed.getY() + me.getY());
            component.setLocation(x, y);
        }
    }

    public void mouseReleased(MouseEvent me) {
        if(game.gameState == 2) return;
        ((ship) component).dropped = true;
        x = gui.gridTileS * ((int)(x/gui.gridTileS));
        y = gui.gridTileS * ((int)(y/gui.gridTileS));
        if(((ship)component).direction.equals("u") || ((ship)component).direction.equals("d")){
            if(x<-gui.gridTileS) x = -gui.gridTileS;
            else if(x+component.getWidth()>gui.gridTileS*11) x = gui.gridTileS*11 - component.getWidth();
            if(y<gui.gridTileS) y = gui.gridTileS;
            else if(y+component.getHeight()>gui.gridTileS*11) y = gui.gridTileS*11 - component.getHeight();
        }
        else{
            if(x<gui.gridTileS) x = gui.gridTileS;
            else if(x+component.getWidth()>gui.gridTileS*11) x = gui.gridTileS*11 - component.getWidth();
            if(y<-gui.gridTileS) y = -gui.gridTileS;
            else if(y+component.getHeight()>gui.gridTileS*11) y = gui.gridTileS*11 - component.getHeight();
        }
        
        component.setLocation(x, y);
        
        ((ship) component).dropped = false;

        component = me.getComponent().getParent();

        for (int i = 0; i < game.userBoard.length; i++) {
            for (int j = 0; j < game.userBoard[0].length; j++) {
                if(game.userBoard[i][j] == ((ship) component).size) game.userBoard[i][j] = 0;
            }

        }
        for (ship s : game.ships) {
            int x = s.getX() - gui.gridTileS
                + (s.getWidth() - s.hitbox.getWidth()) / 2;
            int y = s.getY() - gui.gridTileS
                + (s.getHeight() - s.hitbox.getHeight()) / 2;


            if (s.direction.equals("u") || s.direction.equals("d")) {
                for (int i = 0; i < s.size; i++) {
                    game.userBoard[(y + gui.gridTileS * i) / gui.gridTileS][x/ gui.gridTileS] = s.size;
                }
            }
            if (s.direction.equals("r") || s.direction.equals("l")) {
                for (int i = 0; i < s.size; i++) {
                    game.userBoard[y / gui.gridTileS][(x + gui.gridTileS * i)/ gui.gridTileS] = s.size;
                }
            }
        }
        

        System.out.println("---------------------------");
        for (int i = 0; i < game.userBoard.length; i++) {
            for (int j = 0; j < game.userBoard[0].length; j++) {
                System.out.print(game.userBoard[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}
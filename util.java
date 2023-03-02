/**BATTLESHIP UTIL
 * 
 * @braydenHanna
 * @2/8
 */

import javax.swing.*;

import java.awt.*;

public class util {
    public static ImageIcon scaleImage(ImageIcon imageIcon, double scale) {
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) (image.getWidth(null) * scale),
                (int) (image.getHeight(null) * scale), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newimg); // transform it back
    }
}
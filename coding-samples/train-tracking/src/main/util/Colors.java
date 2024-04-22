package util;

import java.awt.Color;
import java.util.Random;

/**
 * Returns random color of object type Color.
 * 
 * @return Color: Random Color.
 */
public class Colors {
    public static Color randomColor() {
        Random rand = new Random();
        return new Color((int) (255 * rand.nextFloat()), (int) (255 * rand.nextFloat()),
                (int) (255 * rand.nextFloat()));
    }

}

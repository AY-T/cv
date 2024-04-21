package util;

import java.awt.Color;
import java.util.Random;

public class Colors {
    public static Color randomColor() {
        Random rand = new Random();
        return new Color((int) (255 * rand.nextFloat()), (int) (255 * rand.nextFloat()),
                (int) (255 * rand.nextFloat()));
    }

}

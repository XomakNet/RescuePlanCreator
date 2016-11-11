package net.xomak.sga2;

import net.xomak.sga2.field.Field;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by regis on 12.11.2016.
 */
public class RadiationMapCreator {
    private Field field;
    private double[][] radiation;
    private BufferedImage bufferedImage;

    public RadiationMapCreator(Field field, double[][] radiation) {
        this.field = field;
        this.radiation = radiation;
    }

    public BufferedImage drawImage() {
        bufferedImage = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        double maxLevel = 0;
        for(int x = 0; x < field.getWidth(); x++) {
            for(int y = 0; y < field.getWidth(); y++) {
                double current = radiation[x][y];
                if(current != Double.POSITIVE_INFINITY) {
                    maxLevel = Math.max(current, maxLevel);
                }
            }
        }
        for(int x = 0; x < field.getWidth(); x++) {
            for(int y = 0; y < field.getWidth(); y++) {
                double current = radiation[x][y];

                // TODO: Change this
                if(current > maxLevel) {
                    current = maxLevel;
                }


                Color test = Color.getHSBColor(0, (float)(current/maxLevel), 1);
                bufferedImage.setRGB(x, y, test.getRGB());
            }
        }
        return bufferedImage;
    }

    public void saveToFile(String path) {
        if(bufferedImage == null) {
            drawImage();
        }
        File outputfile = new File(path);
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

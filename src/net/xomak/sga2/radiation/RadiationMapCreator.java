package net.xomak.sga2.radiation;

import net.xomak.sga2.field.Field;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RadiationMapCreator {
    private Field field;
    private double[][] radiation;
    private BufferedImage bufferedImage;
    private Color coldColor = new Color(102, 0, 204);
    private Color hotColor = new Color(255, 51, 0);

    public RadiationMapCreator(Field field, double[][] radiation) {
        this.field = field;
        this.radiation = radiation;
    }

    public BufferedImage drawImage() {
        bufferedImage = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        double maxLevel = radiation[0][0];
        double minLevel = radiation[0][0];
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getWidth(); y++) {
                double current = radiation[x][y];
                if (current != Double.POSITIVE_INFINITY) {
                    maxLevel = Math.max(current, maxLevel);
                    minLevel = Math.min(current, minLevel);
                }
            }
        }
        double maxMinusMin = maxLevel - minLevel;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getWidth(); y++) {
                double current = radiation[x][y];
                if (current > maxLevel) {
                    current = maxLevel;
                }
                double normalized = (current - minLevel) / maxMinusMin;

                Color heatColor = getHeatColor(normalized);
                bufferedImage.setRGB(x, y, heatColor.getRGB());
            }
        }
        return bufferedImage;
    }

    /**
     * Mix {@link #coldColor} and {@link #hotColor} together and return a color for particular normalized temperature
     *
     * @param normalizedTemperature temperature from 0 to 1.0
     * @return color
     */
    private Color getHeatColor(double normalizedTemperature) {
        int red = (int) ((hotColor.getRed() - coldColor.getRed()) * normalizedTemperature + coldColor.getRed());
        int green = (int) ((hotColor.getGreen() - coldColor.getGreen()) * normalizedTemperature + coldColor.getGreen());
        int blue = (int) ((hotColor.getBlue() - coldColor.getBlue()) * normalizedTemperature + coldColor.getBlue());
        return new Color(red, green, blue);
    }

    public void saveToFile(String path) {
        if (bufferedImage == null) {
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

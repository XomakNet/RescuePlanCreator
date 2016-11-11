package net.xomak.sga2;

import net.xomak.sga2.field.Field;
import net.xomak.sga2.field.Node;
import net.xomak.sga2.field.NodeType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by regis on 11.11.2016.
 */


public class PictureLoader {
    private static final int BLACK_COLOR = -16777216;
    private static final int WHITE_COLOR = -1;
    private static final int ORANGE_COLOR = -32985;
    private static final int RED_COLOR = -1237980;
    private static final int GREEN_COLOR = -14503604;

    private Field field;
    private BufferedImage image;

    private static NodeType getTypeByColor(final int color) {
        switch(color) {
            case BLACK_COLOR:
                return NodeType.WALL;
            case WHITE_COLOR:
                return NodeType.EMPTY;
            case ORANGE_COLOR:
                return NodeType.SOURCE;
            case RED_COLOR:
                return NodeType.START;
            case GREEN_COLOR:
                return NodeType.FINISH;
            default:
                throw new IllegalArgumentException("Incorrect color code: "+color);
        }
    }

    public PictureLoader(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();
        field = new Field(width, height);
        long nodeId = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = image.getRGB(x, y);
                NodeType type = getTypeByColor(color);
                field.addNode(type, nodeId++, x, y);
            }
        }
    }

    public Field getField() {
        return field;
    }

    public BufferedImage getImage() {
        return image;
    }
}



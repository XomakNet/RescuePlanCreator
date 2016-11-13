package net.xomak.sga2;

import net.xomak.sga2.field.Field;
import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.radiation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PictureLoader loader;
        try {
            loader = new PictureLoader("map.png");
            Field field = loader.getField();
            BufferedImage bufferedImage = loader.getImage();

            RadiationCalculator radiationCalculator = new RadiationCalculator(field, "radiation.dat", 8);
            double[][] radiation = radiationCalculator.get();

            RadiationMapCreator radiationMapCreator = new RadiationMapCreator(field, radiation);
            radiationMapCreator.saveToFile("radiation.png");

//            MinRadiationPathFinder minRadiationPathFinder = new SingleDijkstraMinRadiationPathFinder(field, radiation, field.getStartNode(),
//                    field.getFinishNode(), 1300);
            MinRadiationPathFinder minRadiationPathFinder = new DoubleDijkstraMinRadiationPathFinder(field, radiation, field.getStartNode(),
                    field.getFinishNode(), 1300);
            minRadiationPathFinder.run();

            Path minPath = minRadiationPathFinder.getPath();

            if (minPath != null) {
                System.out.println("Total radiation level: " + minRadiationPathFinder.getPath().getWeight());
                System.out.println("Path length: " + minRadiationPathFinder.getPathLength());
                System.out.println("Saving results to the image files...");

                BufferedImage radiationMapImage = radiationMapCreator.drawImage();

                drawMinimalRadiationPath(minRadiationPathFinder.getPath(), radiationMapImage);
                File pathImageFile = new File("path.png");

                ImageIO.write(radiationMapImage, "png", pathImageFile);
            } else {
                System.out.println("Sorry, no path, satisfying given requirements, could be found.");
            }
        } catch (IOException e) {
            System.err.println("IO Error: " + e);
        } catch (ClassNotFoundException e) {
            System.err.println("IO Error, while reading cache file: " + e);
        }

    }

    public static void drawMinimalRadiationPath(final Path path, final BufferedImage bufferedImage) {
        List<Edge> edges = path.getEdges();
        for (Edge edge : edges) {
            MinRadiationVertex mpv = (MinRadiationVertex) edge.getFrom();
            bufferedImage.setRGB(mpv.getNode().getX(), mpv.getNode().getY(), Color.BLACK.getRGB());
        }
    }
}

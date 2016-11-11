package net.xomak.sga2;

import net.xomak.sga2.algorithms.Dijkstra;
import net.xomak.sga2.field.Field;
import net.xomak.sga2.field.MinPathVertex;
import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.graph.Vertex;
import net.xomak.sga2.graph.VertexAnalyzer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by regis on 11.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        PictureLoader l = new PictureLoader("map.png");
        Field field = l.getField();
        BufferedImage bufferedImage = l.getImage();
        Vertex vertex = field.getStartNodeForMinimalPathSearch();
        Dijkstra d = new Dijkstra(vertex, true, new VertexAnalyzer() {
            @Override
            public boolean shouldConsider(final Vertex vertex, Double distance) {

                if(distance < 1300) {
                    return true;
                }
                else return false;
            }
        });

        RadiationCalculator radiationCalculator = new RadiationCalculator(field, "radiation.dat", 8);
        double[][] t = radiationCalculator.get();
        RadiationMapCreator radiationMapCreator = new RadiationMapCreator(field, t);
        radiationMapCreator.saveToFile("radiation.png");

        d.run();
        System.out.println(d.getDistanceFromStart(field.getStartVertex()));
        Path path = d.getPathToStartFrom(field.getStartVertex());
        List<Edge> edges = path.getEdges();
        for(Edge edge : edges) {
            MinPathVertex mpv = (MinPathVertex)edge.getFrom();
            bufferedImage.setRGB(mpv.getNode().getX(), mpv.getNode().getY(), 9999999);
        }


        File outputfile = new File("path.png");
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

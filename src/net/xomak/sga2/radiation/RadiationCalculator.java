package net.xomak.sga2.radiation;

import net.xomak.sga2.field.Field;
import net.xomak.sga2.field.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RadiationCalculator {

    private File cacheFile;
    private Field field;
    private int threadsNum;

    public RadiationCalculator(Field field, String cachePath, int threadsNum) {
        cacheFile = new File(cachePath);
        this.field = field;
        this.threadsNum = threadsNum;
    }

    static double calculate(int x1, int y1, int x2, int y2) {
        double dst = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return 100000 / Math.pow(dst, 2);
        //return 1000000 * Math.exp(-dst / 2);
    }

    private double[][] calculate(Field field, int threadsNum) {
        double result[][] = new double[field.getWidth()][field.getHeight()];
        Set<Node> sourceNodes = field.getSourceNodes();
        int xPerThread = (field.getWidth() / threadsNum) + 1;
        List<Thread> threads = new ArrayList<>(threadsNum);
        int x = 0;
        while (x < field.getWidth()) {
            int xTill = (x + xPerThread > field.getWidth()) ? field.getWidth() : x + xPerThread;
            RadiationCalculatorThread thread = new RadiationCalculatorThread(field.getWidth(),
                    field.getHeight(), result, x, xTill, sourceNodes);
            thread.start();
            threads.add(thread);
            x = xTill;
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public double[][] loadFromCache() throws IOException, ClassNotFoundException {
        double radiation[][] = null;
        if (cacheFile.exists() && !cacheFile.isDirectory()) {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(cacheFile));
            radiation = (double[][]) inputStream.readObject();
        }
        return radiation;
    }

    public double[][] calculateAndWrite() {
        double radiation[][] = calculate(field, threadsNum);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(cacheFile));
            outputStream.writeObject(radiation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return radiation;
    }

    public double[][] get() throws IOException, ClassNotFoundException {
        double[][] result = loadFromCache();
        if (result == null) {
            result = calculateAndWrite();
        }
        return result;
    }

}

class RadiationCalculatorThread extends Thread {
    private double[][] result;
    private int from;
    private int to;
    private int width;
    private int height;
    private Set<Node> sourceNodes;

    RadiationCalculatorThread(final int width, final int height, final double[][] result, final int from, final int to,
                              final Set<Node> sourceNodes) {
        this.result = result;
        this.from = from;
        this.to = to;
        this.sourceNodes = sourceNodes;
        this.width = width;
        this.height = height;
    }

    public void run() {
        for (int x = from; x < to; x++) {
            for (int y = 0; y < height; y++) {
                double currentResult = 0;
                for (Node node : sourceNodes) {
                    currentResult += RadiationCalculator.calculate(x, y, node.getX(), node.getY());
                }
                result[x][y] = currentResult;
            }
        }
    }
}
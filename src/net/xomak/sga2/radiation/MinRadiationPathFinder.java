package net.xomak.sga2.radiation;

import net.xomak.sga2.graph.Path;


public interface MinRadiationPathFinder {
    void run();

    Path getPath();

    double getPathLength();
}

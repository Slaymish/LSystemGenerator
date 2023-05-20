package com.example.lsystemgenerator.util;

import javafx.geometry.Point3D;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class ObjExporter {
    private StringBuilder objBuilder;
    private String fileName;

    public ObjExporter(String fileName) {
        this.fileName = fileName;
        objBuilder = new StringBuilder();
    }

    public void addVertices(Map<Integer, Point3D> vertices) {
        for (Point3D vertex : vertices.values()) {
            objBuilder.append("v " + vertex.getX() + " " + vertex.getY() + " " + vertex.getZ() + "\n");
        }
    }

    public void addEdges(Map<Edge, Integer[]> edges, Map<Integer, Point3D> points) {
        for (Edge edge : edges.keySet()) {
            Integer[] edgeIndices = edges.get(edge);
            objBuilder.append("l " + edgeIndices[0] + " " + edgeIndices[1] + "\n");
        }
    }

    public void writeToFile() throws IOException {
        FileWriter writer = new FileWriter(new File(fileName));
        writer.write(objBuilder.toString());
        writer.close();
    }
}

package com.example.lsystemgenerator.util;

import javafx.geometry.Point3D;

public record Edge(Point3D start, Point3D end) {
    @Override
    public String toString() {
        return "Edge{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}

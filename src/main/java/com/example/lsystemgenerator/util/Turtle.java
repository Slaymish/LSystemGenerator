package com.example.lsystemgenerator.util;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

import java.util.Stack;

public class Turtle {
    private Point3D position;
    private Point3D heading;

    private Stack<Point3D> stack = new Stack<>();

    public Turtle() {
        this.position = new Point3D(0, -1, 0);
        this.heading = new Point3D(0, 1, 0);
    }

    public void forward(double distance) {
        position = position.add(heading.multiply(distance));
    }

    public void turn(double angle) {
        Rotate rotate = new Rotate(angle, new Point3D(0, 0, 1));
        heading = rotate.transform(heading);
    }

    public void pitch(double angle) {
        Rotate rotate = new Rotate(angle, new Point3D(1, 0, 0));
        heading = rotate.transform(heading);
    }

    public void roll(double angle) {
        Rotate rotate = new Rotate(angle, new Point3D(0, 1, 0));
        heading = rotate.transform(heading);
    }

    public void push() {
        this.stack.push(this.position);
        this.stack.push(this.heading);
    }

    public void pop() {
        if (!this.stack.isEmpty()) {
            this.heading = this.stack.pop();
            this.position = this.stack.pop();
        }
    }

    public Point3D getPosition() {
        return this.position;
    }
}


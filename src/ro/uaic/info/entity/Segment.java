package ro.uaic.info.entity;

import java.io.Serializable;
import java.util.LinkedList;

public class Segment implements Serializable {
    Point x1, x2;

    public Segment() {
    }

    public Segment(Point x1, Point x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    public Point getX1() {
        return x1;
    }

    public void setX1(Point x1) {
        this.x1 = x1;
    }

    public Point getX2() {
        return x2;
    }

    public void setX2(Point x2) {
        this.x2 = x2;
    }
}

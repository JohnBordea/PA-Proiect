package ro.uaic.info.entity;

import java.io.Serializable;
import java.util.LinkedList;

public class Point implements Serializable {
    private int x;
    private int y;

    public LinkedList<Object> constrains = new LinkedList<>();

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

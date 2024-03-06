package cn.com.twoke.game.tank.entity;

import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.util.Objects;

public class Transform {

    private Point position;
    private Dimension size;

    public Transform(Point position, Dimension size) {
        this.position = position;
        this.size = size;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        if (Objects.isNull(position)) return;
        setPosition(position.x, position.y);
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        if (Objects.isNull(size)) return;
        setSize(size.width, size.height);
    }

    public void setSize(int width, int height) {
        this.size.width = width;
        this.size.height = height;
    }



}

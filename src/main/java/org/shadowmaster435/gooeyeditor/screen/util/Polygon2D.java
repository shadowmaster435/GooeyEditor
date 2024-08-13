package org.shadowmaster435.gooeyeditor.screen.util;

import org.joml.Intersectiond;
import org.joml.Vector2d;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Polygon2D {


    private final ArrayList<Vector2d> points = new ArrayList<>();


    public Polygon2D(Vector2d... points) {
        if (points.length < 3) {
            throw new RuntimeException("Attempted to create polygon with less than 3 points.");
        }
        this.points.addAll(Arrays.asList(points));
    }

    public ArrayList<Vector2d> getPoints() {
        return points;
    }

    public void addPoint(Vector2d point) {
        this.points.add(point);
    }

    public void removePoint(int index) {
        if (points.size() <= 3) {
            throw new RuntimeException("Attempted to remove point from polygon with 3 points.");
        }
        points.removeIf(a -> index < points.size());
    }

    public boolean intersectsPolygon(Polygon2D polygon2D) {
        return Intersectiond.testPolygonPolygon(points.toArray(new Vector2d[]{}), polygon2D.getPoints().toArray(new Vector2d[]{}));
    }

    public boolean pointInPolygon(Vector2d point) {
        Path2D path = new Path2D.Double();
        path.moveTo(points.getFirst().x, points.getFirst().y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.closePath();
        Point2D testPoint = new Point2D.Double(point.x, point.y);
        return path.contains(testPoint);
    }

}

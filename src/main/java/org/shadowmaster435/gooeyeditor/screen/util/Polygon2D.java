package org.shadowmaster435.gooeyeditor.screen.util;

import org.joml.*;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Polygon2D {


    private final ArrayList<Vector2d> points = new ArrayList<>();


    public Polygon2D(Rect2 rect) {
        points.add(new Vector2d(rect.getX(), rect.getY()));
        points.add(new Vector2d(rect.getX(), rect.getY() + rect.getHeight()));
        points.add(new Vector2d(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));
        points.add(new Vector2d(rect.getX() + rect.getWidth(), rect.getY()));

    }

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

    public boolean pointInPolygon(double x, double y) {
        Path2D path = new Path2D.Double();
        path.moveTo(points.getFirst().x, points.getFirst().y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.closePath();
        Point2D testPoint = new Point2D.Double(x,y);
        return path.contains(testPoint);
    }

    public boolean pointInPolygon(Vector2d point) {
        return pointInPolygon(point.x, point.y);
    }

    public void transform(Matrix4f matrix4f) {
        var mat3 = matrix4f.get3x3(new Matrix3d());
        for (Vector2d point : points) {
            var vec3 = new Vector3d(point.x, 0, point.y);
            mat3.transform(vec3);
            point.x = vec3.x;
            point.y = vec3.z;
        }
    }

}

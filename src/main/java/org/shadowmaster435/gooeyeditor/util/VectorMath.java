package org.shadowmaster435.gooeyeditor.util;

import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;

public class VectorMath {

    public static Vec3d invertDirection(Vec3d vec) {
        return new Vec3d(1.0 / vec.x, 1.0 / vec.y, 1.0 / vec.z);
    }

    public static Vec3d reflect(Vec3d direction, Vec3d normal) {
        return new Vec3d(2,2,2).multiply(normal).multiply(direction.dotProduct(normal)).subtract(direction);
    }

    public static float angleToPoint(Vector2f a, Vector2f b) {
        var vec = (new Vector2f(a).sub(b));
        return (float) Math.atan2(vec.y, vec.x);
    }

    public static Vec3d bounce(Vec3d direction, Vec3d normal) {
        return reflect(direction, normal).negate();
    }

    public static Vec3d project(Vec3d pos, Vec3d planeNormal) {
        return planeNormal.multiply(pos.dotProduct(planeNormal) / planeNormal.lengthSquared());
    }

    public static Vec3d directionTo(Vec3d from, Vec3d to) {
        return to.subtract(from).normalize();
    }
}

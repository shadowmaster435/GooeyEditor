package org.shadowmaster435.gooeyeditor.screen.util;

import org.joml.Vector2i;
import org.joml.Vector4i;

import java.awt.*;

public class Rect2 extends Rectangle {

    //region Initializers
    public Rect2() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public static Rect2 fromPoints(Vector2i a, Vector2i b) {
        var size = a.sub(b).absolute();
        return new Rect2(a, size);
    }

    public Rect2(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }
    public Rect2(Vector2i pos, Vector2i size) {
        this.x = pos.x;
        this.y = pos.y;
        this.width = size.x;
        this.height = size.y;
    }
    public Rect2(Vector2i pos, int w, int h) {
        this.x = pos.x;
        this.y = pos.y;
        this.width = w;
        this.height = h;
    }
    public Rect2(int x, int y, Vector2i size) {
        this.x = x;
        this.y = y;
        this.width = size.x;
        this.height = size.y;
    }
    public Rect2(Vector4i bounds) {
        this.x = bounds.x;
        this.y = bounds.y;
        this.width = bounds.z;
        this.height = bounds.w;
    }



    //endregion

    public Vector2i getRelativePos(int x, int y) {
        return new Vector2i(x - this.x, y - this.y);
    }

    public NinePatchRegion getPointPatchRegion(int x, int y, int padding) {
        for (NinePatchRegion region : NinePatchRegion.values()) {
            if (region == NinePatchRegion.NONE) {
                continue;
            }
            var region_rect = getPatchRegionRect(region, padding);
            if (region_rect.contains(x, y)) {
                return region;
            }
        }
        return NinePatchRegion.NONE;
    }

    public Rect2 getPointPatchRegionRect(int x, int y, int padding) {
        return getPatchRegionRect(getPointPatchRegion(x, y, padding), padding);
    }

    public Rect2 getPatchRegionRect(NinePatchRegion region, int padding) {
        int x = this.x;
        int y = this.y;
        int w = this.width;
        int h = this.height;
        switch (region) {
            case CENTER -> {
                x = this.x + padding;
                y = this.y + padding;
                w = this.width - padding * 2;
                h = this.height - padding * 2;
            }
            case TOP -> {
                x = this.x + padding;
                y = this.y;
                w = this.width - padding * 2;
                h = padding;
            }
            case BOTTOM -> {
                x = this.x + padding;
                y = this.y + (height - padding);
                w = this.width - padding * 2;
                h = padding;
            }
            case LEFT -> {
                x = this.x;
                y = this.y + padding;
                w = padding;
                h = this.height - padding * 2;
            }
            case RIGHT -> {
                x = this.x + (width - padding);
                y = this.y + padding;
                w = padding;
                h = this.height - padding * 2;
            }
            case TR -> {
                x = this.x + (width - padding);
                y = this.y;
                w = padding;
                h = padding;
            }
            case TL -> {
                x = this.x;
                y = this.y;
                w = padding;
                h = padding;
            }
            case BR -> {
                x = this.x + (width - padding);
                y = this.y + (height - padding);
                w = padding;
                h = padding;
            }
            case BL -> {
                x = this.x + padding;
                y = this.y + (height - padding);
                w = padding;
                h = padding;
            }
        }
        return new Rect2(x, y, w, h);
    }
    public enum NinePatchRegion {
        NONE,
        CENTER,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        TL,
        TR,
        BL,
        BR
    }
}

package org.shadowmaster435.gooeyeditor.screen.util;

import net.minecraft.util.math.MathHelper;

public class FloatRange implements Range {

    public float min;
    public float max;

    public FloatRange(float min, float max) {
        this.min = min;
        this.max = max;
    }
    public Float lerp(float delta) {
        return MathHelper.lerp(delta, min, max);
    }


}

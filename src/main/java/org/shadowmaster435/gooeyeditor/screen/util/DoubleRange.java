package org.shadowmaster435.gooeyeditor.screen.util;

import net.minecraft.util.math.MathHelper;

public class DoubleRange implements Range {

    public double min;
    public double max;

    public DoubleRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public Double lerp(float delta) {
        return MathHelper.lerp(delta, min, max);
    }


}

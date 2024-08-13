package org.shadowmaster435.gooeyeditor.screen.util;

import net.minecraft.util.math.MathHelper;

public class IntRange implements Range {

    public int min;
    public int max;

    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }


    public Integer lerp(float delta) {
        return MathHelper.lerp(delta, min, max);
    }
}

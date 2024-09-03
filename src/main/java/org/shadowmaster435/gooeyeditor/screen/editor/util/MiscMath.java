package org.shadowmaster435.gooeyeditor.screen.editor.util;

import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;

public interface MiscMath {

    default Vector2i gridify(int i, int x_size, int y_size) {
        int x = i % x_size;
        int y = i / y_size;
        return new Vector2i(x, y);
    }
    default int degridify(int x, int y, int x_size, int y_size) {
        return (x % x_size) + (y * y_size);
    }

}

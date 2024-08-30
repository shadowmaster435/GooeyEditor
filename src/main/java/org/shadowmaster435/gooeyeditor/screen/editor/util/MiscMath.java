package org.shadowmaster435.gooeyeditor.screen.editor.util;

import org.joml.Vector2i;

public interface MiscMath {

    default Vector2i gridify(int i, int x_size, int y_size) {
        int x = i % x_size;
        int y = i / y_size;
        return new Vector2i(x, y);
    }


}

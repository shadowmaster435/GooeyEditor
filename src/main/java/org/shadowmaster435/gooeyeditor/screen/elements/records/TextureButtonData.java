package org.shadowmaster435.gooeyeditor.screen.elements.records;

import net.minecraft.util.Identifier;

public record TextureButtonData(Identifier off_texture, Identifier off_hovered_texture, Identifier on_texture, Identifier on_hovered_texture) {

    /**
     * Creates a {@code TextureButtonData} with no hover textures.
     */
    public static TextureButtonData onOff(Identifier on, Identifier off) {
        return new TextureButtonData(off, off, on, on);
    }

}

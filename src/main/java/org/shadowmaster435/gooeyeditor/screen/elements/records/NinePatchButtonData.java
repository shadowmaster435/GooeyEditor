package org.shadowmaster435.gooeyeditor.screen.elements.records;

import net.minecraft.util.Identifier;

public record NinePatchButtonData(NinePatchTextureData off_texture, NinePatchTextureData off_hovered_texture, NinePatchTextureData on_texture, NinePatchTextureData on_hovered_texture) {

    /**
     * Creates a {@code NinePatchButtonData} with no hover textures.
     */
    public static NinePatchButtonData onOff(NinePatchTextureData on, NinePatchTextureData off) {
        return new NinePatchButtonData(off, off, on, on);
    }

    /**
     * Creates a {@code NinePatchButtonData} one texture.
     */
    public static NinePatchButtonData single(NinePatchTextureData texture) {
        return new NinePatchButtonData(texture, texture, texture, texture);
    }
}

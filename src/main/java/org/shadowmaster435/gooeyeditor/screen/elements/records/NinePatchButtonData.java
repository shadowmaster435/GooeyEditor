package org.shadowmaster435.gooeyeditor.screen.elements.records;

import net.minecraft.util.Identifier;

public record NinePatchButtonData(NinePatchTextureData off_texture, NinePatchTextureData off_hovered_texture, NinePatchTextureData on_texture, NinePatchTextureData on_hovered_texture) {

    /**
     * Creates a {@code TextureButtonData} with no hover textures.
     */
    public static NinePatchButtonData onOff(NinePatchTextureData on, NinePatchTextureData off) {
        return new NinePatchButtonData(off, off, on, on);
    }

}

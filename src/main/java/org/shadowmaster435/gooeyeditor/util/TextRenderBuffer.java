package org.shadowmaster435.gooeyeditor.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawContext;

public class TextRenderBuffer extends VertexBuffer {

    public TextRenderBuffer(Usage usage) {
        super(usage);
    }

    public void cache(DrawContext context, String string) {
        var renderer = MinecraftClient.getInstance().textRenderer;
    }

}

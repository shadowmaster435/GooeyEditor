package org.shadowmaster435.gooeyeditor.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;

public class DrawBuffer {
    
    public final Framebuffer framebuffer;
    public final Framebuffer previousBuffer;

    private final DrawContext context;
    
    public DrawBuffer(DrawContext context) {
        var window = MinecraftClient.getInstance().getWindow();
        this.framebuffer = new SimpleFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), true, true);
        this.previousBuffer = new SimpleFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), true, true);
        this.context = context;
    }


    public void beginSecondaryDrawing() {
        previousBuffer.beginWrite(false);
        context.draw();
        previousBuffer.endWrite();
    }

    public void endSecondaryDrawing() {
        framebuffer.beginWrite(false);
        context.draw();
        framebuffer.endWrite();
        framebuffer.beginRead();
        var shader = GooeyEditorClient.getDrawBuffer();
        RenderSystem.setShader(GooeyEditorClient::getDrawBuffer);
        RenderSystem.setShaderTexture(1, previousBuffer.getColorAttachment());
        RenderSystem.setShaderTexture(2, framebuffer.getDepthAttachment());
        framebuffer.endRead();
    }



}

package org.shadowmaster435.gooeyeditor.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;

import java.util.Objects;

// unused for now
public class ColorPicker extends GuiElement {

    public int hue = 0;

    public ColorPicker(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }

    public ColorPicker(int x, int y, int w, int h, boolean editMode) {
        super(x, y, editMode);
        getRect().setBounds(x, y, w, h);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
     //   drawTexture(context, sv_gradient, 256, 256);
        drawGradient(context);

    }



    public void drawGradient(DrawContext context) {
        var x = getX();
        var y = getY();
        var width = getWidth();
        var height = getHeight();
        RenderSystem.setShader(GooeyEditorClient::getHueGradient);
        var shader = GooeyEditorClient.getHueGradient();
        shader.getUniform("ProjMat").set(RenderSystem.getProjectionMatrix());
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        Objects.requireNonNull(shader.getUniform("Hue")).set(Math.clamp(hue / 360f, 0, 1));
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, Identifier.of(GooeyEditor.id, "textures/gui/black.png"));
        RenderSystem.disableCull();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y, (float) layer).texture(0, 0).color(1f, 1f, 1f, 1f);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y + height, (float)layer).texture(0, 1).color(1f, 1f, 1f, 1f);;
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y + height, (float)layer).texture(1, 1).color(1f, 1f, 1f, 1f);;
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y, (float)layer).texture(1, 0).color(1f, 1f, 1f, 1f);;
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public static Vector3i hsvToRgb(float hue, float sat, float val) {
        float r, g, b;
        float saturation = Math.clamp(sat, 0, 1);
        float value = Math.clamp(val, 0, 1);

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        if (h == 0) {
            r = value;
            g = t;
            b = p;
        } else if (h == 1) {
            r = q;
            g = value;
            b = p;
        } else if (h == 2) {
            r = p;
            g = value;
            b = t;
        } else if (h == 3) {
            r = p;
            g = q;
            b = value;
        } else if (h == 4) {
            r = t;
            g = p;
            b = value;
        } else if (h <= 6) {
            r = value;
            g = p;
            b = q;
        } else {
            throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
        var a = new Vector3f(r, g, b).mul(255);
        return new Vector3i((int) a.x, (int) a.y, (int) a.z);
    }

    @Override
    public Property[] getProperties() {
        var hue = new Property("Hue", "hue", "hue", Integer.class);

        return new Property[]{hue};
    }
}

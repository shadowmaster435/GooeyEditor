package org.shadowmaster435.gooeyeditor.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;

import java.util.Objects;

public class GuiRadialTexture extends GuiElement {

    public Identifier texture;

    public float angle = 360;
    public boolean pixelate = false;


    public GuiRadialTexture(Identifier texture, int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
        this.texture = texture;
    }
    public GuiRadialTexture(int x, int y, boolean editMode) {
        super(x, y, 32,32, editMode);
        this.texture = Identifier.of(GooeyEditor.id, "textures/gui/progress_circle.png");
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        var x = getGlobalX();
        var y = getGlobalY();
        var width = getWidth();
        var height = getHeight();
        Objects.requireNonNull(GooeyEditorClient.getRadialTexture().getUniform("Angle")).set(angle);
        Objects.requireNonNull(GooeyEditorClient.getRadialTexture().getUniform("Pixelate")).set((pixelate) ? 1 : 0);

        RenderSystem.setShader(GooeyEditorClient::getRadialTexture);

        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, texture);

        RenderSystem.disableCull();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y, (float) layer).texture(0, 0).color(1f, 1f, 1f, 1f);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y + height, (float)layer).texture(0, 1).color(1f, 1f, 1f, 1f);
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y + height, (float)layer).texture(1, 1).color(1f, 1f, 1f, 1f);
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y, (float)layer).texture(1, 0).color(1f, 1f, 1f, 1f);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
        super.preTransform(context, mouseX, mouseY, angle);
    }


    @Override
    public Property[] getProperties() {
        var angle = new Property("Angle", "angle", "angle", Float.class);
        var pixelate = new Property("Pixelate", "pixelate", "pixelate", Boolean.class);

        var texture = new Property("Texture", "texture", "texture", Identifier.class);

        return new Property[]{texture,angle,pixelate};
    }

    public float getangle() {
        return angle;
    }

    public void setangle(float angle) {
        this.angle = angle;
    }

    public Identifier getTexture() {
        return texture;
    }

}
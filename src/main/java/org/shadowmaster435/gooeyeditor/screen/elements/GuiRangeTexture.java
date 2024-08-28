package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.shadowmaster435.gooeyeditor.screen.util.Range;

import java.lang.constant.Constable;

public class GuiRangeTexture extends ParentableWidgetBase {

    public Identifier texture;
    public float delta = 0;
    public int texture_width = 0;
    public int texture_height = 0;


    public GuiRangeTexture(Identifier texture, int x, int y, int w, int h, int texture_width, int texture_height, boolean editMode) {
        super(x, y, w, h, editMode);
        this.texture = texture;
        this.texture_width = texture_width;
        this.texture_height = texture_height;
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        int progress = MathHelper.lerp(this.delta, 0, texture_width);
        int width_delta_progress = MathHelper.lerp((float) progress / texture_width, 0, getWidth());

        context.drawTexture(texture, getX(), getY(), width_delta_progress, getHeight(), 0f, 0f, (int) (texture_width * this.delta), texture_height, texture_width, texture_height);
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var texture_width = new Property("Texture Width", "texture_width", "texture_width", Integer.class);
        var texture_height = new Property("Texture Height", "texture_height", "texture_height", Integer.class);
        var delta = new Property("Delta", "delta", "delta", Float.class);
        var texture = new Property("Texture", "texture", "texture", Identifier.class);

        return new Property[]{texture,texture_width,texture_height,delta};
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public Identifier getTexture() {
        return texture;
    }

}

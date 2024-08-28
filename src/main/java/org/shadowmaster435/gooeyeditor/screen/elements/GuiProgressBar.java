package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GuiProgressBar extends GuiRangeTexture {

    public Identifier empty_texture;

    public GuiProgressBar(Identifier texture, Identifier empty_texture, int x, int y, int w, int h, int texture_width, int texture_height, boolean editMode) {
        super(texture, x, y, w, h, texture_width, texture_height, editMode);
        this.empty_texture = empty_texture;
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        int progress = MathHelper.lerp(this.delta, 0, texture_width);
        int width_delta_progress = MathHelper.lerp((float) progress / texture_width, 0, getWidth());

        context.drawTexture(empty_texture, getX() + width_delta_progress, getY(), getWidth() - width_delta_progress, getHeight(), 0f, 0f, (int) (texture_width * this.delta), texture_height, texture_width, texture_height);
        super.preTransform(context, mouseX, mouseY, delta);
    }
    @Override
    public Property[] getProperties() {

        var empty_texture = new Property("Empty Texture", "empty_texture", "empty_texture", Identifier.class);

        return mergeProperties(super.getProperties(), new Property[]{empty_texture});
    }
}

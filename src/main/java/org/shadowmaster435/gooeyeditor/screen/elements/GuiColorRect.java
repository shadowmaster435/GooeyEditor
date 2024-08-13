package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;

public class GuiColorRect extends GuiElement {

    private int rgb = ColorHelper.Argb.getArgb(255,255,255,255);

    public GuiColorRect(int r, int g, int b, int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
        this.rgb = ColorHelper.Argb.getArgb(r,g,b);

    }

    public GuiColorRect(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), rgb);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}

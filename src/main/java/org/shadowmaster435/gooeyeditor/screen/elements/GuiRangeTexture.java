package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.screen.util.Range;

public class GuiRangeTexture extends GuiElement {

    private Identifier texture;
    private float delta = 0;

    public Range range;


    public GuiRangeTexture(Identifier texture, Range range, int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
        this.texture = texture;
        this.range = range;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        push(context);
        int progress = range.lerp(delta);
        context.drawGuiTexture(texture, 0, progress,0, getHeight(), getX(), getY(), layer, getWidth(), getHeight());
        super.render(context,mouseX,mouseY,delta);
        pop(context);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public Range getRange() {
        return range;
    }

    public Identifier getTexture() {
        return texture;
    }

}

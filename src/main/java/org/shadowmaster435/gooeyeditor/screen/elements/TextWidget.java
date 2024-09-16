package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

public class TextWidget extends GuiElement {

    public String text = "";
    public boolean draw_shadow = false;
    public int a = 255;
    public int r = 255;
    public int g = 255;
    public int b = 255;


    public TextWidget(int x, int y, boolean editMode) {
        super(x, y, 0, 0, editMode);
        text = "Text";
    }




    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        super.preTransform(context, mouseX, mouseY, delta);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.of(text), getX(), getY(), ColorHelper.Argb.getArgb(a,r,g,b), draw_shadow);
        setHeight(Math.max(8, getHeight()));
        setWidth(Math.max(MinecraftClient.getInstance().textRenderer.getWidth(text), getWidth()));
    }

    @Override
    public Property[] getProperties() {
        var text = new Property("Text", "text", "text", String.class);
        var draw_shadow = new Property("Draw Shadow", "draw_shadow", "draw_shadow", Boolean.class);

        var a = new Property("Alpha", "a", "a", Integer.class);
        var r = new Property("Red", "r", "r", Integer.class);
        var g = new Property("Green", "g", "g", Integer.class);
        var b = new Property("Blue", "b", "b", Integer.class);


        return new Property[]{r, g, b, a, text, draw_shadow};
    }
}

package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.joml.Vector2i;

import java.util.ArrayList;

public class DropDownWidget extends TextButtonWidget {

    private int spacing = 0;
    private ArrayList<TextButtonWidget> entries = new ArrayList<>();

    public DropDownWidget(int x, int y, Text message, boolean editMode) {
        super(x, y, message, editMode);
        toggle_mode = true;
    }
    public DropDownWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        toggle_mode = true;
    }
    public void setSpacing(int amount) {
        spacing = amount;
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        var color = (!pressed) ? ColorHelper.Argb.getArgb(255,255,255) : ColorHelper.Argb.getArgb(127,127,127);
        for (TextButtonWidget widget : entries) {
            if (pressed) {
                context.getMatrices().push();

                widget.render(context, mouseX, mouseY, delta);
                context.getMatrices().pop();
            }
            widget.setActive(pressed);
            widget.setVisible(pressed);

        }

        context.drawText(MinecraftClient.getInstance().textRenderer, message, getX(),getY(), color, true);

    }

    @Override
    public Vector2i getSize() {
        if (pressed) {
            var max_size_x = getWidth();
            var size_y = getHeight();
            for (TextButtonWidget widget : entries) {
                size_y += spacing + widget.getHeight();
                max_size_x = Math.max(max_size_x, widget.getWidth());
            }
            size_y -= spacing;
            return new Vector2i(max_size_x, size_y);
        } else {
            return super.getSize();
        }
    }

    public void removeEntry(int index) {
        entries.remove(index);
    }

    public void removeEntry(TextButtonWidget widget) {
        entries.remove(widget);
    }

    public void addEntry(TextButtonWidget widget) {
        entries.add(widget);
        widget.setActive(false);
        widget.setX(getX());
        widget.setY(getY() + ((spacing + getHeight()) * entries.size()));

    }



    public void toggleEntries() {
        for (TextButtonWidget widget : entries) {
            widget.setActive(!widget.isActive());
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {

        super.mouseMoved(mouseX, mouseY);
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            toggleEntries();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

}

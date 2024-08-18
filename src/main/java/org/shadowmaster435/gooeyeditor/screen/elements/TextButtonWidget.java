package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextButtonWidget extends GuiButton {

    public Text message = Text.of("");
    public TextButtonWidget(int x, int y, String message, boolean editMode) {
        super(x, y, 0, 0, editMode);

        this.message = Text.of(message);
    }

    public TextButtonWidget(int x, int y, Text message, boolean editMode) {
        super(x, y, 0, 0, editMode);

        this.message = message;
    }
    public TextButtonWidget(int x, int y, boolean editMode) {
        super(x, y, 0, 0, editMode);
    }


    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        var color = (!pressed) ? ColorHelper.Argb.getArgb(255,255,255) : ColorHelper.Argb.getArgb(127,127,127);
        context.drawText(MinecraftClient.getInstance().textRenderer, message, getX(), getY(), color, true);
        setHeight(Math.max(8, getHeight()));

        setWidth(Math.max(MinecraftClient.getInstance().textRenderer.getWidth(message), getWidth()));
        if (!toggle_mode) {
            pressed = false;
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    public Vector2i getSize() {
        return new Vector2i(getWidth(), getHeight());
    }




}

package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextButtonWidget extends GuiButton {

    public String text = "Text";
    public TextButtonWidget(int x, int y, String message, boolean editMode) {
        super(x, y, 0, 0, editMode);

        this.text = message;
    }

    public TextButtonWidget(int x, int y, boolean editMode) {
        super(x, y, 0, 0, editMode);
    }

    private boolean drawnText = false;

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        var color = (!pressed) ? ColorHelper.Argb.getArgb(255,255,255) : ColorHelper.Argb.getArgb(127,127,127);

        context.drawText(MinecraftClient.getInstance().textRenderer, text, getGlobalX(), getGlobalY(), color, false);
        setHeight(Math.max(8, getHeight()));

        setWidth(Math.max(MinecraftClient.getInstance().textRenderer.getWidth(text), getWidth()));
        if (!toggle_mode) {
            pressed = false;
        }

        super.preTransform(context, mouseX, mouseY, delta);
    }

    public Vector2i getSize() {
        return new Vector2i(getWidth(), getHeight());
    }




}

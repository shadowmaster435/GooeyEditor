package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

public class DraggableElementReferenceButton extends GuiElement {
    public String text = "";
    public final GuiEditorScreen screen;
    public final WidgetTree tree;
    public final GuiElement referenced;
    public boolean hovering = false;
    private double mouseDragX = 0;
    private double mouseDragY = 0;
    private int mouseClickX = 0;
    private int mouseClickY = 0;

    private boolean dragging = false;
    protected DraggableElementReferenceButton(int x, int y, String text, GuiElement referenced, WidgetTree tree, GuiEditorScreen screen, boolean editMode) {
        super(x, y, 0, 0, editMode);
        this.text = text;
        this.referenced = referenced;
        this.screen = screen;
        this.tree = tree;
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        hovering = tree.hoveredButton != null && tree.hoveredButton == this;
        if (hovering && !dragging) {

            context.drawText(MinecraftClient.getInstance().textRenderer, text, getGlobalX(), getGlobalY(), ColorHelper.Argb.getArgb(255, 0, 255, 0), false);
        } else {

            if (dragging) {
                if (referenced.selectable) {
                    context.drawText(MinecraftClient.getInstance().textRenderer, referenced.name, (int) mouseDragX + mouseClickX, (int) mouseDragY + mouseClickY, Colors.WHITE, false);
                }
                context.drawText(MinecraftClient.getInstance().textRenderer, text, getGlobalX(), getGlobalY(), ColorHelper.Argb.getArgb(127, 150, 150, 150), false);

            } else {
                context.drawText(MinecraftClient.getInstance().textRenderer, text, getGlobalX(), getGlobalY(), ColorHelper.Argb.getArgb(255, 255, 255, 255), false);
            }
        }
        setHeight(Math.max(8, getHeight()));
        setWidth(Math.max(MinecraftClient.getInstance().textRenderer.getWidth(text), getWidth()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY) && button == 0) {
            dragging = true;
            mouseDragX = mouseX;
            mouseDragY = mouseY;
            mouseClickX = (int) -Math.abs(getGlobalX() - mouseX);
            mouseClickY = (int) -Math.abs(getGlobalY() - mouseY);

        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void transfer(DraggableElementReferenceButton button) {
        button.referenced.reparent(referenced);

        tree.regenTree();
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;

        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0) {
            mouseDragX = mouseX;
            mouseDragY = mouseY;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}

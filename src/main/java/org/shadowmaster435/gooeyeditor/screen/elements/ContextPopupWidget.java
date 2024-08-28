package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.elements.container.PopupContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ScrollableListContainer;

public class ContextPopupWidget extends PopupContainer {

    public ScrollableListContainer list;
    public ScrollbarWidget scrollbarWidget;
    private static final Identifier border = Identifier.of(GooeyEditor.id, "textures/gui/gray_filled_white_box.png");


    public ContextPopupWidget(int w, int h, boolean editMode) {
        super(w, h, editMode);
        this.scrollbarWidget = new ScrollbarWidget(0,0, 12, getHeight() - 2, false);
        this.list = new ScrollableListContainer(0, 0, w, h, scrollbarWidget,2, editMode);
        scrollbarWidget.layer = layer + 1;
        list.layer = layer + 1;
        super.addElement(scrollbarWidget);
        super.addElement(list);

    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        list.setSize(getSize());
        list.setPosition(2, 2);
        list.scissor(true);
        list.setScissor(getX() + 2, getY() + 2, getX() + getWidth() - 2, getY() + getHeight() - 2);
        scrollbarWidget.setPosition(getX() + getWidth() - 13, getY() + 1);
        scrollbarWidget.setSize(12, getHeight() - 2);
        drawNinePatchTexture(context, getRect(), border, 1, 16, 16);
        super.preTransform(context, mouseX, mouseY, delta);
    }

    public void tryClose(int mouseX, int mouseY) {
        var child = getHoveredChild(mouseX, mouseY);
        var clicked = !isMouseOver(mouseX, mouseY) && child != null;
        var clicked_out = !isMouseOver(mouseX, mouseY);
        if (clicked || clicked_out) {
            close();
        }
    }

    @Override
    public void addElement(GuiElement element) {
        element.layer = layer + 1;
        list.addElement(element);
    }

    @Override
    public void removeElement(int index) {
        list.removeElement(index);
    }

    @Override
    public void removeElement(GuiElement element) {
        list.removeElement(element);
    }

    @Override
    public GuiElement getHoveredChild(int mouseX, int mouseY) {
        return (isOpen()) ? list.getHoveredChild(mouseX, mouseY) : null;
    }
}
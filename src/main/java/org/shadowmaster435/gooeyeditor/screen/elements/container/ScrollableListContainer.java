package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.ScrollbarWidget;


/**
 * Scrollable List container variant see {@link ListContainer}.
 * To access scroll functionality a scrollbar must be provided.
 */
public class ScrollableListContainer extends ListContainer {

    private ScrollbarWidget scrollbar;
    private int scroll_offset = 0;
    private double previous_scroll_pos = 0;
    public boolean reversed = false;

    public ScrollableListContainer(int x, int y, int w, int h, ScrollbarWidget scrollbar, int element_spacing, boolean editMode) {
        super(x, y, w, h, element_spacing, editMode);
        this.scrollbar = scrollbar;
    }

    public void setScrollbar(ScrollbarWidget scrollbar) {
        this.scrollbar = scrollbar;
    }

    public ScrollbarWidget getScrollbar() {
        return scrollbar;
    }
    @Override
    public void arrange() {
        var y_offset = 0;

        for (SealedGuiElement element : this) {
            if (element instanceof ScrollbarWidget) {
                continue;
            }
            element.setX(0);
            if (reversed) {
                element.setY(y_offset - scroll_offset);
            } else {
                element.setY(y_offset + scroll_offset);
            }
            y_offset += getElementSpacing() + element.getHeight();
        }
        listHeight = y_offset;
      //  setHeight(listHeight);
    }




    @Override
    public void postTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        previous_scroll_pos = scrollbar.grabber_pos;

        var scroll_delta = scrollbar.getGrabberDelta();
        scrollbar.render(context, mouseX, mouseY, delta);
        scroll_offset = MathHelper.lerp(scroll_delta, 0, getListHeight() - getHeight());
        if (previous_scroll_pos != scrollbar.grabber_pos) {
            arrange();
        }
        super.postTransform(context, mouseX, mouseY, delta);
    }
}


package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.ScrollbarWidget;


/**
 * Scrollable List container variant see {@link ListContainer}.
 * To access scroll functionality a scrollbar must be provided.
 */
public class ScrollableContainer extends BaseContainer {

    private ScrollbarWidget scrollbar;
    private int scroll_offset = 0;
    private double previous_scroll_pos = 0;
    private int element_spacing = 12;
    private int max_length = 0;

    public ScrollableContainer(int x, int y, int w, int h, int max_length, ScrollbarWidget scrollbar, int element_spacing, boolean editMode) {
        super(x, y, w, h, editMode);
        this.scrollbar = scrollbar;
        this.max_length = max_length;
        this.element_spacing = element_spacing;

    }

    public void setScrollbar(ScrollbarWidget scrollbar) {
        this.scrollbar = scrollbar;
    }

    public ScrollbarWidget getScrollbar() {
        return scrollbar;
    }
    @Override
    public void arrange() {
        var max_y = 0;
        for (GuiElement element : this) {
            element.offset(true);
            element.setOffsetY(scroll_offset);
            max_y += element.getHeight() + element_spacing;

        }
        setHeight((max_y - max_length) + getY());
    }
    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (scrollbar != null) {
            var scroll_delta = scrollbar.getGrabberDelta();
            scrollbar.render(context, mouseX, mouseY, delta);
            scroll_offset = MathHelper.lerp(scroll_delta, 0,  getHeight());
            if (previous_scroll_pos != scrollbar.grabber_pos) {
                arrange();
            }
            previous_scroll_pos = scrollbar.grabber_pos;
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }
}


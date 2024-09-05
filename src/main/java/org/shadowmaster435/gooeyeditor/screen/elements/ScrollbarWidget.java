package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.elements.container.BaseContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchTextureData;
import org.shadowmaster435.gooeyeditor.screen.elements.records.ScrollbarWidgetData;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;
import org.shadowmaster435.gooeyeditor.util.InputHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ScrollbarWidget extends NinePatchTexture {

    public int grabber_texture_width;
    public int grabber_texture_height;
    public int grabber_edge_thickness;
    public int grabber_click_offset = 0;
    private boolean grabbed = false;
    public boolean horizontal = false;


    public double scroll_delta = 1;

    public static final NinePatchTextureData DEFAULT_SCROLLBAR_BG = new NinePatchTextureData(16,16,5,Identifier.of(GooeyEditor.id, "textures/gui/slot.png"));
    public static final ScrollbarWidgetData DEFAULT_SCROLLBAR = new ScrollbarWidgetData(DEFAULT_SCROLLBAR_BG,Identifier.of(GooeyEditor.id, "textures/gui/scroll_grabber.png"),16,16,5, 16);


    public Identifier grabber_texture;
    public int grabber_length = 8;
    public double grabber_pos = 0;


    @Override
    public Property[] getProperties() {
        var texture_width = new Property("Grabber Texture Width", "grabber_texture_width", "grabber_texture_width", Integer.class);
        var texture_height = new Property("Grabber Texture Height", "grabber_texture_height", "grabber_texture_height", Integer.class);
        var edge_thickness = new Property("Grabber Edge Thickness", "grabber_edge_thickness", "grabber_edge_thickness", Integer.class);
        var grabber_length = new Property("Grabber Length", "grabber_length", "grabber_length", Integer.class);
        var horizontal = new Property("Horizontal", "horizontal", "horizontal", Boolean.class);
        var texture = new Property("Grabber Texture", "grabber_texture", "grabber_texture", Identifier.class);
        return mergeProperties(super.getProperties(), new Property[]{horizontal, texture_width, texture_height, edge_thickness, grabber_length, texture});
    }

    /**
     * Create Scrollbar with default look.
     */
    public ScrollbarWidget(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, DEFAULT_SCROLLBAR.bg(), editMode);
        this.grabber_length = Math.clamp(grabber_length, DEFAULT_SCROLLBAR.grabber_texture_height(), h);
        this.grabber_texture = DEFAULT_SCROLLBAR.grabber_texture();
        this.grabber_texture_width = DEFAULT_SCROLLBAR.grabber_texture_width();
        this.grabber_texture_height = DEFAULT_SCROLLBAR.grabber_texture_height();
        this.grabber_edge_thickness = DEFAULT_SCROLLBAR.grabber_edge_thickness();
    }

    public ScrollbarWidget(int x, int y, int w, int h, ScrollbarWidgetData data, boolean editMode) {
        super(x, y, w, h, data.bg(), editMode);
        this.grabber_length = Math.clamp(grabber_length, Math.min(data.grabber_texture_height(), h),Math.max(data.grabber_texture_height(), h));
        this.grabber_texture = data.grabber_texture();
        this.grabber_texture_width = data.grabber_texture_width();
        this.grabber_texture_height = data.grabber_texture_height();
        this.grabber_edge_thickness = data.grabber_edge_thickness();
    }

    public ScrollbarWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        var data = DEFAULT_SCROLLBAR;

        this.grabber_length = Math.clamp(grabber_length, Math.min(data.grabber_texture_height(), 16),Math.max(data.grabber_texture_height(), 16));
        this.grabber_texture = data.grabber_texture();
        this.grabber_texture_width = data.grabber_texture_width();
        this.grabber_texture_height = data.grabber_texture_height();
        this.grabber_edge_thickness = data.grabber_edge_thickness();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (isEditMode()) {
            super.render(context, mouseX, mouseY, delta);
            return;
        }
        if (!InputHelper.isLeftMouseHeld) {
            grabbed = false;
        }
        tryGrab(mouseX, mouseY);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        super.preTransform(context, mouseX, mouseY, delta);
        drawNinePatchTexture(context, getGlobalRect(), texture, grabber_edge_thickness, grabber_texture_width, grabber_texture_height);

        drawNinePatchTexture(context, getGrabberRect(), grabber_texture, grabber_edge_thickness, grabber_texture_width, grabber_texture_height);
    }

    public boolean mouseOverScrollBar(int mouseX, int mouseY) {
        return isMouseOver(mouseX, mouseY) && getGrabberRect().contains(mouseX, mouseY);
    }

    public float getGrabberDelta() {
        return (float) grabber_pos / (getHeight() - grabber_texture_height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseOverScrollBar((int) mouseX, (int) mouseY) && button == 0) {
            grabbed = true;
            grabber_click_offset = getGrabberRect().getRelativePos(0, (int) mouseY).y;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        grabbed = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean canScroll(int mouseX, int mouseY) {
        return (parent instanceof BaseContainer e) ? e.isMouseOver(mouseX,mouseY) : isMouseOver(mouseX,mouseY);
    }

    public Rect2 getGrabberRect() {
        return new Rect2(getGlobalX(), (int) (grabber_pos + getGlobalY()), getWidth(), grabber_length);
    }

    public double getGrabberPosition() {
        return grabber_pos;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        tryScroll((int) mouseX, (int) mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public void tryScroll(int mouseX, int mouseY, double verticalAmount) {
        if (canScroll(mouseX, mouseY)) {
            grabber_pos = Math.clamp(grabber_pos - (verticalAmount * scroll_delta), 0, getHeight() - getGrabberRect().height);
        }
    }

    public void tryGrab(double mouseX, double mouseY) {
        if (grabbed) {
            var h = Math.clamp((int) (mouseX) - getGlobalX() - grabber_click_offset, 0, getHeight() - getGrabberRect().height);
            var v = Math.clamp((int) (mouseY) - getGlobalY() - grabber_click_offset, 0, getHeight() - getGrabberRect().height);

            grabber_pos = (horizontal) ? h : v;
        }
    }
}

package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.elements.records.TextureButtonData;

public class TextureButtonWidget extends GuiButton {

    public Identifier off;
    public Identifier off_hovered;
    public Identifier on;
    public Identifier on_hovered;




    public TextureButtonWidget(int x, int y, int w, int h, TextureButtonData data, boolean editMode) {
        super(x, y, w, h, editMode);
        this.off = data.off_texture();
        this.off_hovered = data.off_hovered_texture();
        this.on = data.on_texture();
        this.on_hovered = data.on_hovered_texture();
    }
    public TextureButtonWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        var data = CHECKBOX;
        this.off = data.off_texture();
        this.off_hovered = data.off_hovered_texture();
        this.on = data.on_texture();
        this.on_hovered = data.on_hovered_texture();
    }

    public void setData(TextureButtonData data) {
        this.off = data.off_texture();
        this.off_hovered = data.off_hovered_texture();
        this.on = data.on_texture();
        this.on_hovered = data.on_hovered_texture();
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {

        if (isMouseOver(mouseX, mouseY)) {
            if (pressed) {
                drawTexture(context, on_hovered, getWidth(), getHeight());
            } else {
                drawTexture(context, off_hovered, getWidth(), getHeight());
            }
        } else {
            if (pressed) {
                drawTexture(context, on, getWidth(), getHeight());
            } else {
                drawTexture(context, off, getWidth(), getHeight());
            }
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var off = new Property("Off Texture", "off", "off", Identifier.class);
        var off_hovered = new Property("Off Hovered Texture", "off_hovered", "off_hovered", Identifier.class);
        var on = new Property("On Texture", "on", "on", Identifier.class);
        var on_hovered = new Property("On Hovered Texture", "on_hovered", "on_hovered", Identifier.class);
        return mergeProperties(super.getProperties(), new Property[]{off, off_hovered, on, on_hovered});
    }
    // preset
    public static final TextureButtonData CHECKBOX = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/checkbox_empty.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/checkbox_empty_hovered.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/checkbox.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/checkbox_hovered.png")
    );

    public static final TextureButtonData LARGE_ARROW_LEFT = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_left.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_left_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_left.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_left_selected.png")
    );

    public static final TextureButtonData LARGE_ARROW_RIGHT = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_right.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_right_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_right.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_right_selected.png")
    );

    public static final TextureButtonData LARGE_ARROW_UP = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_up.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_up_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_up.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_up_selected.png")
    );

    public static final TextureButtonData LARGE_ARROW_DOWN = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_down.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_down_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_down.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/large_arrow_down_selected.png")
    );

    public static final TextureButtonData ARROW_UP = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_up.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_up_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_up.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_up_selected.png")
    );

    public static final TextureButtonData ARROW_DOWN = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_down.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_down_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_down.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_down_selected.png")
    );

    public static final TextureButtonData ARROW_LEFT = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_left.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_left_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_left.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_left_selected.png")
    );


    public static final TextureButtonData ARROW_RIGHT = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_right.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_right_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_right.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/arrow_right_selected.png")
    );

    public static final TextureButtonData BOOK_ARROW_LEFT = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_left.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_left_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_left.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_left_selected.png")
    );

    public static final TextureButtonData BOOK_ARROW_RIGHT = new TextureButtonData(
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_right.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_right_selected.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_right.png"),
            Identifier.of(GooeyEditor.id, "textures/gui/book_arrow_right_selected.png")
    );

}

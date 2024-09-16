package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchButtonData;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchTextureData;
import org.shadowmaster435.gooeyeditor.screen.elements.records.TextureButtonData;

public class NinePatchButtonWidget extends GuiButton {

    public NinePatchTextureData off;
    public NinePatchTextureData off_hovered;
    public NinePatchTextureData on;
    public NinePatchTextureData on_hovered;
    public int edge_thickness = 0;
    public int texture_width = 16;
    public int texture_height = 16;



    public NinePatchButtonWidget(int x, int y, int w, int h, NinePatchButtonData data, boolean editMode) {
        super(x, y, w, h, editMode);
        this.off = data.off_texture();
        this.off_hovered = data.off_hovered_texture();
        this.on = data.on_texture();
        this.on_hovered = data.on_hovered_texture();
    }
    public NinePatchButtonWidget(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
        var data = PANEL_BUTTON;
        this.off = data.off_texture();
        this.off_hovered = data.off_hovered_texture();
        this.on = data.on_texture();
        this.on_hovered = data.on_hovered_texture();
    }

    public NinePatchButtonWidget(int x, int y, boolean editMode) {
        this(x, y, 32, 32, editMode);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (isMouseOver(mouseX, mouseY)) {
            if (pressed) {
                drawNinePatchTexture(context, getGlobalRect(), on_hovered.texture(), edge_thickness);
            } else {
                drawNinePatchTexture(context, getGlobalRect(), off_hovered.texture(), edge_thickness);
            }
        } else {
            if (pressed) {
                drawNinePatchTexture(context, getGlobalRect(), on.texture(), edge_thickness);
            } else {
                drawNinePatchTexture(context, getGlobalRect(), off.texture(), edge_thickness);
            }
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var texture_width = new Property("Texture Width", "texture_width", "texture_width", Integer.class);
        var texture_height = new Property("Texture Height", "texture_height", "texture_height", Integer.class);
        var edge_thickness = new Property("Edge Thickness", "edge_thickness", "edge_thickness", Integer.class);
        var off = new Property("Off Texture", "off", "off", Identifier.class);
        var off_hovered = new Property("Off Hovered Texture", "off_hovered", "off_hovered", Identifier.class);
        var on = new Property("On Texture", "on", "on", Identifier.class);
        var on_hovered = new Property("On Hovered Texture", "on_hovered", "on_hovered", Identifier.class);
        return mergeProperties(super.getProperties(), off, off_hovered, on, on_hovered, texture_width, texture_height, edge_thickness);
    }
    // preset
    public static final NinePatchButtonData PANEL_BUTTON = NinePatchButtonData.onOff(NinePatchTexture.SELECTED_THIN_PANEL, NinePatchTexture.THIN_PANEL);
    public static final NinePatchButtonData ENCHANTING_TABLE_BUTTON = NinePatchButtonData.onOff(NinePatchTexture.TAN_BEVELED_PANEL, NinePatchTexture.DISABLED_TAN_BEVELED_PANEL);

}

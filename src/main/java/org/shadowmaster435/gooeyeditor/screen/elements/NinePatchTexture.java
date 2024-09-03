package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchTextureData;

public class NinePatchTexture extends ParentableWidgetBase {

    public int edge_thickness = 0;
    public Identifier texture = Identifier.of("minecraft:textures/block/dirt.png");
    public int texture_width = 16;
    public int texture_height = 16;
    public static final NinePatchTextureData GUI_PANEL = new NinePatchTextureData(16, 16, 5, Identifier.of(GooeyEditor.id, "textures/gui/gui_panel.png"));
    public static final NinePatchTextureData GUI_BOX = new NinePatchTextureData(16, 16, 6, Identifier.of(GooeyEditor.id, "textures/gui/gui_box.png"));
    public static final NinePatchTextureData GUI_SLOT = new NinePatchTextureData(16, 16, 6, Identifier.of(GooeyEditor.id, "textures/gui/slot.png"));


    public NinePatchTexture(int x, int y, int w, int h, NinePatchTextureData data, boolean editMode) {
        super(x, y, w, h, editMode);
        this.edge_thickness = data.edge_thickness();
        this.texture = data.texture();
        this.texture_width = data.texture_width();
        this.texture_height = data.texture_height();
    }
    public NinePatchTexture(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }
    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!(this instanceof ScrollbarWidget)) { // probably bad idea but i cant be bothered
            drawNinePatchTexture(context, getGlobalRect(), texture, edge_thickness, texture_width, texture_height);

        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var texture_width = new Property("Texture Width", "texture_width", "texture_width", Integer.class);
        var texture_height = new Property("Texture Height", "texture_height", "texture_height", Integer.class);
        var edge_thickness = new Property("Edge Thickness", "edge_thickness", "edge_thickness", Integer.class);
        var texture = new Property("Texture", "texture", "texture", Identifier.class);

        return new Property[]{texture_width, texture_height, edge_thickness, texture};
    }
}

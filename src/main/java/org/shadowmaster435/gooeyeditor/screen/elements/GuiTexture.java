package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.editor.util.EditorUtil;
import org.shadowmaster435.gooeyeditor.screen.elements.container.GenericContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchTextureData;

public class GuiTexture extends ParentableWidgetBase implements EditorUtil {

    public Identifier texture = Identifier.of("minecraft:textures/block/dirt.png");
    public int texture_width = 0;
    public int texture_height = 0;


    public GuiTexture(int x, int y, int w, int h, Identifier texture, int texture_width, int texture_height, boolean editMode) {
        super(x, y, w, h, editMode);
        this.texture = texture;
        this.texture_width = texture_width;
        this.texture_height = texture_height;
    }

    public GuiTexture(int x, int y, int w, int h, int texture_width, int texture_height, boolean editMode) {
        super(x, y, w, h, editMode);
        this.texture_width = texture_width;
        this.texture_height = texture_height;
    }

    public GuiTexture(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        drawTexture(context, texture, texture_width, texture_height);
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var texture_width = new Property("Texture Width", "texture_width", "texture_width", Integer.class);
        var texture_height = new Property("Texture Height", "texture_height", "texture_height", Integer.class);
        var texture = new Property("Texture", "texture", "texture", Identifier.class);

        return new Property[]{texture_width, texture_height, texture};
    }

    // preset
    public static final Identifier MAGNIFYING_GLASS_ICON = Identifier.of(GooeyEditor.id, "textures/gui/magnifying_glass_icon.png");

}

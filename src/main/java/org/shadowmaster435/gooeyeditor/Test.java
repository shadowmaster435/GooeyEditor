package org.shadowmaster435.gooeyeditor;

import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.joml.*;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.screen.GuiScreen;

public class Test extends GuiScreen {

    public ScrollbarWidget scrollbar;
    public GuiTexture dirt;

    @Override
    public void initElements() {
        var scrollbar = new ScrollbarWidget(86, 43, false);
        scrollbar.texture_width = 16;
        scrollbar.texture_height = 16;
        scrollbar.edge_thickness = 5;
        scrollbar.texture = Identifier.of("gooeyeditor:textures/gui/slot.png");
        scrollbar.horizontal = false;
        scrollbar.grabber_texture_width = 16;
        scrollbar.grabber_texture_height = 16;
        scrollbar.grabber_edge_thickness = 5;
        scrollbar.grabber_length = 12;
        scrollbar.grabber_texture = Identifier.of("gooeyeditor:textures/gui/scroll_grabber.png");
        scrollbar.name = "scrollbar";
        scrollbar.setSize(new Vector2i(15, 162));
        scrollbar.setScale(new Vector2f(1.0F, 1.0F));
        scrollbar.rotation = 0.0F;
        scrollbar.layer = 3;
        scrollbar.setOrigin(new Vector2i(0, 0));
        scrollbar.center_origin = false;
        addDrawableChild(scrollbar);
        this.scrollbar = scrollbar;

        var dirt = new GuiTexture(141, 69, false);
        dirt.texture_width = 16;
        dirt.texture_height = 16;
        dirt.texture = Identifier.of("minecraft:textures/block/dirt.png");
        dirt.name = "dirt";
        dirt.setSize(new Vector2i(92, 90));
        dirt.setScale(new Vector2f(1.0F, 1.0F));
        dirt.rotation = 157.6938F;
        dirt.layer = 2;
        dirt.setOrigin(new Vector2i(0, 0));
        dirt.center_origin = true;
        addDrawableChild(dirt);
        this.dirt = dirt;

    }

    @Override
    public GuiElement[] getElements() {
        return new GuiElement[]{this.scrollbar, this.dirt};
    }
}
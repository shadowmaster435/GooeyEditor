package org.shadowmaster435.gooeyeditor;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.GuiScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.*;

public class Test3 extends GuiScreen<InventoryExampleHandler> {

    //This default init function is required if you want to load this screen in the editor
    //will throw an exception if excluded
    public Test3(InventoryExampleHandler handler, PlayerInventory inventory, Text text) {
        super(handler, inventory);
        initElements();

    }
    public ScrollbarWidget scrollBar;
    public GuiTexture texture;
    public NinePatchTexture ninePatch;
    public GuiTexture texture1;
    public NinePatchTexture ninePatch2;
    public TextWidget text;
    public GuiTexture texture2;
    public NinePatchTexture ninePatch1;
    public ItemDisplayWidget itemDisplay;
    public TextField textField;

    @Override
    public void clinit() {

    }

    @Override
    public void initElements() {
        var scrollBar = new ScrollbarWidget(198, 57, false);
        scrollBar.texture_width = 16;
        scrollBar.texture_height = 16;
        scrollBar.edge_thickness = 5;
        scrollBar.texture = Identifier.of("gooeyeditor:textures/gui/slot.png");
        scrollBar.horizontal = false;
        scrollBar.grabber_texture_width = 16;
        scrollBar.grabber_texture_height = 16;
        scrollBar.grabber_edge_thickness = 5;
        scrollBar.grabber_length = 12;
        scrollBar.grabber_texture = Identifier.of("gooeyeditor:textures/gui/scroll_grabber.png");
        scrollBar.name = "scrollBar";
        scrollBar.setSize(new Vector2i(13, 144));
        scrollBar.setScale(new Vector2f(1.0F, 1.0F));
        scrollBar.rotation = 0.0F;
        scrollBar.layer = 3;
        scrollBar.setOrigin(new Vector2i(0, 0));
        scrollBar.center_origin = false;
        addDrawableChild(scrollBar);
        this.scrollBar = scrollBar;

        var texture = new GuiTexture(154, 152, false);
        texture.texture_width = 16;
        texture.texture_height = 16;
        texture.texture = Identifier.of("minecraft:textures/item/diamond.png");
        texture.name = "texture";
        texture.setSize(new Vector2i(32, 32));
        texture.setScale(new Vector2f(1.0F, 1.0F));
        texture.rotation = 34.695156F;
        texture.layer = 3;
        texture.setOrigin(new Vector2i(0, 0));
        texture.center_origin = true;
        addDrawableChild(texture);
        this.texture = texture;

        var ninePatch = new NinePatchTexture(56, 42, false);
        ninePatch.texture_width = 16;
        ninePatch.texture_height = 16;
        ninePatch.edge_thickness = 5;
        ninePatch.texture = Identifier.of("gooeyeditor:textures/gui/gui_panel.png");
        ninePatch.name = "ninePatch";
        ninePatch.setSize(new Vector2i(226, 176));
        ninePatch.setScale(new Vector2f(1.0F, 1.0F));
        ninePatch.rotation = 0.0F;
        ninePatch.layer = 1;
        ninePatch.setOrigin(new Vector2i(0, 0));
        ninePatch.center_origin = false;
        addDrawableChild(ninePatch);
        this.ninePatch = ninePatch;

        var texture1 = new GuiTexture(140, 146, false);
        texture1.texture_width = 16;
        texture1.texture_height = 16;
        texture1.texture = Identifier.of("minecraft:textures/item/diamond.png");
        texture1.name = "texture1";
        texture1.setSize(new Vector2i(32, 32));
        texture1.setScale(new Vector2f(1.0F, 1.0F));
        texture1.rotation = 9.462322F;
        texture1.layer = 4;
        texture1.setOrigin(new Vector2i(0, 0));
        texture1.center_origin = true;
        addDrawableChild(texture1);
        this.texture1 = texture1;

        var ninePatch2 = new NinePatchTexture(193, 52, false);
        ninePatch2.texture_width = 16;
        ninePatch2.texture_height = 16;
        ninePatch2.edge_thickness = 6;
        ninePatch2.texture = Identifier.of("gooeyeditor:textures/gui/gui_box.png");
        ninePatch2.name = "ninePatch2";
        ninePatch2.setSize(new Vector2i(76, 154));
        ninePatch2.setScale(new Vector2f(1.0F, 1.0F));
        ninePatch2.rotation = 0.0F;
        ninePatch2.layer = 2;
        ninePatch2.setOrigin(new Vector2i(0, 0));
        ninePatch2.center_origin = false;
        addDrawableChild(ninePatch2);
        this.ninePatch2 = ninePatch2;

        var text = new TextWidget(62, 49, false);
        text.r = 255;
        text.g = 255;
        text.b = 255;
        text.a = 255;
        text.text = "Example";
        text.draw_shadow = true;
        text.name = "text";
        text.setSize(new Vector2i(39, 12));
        text.setScale(new Vector2f(1.0F, 1.0F));
        text.rotation = 0.0F;
        text.layer = 3;
        text.setOrigin(new Vector2i(0, 0));
        text.center_origin = false;
        addDrawableChild(text);
        this.text = text;

        var texture2 = new GuiTexture(61, 111, false);
        texture2.texture_width = 16;
        texture2.texture_height = 16;
        texture2.texture = Identifier.of("minecraft:textures/item/diamond_pickaxe.png");
        texture2.name = "texture2";
        texture2.setSize(new Vector2i(80, 80));
        texture2.setScale(new Vector2f(1.0F, 1.0F));
        texture2.rotation = -75.96376F;
        texture2.layer = 4;
        texture2.setOrigin(new Vector2i(0, 0));
        texture2.center_origin = true;
        addDrawableChild(texture2);
        this.texture2 = texture2;

        var ninePatch1 = new NinePatchTexture(153, 88, false);
        ninePatch1.texture_width = 16;
        ninePatch1.texture_height = 16;
        ninePatch1.edge_thickness = 5;
        ninePatch1.texture = Identifier.of("gooeyeditor:textures/gui/slot.png");
        ninePatch1.name = "ninePatch1";
        ninePatch1.setSize(new Vector2i(34, 34));
        ninePatch1.setScale(new Vector2f(1.0F, 1.0F));
        ninePatch1.rotation = 0.0F;
        ninePatch1.layer = 3;
        ninePatch1.setOrigin(new Vector2i(0, 0));
        ninePatch1.center_origin = false;
        addDrawableChild(ninePatch1);
        this.ninePatch1 = ninePatch1;

        var itemDisplay = new ItemDisplayWidget(154, 89, false);
        itemDisplay.item = Identifier.of("minecraft:dirt");
        itemDisplay.count = 1;
        itemDisplay.name = "itemDisplay";
        itemDisplay.setSize(new Vector2i(32, 32));
        itemDisplay.setScale(new Vector2f(1.0F, 1.0F));
        itemDisplay.rotation = 0.0F;
        itemDisplay.layer = 4;
        itemDisplay.setOrigin(new Vector2i(0, 0));
        itemDisplay.center_origin = false;
        addDrawableChild(itemDisplay);
        this.itemDisplay = itemDisplay;

        var textField = new TextField(62, 60, false);
        textField.setText("");
        textField.setPlaceholder("");
        textField.name = "textField";
        textField.setSize(new Vector2i(129, 14));
        textField.setScale(new Vector2f(1.0F, 1.0F));
        textField.rotation = 0.0F;
        textField.layer = 3;
        textField.setOrigin(new Vector2i(0, 0));
        textField.center_origin = false;
        addDrawableChild(textField);
        this.textField = textField;

    }

    @Override
    public GuiElement[] getElements() {
        return new GuiElement[]{this.scrollBar, this.texture, this.ninePatch, this.texture1, this.ninePatch2, this.text, this.texture2, this.ninePatch1, this.itemDisplay, this.textField};
    }

    @Override
    public boolean isClickOutsideBounds(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }
}
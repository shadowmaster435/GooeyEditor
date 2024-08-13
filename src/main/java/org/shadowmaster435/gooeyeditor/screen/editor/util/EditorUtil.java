package org.shadowmaster435.gooeyeditor.screen.editor.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.elements.*;

public interface EditorUtil {


    default Vector2i getScaledWindowCenter() {

        return new Vector2i(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2);
    }

    default Vector2i getScreenCenteredPos(GuiElement element) {
        var half_size_d = new Vector2d(element.getSize()).mul(0.5).floor();
        var half_size = new Vector2i((int) half_size_d.x, (int) half_size_d.y);
        return getScaledWindowCenter().sub(half_size);
    }

    default ColorPicker createColorPicker() {
        return new ColorPicker(32, 32, 32, 32, true);
    }

    default GuiTexture createTexture() {
        return new GuiTexture(32, 32, 32, 32, Identifier.of("minecraft", "textures/block/dirt.png"), 16, 16, true);
    }

    default NinePatchTexture createNinePatch() {
        return new NinePatchTexture(32, 32, 32, 32, NinePatchTexture.GUI_PANEL, true);
    }

    default ScrollbarWidget createScrollbar() {
        return new ScrollbarWidget(32, 32, 32, 12, ScrollbarWidget.DEFAULT_SCROLLBAR, true);
    }

    default TextField createTextField() {
        return new TextField(32, 32, 32, 12, MinecraftClient.getInstance().textRenderer, true);
    }

    default ItemDisplayWidget createItemDisplay() {
        return new ItemDisplayWidget(32, 32, 32, 32, true);
    }


    default TextWidget createText() {
        return new TextWidget(32, 32, true);
    }

}

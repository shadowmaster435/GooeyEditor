package org.shadowmaster435.gooeyeditor.screen.editor.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.BoxContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ListContainer;

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

    default BoxContainer createBoxContainer() {
        var tex = new BoxContainer(32,32, 32, 32,true);
        tex.name = "boxContainer";
        return tex;
    }

    default SpinboxWidget createSpinbox() {
        var tex = new SpinboxWidget(32,32,true);
        tex.name = "spinbox";
        return tex;
    }

    default GuiRadialTexture createRadialTexture() {
        var tex = new GuiRadialTexture(Identifier.of(GooeyEditor.id, "textures/gui/progress_circle.png"), 32, 32, 32, 32, true);
        tex.name = "rangeTexture";
        return tex;
    }

    default GuiRangeTexture createRangeTexture() {
        var tex = new GuiRangeTexture(Identifier.of(GooeyEditor.id, "textures/gui/progress_arrow.png"), 32, 32, 44, 32, 22, 16, true);
        tex.name = "rangeTexture";
        return tex;
    }

    default GuiTexture createTexture() {
        var tex = new GuiTexture(32, 32, 32, 32, Identifier.of("minecraft", "textures/block/dirt.png"), 16, 16, true);
        tex.name = "texture";
        return tex;
    }

    default ListContainer createListContainer() {
        var list_container = new ListContainer(32, 32, 64, 64, 4, true);
        list_container.name = "listContainer";
        return list_container;
    }
    default SlotGridWidget createSlotGrid() {
        var slot_display = new SlotGridWidget(32, 32, true);
        slot_display.name = "slotGrid";
        return slot_display;
    }

    default PlayerInventoryWidget createPlayerInventory() {
        var slot_display = new PlayerInventoryWidget(32, 32, true);
        slot_display.name = "playerInventory";
        return slot_display;
    }

    default SlotWidget createSlotDisplay() {
        var slot_display = new SlotWidget(32, 32, 16, 16, true);
        slot_display.name = "slotDisplay";
        return slot_display;
    }

    default NinePatchTexture createNinePatch() {
        var nine_patch = new NinePatchTexture(32, 32, 32, 32, NinePatchTexture.PANEL, true);
        nine_patch.name = "ninePatch";
        return nine_patch;
    }

    default ScrollbarWidget createScrollbar() {
        var scrollbar = new ScrollbarWidget(32, 32, 32, 12, ScrollbarWidget.DEFAULT_SCROLLBAR, true);
        scrollbar.name = "scrollBar";
        return scrollbar;
    }

    default TextField createTextField() {
        var field = new TextField(32, 32, 32, 12, MinecraftClient.getInstance().textRenderer, true);
        field.name = "textField";
        return field;
    }

    default ItemDisplayWidget createItemDisplay() {
        var display = new ItemDisplayWidget(32, 32, 32, 32, true);
        display.name = "itemDisplay";
        return display;
    }


    default TextWidget createText() {
        var text = new TextWidget(32, 32, true);
        text.name = "text";
        return text;
    }

}

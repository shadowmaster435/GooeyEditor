package org.shadowmaster435.gooeyeditor.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.SlotWidget;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class HandledGuiScreen<S extends GuiScreenHandler> extends HandledScreen<S> {
    private final HashMap<String, GuiElement> elements = new HashMap<>();
    private final HashMap<SlotWidget, Slot> slotMap = new HashMap<>();

    protected HandledGuiScreen(S handler, PlayerInventory inventory) {
        super(handler, inventory, Text.of(""));

        initElements();
    }



    public abstract void initElements();

    public abstract GuiElement[] getElements();


//    @Nullable
//    @Override
//    private Slot getSlotAt(double x, double y) {
//        for (SlotWidget widget : slotMap.keySet()) {
//            if (widget.isMouseOver(x, y)) {
//                return slotMap.get(widget);
//            }
//        }
//        return null;
//    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
    }

    public final void refreshElements() {
        elements.clear();
        for (Element element : children()) {
            if (element instanceof GuiElement guiElement) {
                elements.put(guiElement.name, guiElement);
            }
        }
    }

    public final ArrayList<GuiElement> getEditableElements() {
        refreshElements();
        ArrayList<GuiElement> result = new ArrayList<>();
        for (String key : elements.keySet()) {
            var guiElement = elements.get(key);
            guiElement.setEditMode(true);
            result.add(guiElement);
        }
        return result;
    }
}

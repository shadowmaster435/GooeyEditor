package org.shadowmaster435.gooeyeditor.screen.editor.util;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.shadowmaster435.gooeyeditor.screen.HandledGuiScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.SlotWidget;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class MixinMethods {

    public static void drawDragItem(CallbackInfoReturnable<Slot> cir, double x, double y, HandledScreen<?> screen) {
        if (screen instanceof HandledGuiScreen<?> guiScreen) {
            var hovered = guiScreen.getHoveredSlot(x, y);
            if (hovered instanceof SlotWidget slotWidget) {
                cir.setReturnValue(slotWidget.displayedSlot);
            }
        }
    }
    public static void checkSlot(CallbackInfoReturnable<Slot> cir, double x, double y, HandledScreen<?> screen) {
        if (screen instanceof HandledGuiScreen<?> guiScreen) {
            var hovered = guiScreen.getHoveredSlot(x, y);
            if (hovered instanceof SlotWidget slotWidget) {
                cir.setReturnValue(slotWidget.displayedSlot);
            }
        }
    }
}

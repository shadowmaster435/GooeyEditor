package org.shadowmaster435.gooeyeditor.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.shadowmaster435.gooeyeditor.screen.GuiScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.SlotWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Inject(
            method = "getSlotAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;",
                    shift = At.Shift.AFTER
            ), cancellable = true)
    public void getSlotAt(double x, double y, CallbackInfoReturnable<Slot> cir) {
        if (((HandledScreen<?>)(Object)this) instanceof GuiScreen<?> guiScreen) {
            var hovered = guiScreen.getHoveredSlot(x, y);
            if (hovered instanceof SlotWidget slotWidget) {
                cir.setReturnValue(slotWidget.displayedSlot);
            }
        }
    }
}

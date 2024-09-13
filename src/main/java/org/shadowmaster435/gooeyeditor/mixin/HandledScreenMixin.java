package org.shadowmaster435.gooeyeditor.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.shadowmaster435.gooeyeditor.screen.editor.util.MixinMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        MixinMethods.checkSlot(cir, x, y, ((HandledScreen<?>)(Object)this));
    }
}

package org.shadowmaster435.gooeyeditor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.shadowmaster435.gooeyeditor.screen.editor.util.MixinMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
            ), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void getSlotAt(double x, double y, CallbackInfoReturnable<Slot> cir) {
        MixinMethods.checkSlot(cir, x, y, ((HandledScreen<?>)(Object)this));
    }
//
//    @Inject(
//            method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawItem(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
//                    shift = At.Shift.AFTER
//            ), cancellable = true)
//    public void setSlotWidgets(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, @Local ItemStack itemStack) {
//        MixinMethods.drawDragItem(c);
//    }
}

package org.shadowmaster435.gooeyeditor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;
import org.shadowmaster435.gooeyeditor.util.InputHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
//    @Inject(
//            method = "render",
//            at = @At(
//                    value = "INVOKE_ASSIGN",
//                    target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;beginRenderTick(JZ)I",
//                    shift = At.Shift.AFTER
//            ),
//            locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    public void render(boolean tick, CallbackInfo ci, Runnable runnable, @Local int i) {
//        if (GooeyEditorClient.editorScreen != null) {
//
//            for (int j = 0; j < Math.min(10, i); ++j) {
//                GooeyEditorClient.editorScreen.updateProperties();
//            }
//        }
//    }



}

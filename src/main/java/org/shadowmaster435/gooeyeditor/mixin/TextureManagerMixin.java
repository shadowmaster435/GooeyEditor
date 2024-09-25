package org.shadowmaster435.gooeyeditor.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.FormattedException;
import net.fabricmc.loader.impl.launch.knot.Knot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.FileNotFoundException;
import java.io.IOException;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

    @Inject(
            method = "loadTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                    shift = At.Shift.BEFORE
            ), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void skipMissingTextureLoggerIfEditor(Identifier id, AbstractTexture texture, CallbackInfoReturnable<AbstractTexture> cir, IOException iOException) {
        if (iOException instanceof FileNotFoundException && MinecraftClient.getInstance().currentScreen instanceof GuiEditorScreen) {
            cir.setReturnValue(MissingSprite.getMissingSpriteTexture());
        }
    }
}

package org.shadowmaster435.gooeyeditor.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {



    @Inject(
            method = "init",
            at = @At(
                    value = "TAIL"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void init(CallbackInfo ci) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ButtonWidget button1;
            button1 = ButtonWidget.builder(Text.literal("Gui Editor"), button -> MinecraftClient.getInstance().setScreen(new GuiEditorScreen(((TitleScreen)(Object) this))))
                    .dimensions(0, 0, MinecraftClient.getInstance().textRenderer.getWidth("Gui Editor") + 3, 16)
                    .build();
            ((TitleScreen)(Object) this).addDrawableChild(button1);
        }

    }

}

package org.shadowmaster435.gooeyeditor.mixin;

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

        ButtonWidget button1;
                button1 = ButtonWidget.builder(Text.literal("Tst"), button -> MinecraftClient.getInstance().setScreen(new GuiEditorScreen(((TitleScreen)(Object) this))))
                .dimensions(0, 0, 16, 16)
                .build();
        ((TitleScreen)(Object) this).addDrawableChild(button1);
    }

}

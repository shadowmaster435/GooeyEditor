package org.shadowmaster435.gooeyeditor.mixin;

import net.minecraft.client.Mouse;
import org.shadowmaster435.gooeyeditor.util.InputHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(
            method = "onMouseButton",
            at = @At(
                    value = "TAIL"
            )
    )
    public void onMouse(long window, int button, int action, int mods, CallbackInfo ci) {
        InputHelper.mouseCallback(window, button, action, mods);
    }

}

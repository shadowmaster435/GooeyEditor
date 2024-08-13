
package org.shadowmaster435.gooeyeditor.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ResourceFactory;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;
import org.shadowmaster435.gooeyeditor.util.InputHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
//
//
//    @Inject(
//            method = "loadPrograms",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
//                    ordinal = 0,
//                    shift = At.Shift.AFTER
//            ),
//            locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    public void registerShaders(ResourceFactory factory, CallbackInfo ci, List list, List list2) throws IOException {
//        list2.add(Pair.of(new ShaderProgram(factory, "hue_gradient", VertexFormats.POSITION_TEXTURE_COLOR), (Consumer) program -> GooeyEditorClient.registerHueGradient((ShaderProgram) program)));
//
//    }


    @Inject(
            method = "render",
            at = @At(
                    value = "TAIL"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, boolean bl, int i, int j, Window window, Matrix4f matrix4f, Matrix4fStack matrix4fStack, DrawContext drawContext) {
        InputHelper.updateKeys();
    }

}

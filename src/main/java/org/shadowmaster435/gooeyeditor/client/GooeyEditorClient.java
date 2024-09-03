package org.shadowmaster435.gooeyeditor.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.Test2;
import org.shadowmaster435.gooeyeditor.TestHandler;

import java.io.IOException;

public class GooeyEditorClient implements ClientModInitializer {

    private static ShaderProgram hue_gradient;
    private static ShaderProgram radial_texture;

    @Override
    public void onInitializeClient() {
        register_shaders();
        GooeyEditor.registerScreenForEditor("Test2", Test2.class);
        HandledScreens.register(GooeyEditor.TESTHANDLERTYPE, Test2::new);
    }


    public static ShaderProgram getHueGradient() {
        return hue_gradient;
    }

    public static ShaderProgram getRadialTexture() {
        return radial_texture;
    }



    public static void registerHueGradient() {

    }

    public static void register_shaders() {

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.ofVanilla("shaders");
            }

            @Override
            public void reload(ResourceManager manager) {
                try {
                    hue_gradient = new ShaderProgram(manager, "hue_gradient", VertexFormats.POSITION_TEXTURE_COLOR);
                    radial_texture = new ShaderProgram(manager, "radial_texture", VertexFormats.POSITION_TEXTURE_COLOR);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

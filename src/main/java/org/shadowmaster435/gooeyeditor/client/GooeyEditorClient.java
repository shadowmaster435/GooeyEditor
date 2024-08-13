package org.shadowmaster435.gooeyeditor.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class GooeyEditorClient implements ClientModInitializer {

    private static ShaderProgram hue_gradient;


    @Override
    public void onInitializeClient() {
        register_shaders();
    }

    public static ShaderProgram getHueGradient() {
        return hue_gradient;
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

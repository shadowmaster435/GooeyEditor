package org.shadowmaster435.gooeyeditor.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.InventoryExample;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;

import java.io.IOException;

public class GooeyEditorClient implements ClientModInitializer {

    public static GuiEditorScreen currentEditor = null;

    private static ShaderProgram hue_gradient;
    private static ShaderProgram radial_texture;
    private static ShaderProgram nine_patch;

    private static ShaderProgram draw_buffer;


    @Override
    public void onInitializeClient() {
        register_shaders();
        GooeyEditor.registerScreenForEditor("InventoryExample", InventoryExample.class);
        HandledScreens.register(GooeyEditor.TESTHANDLERTYPE, InventoryExample::new);
    }


    public static ShaderProgram getHueGradient() {
        return hue_gradient;
    }

    public static ShaderProgram getRadialTexture() {
        return radial_texture;
    }

    public static ShaderProgram getDrawBuffer() {
        return draw_buffer;
    }

    public static ShaderProgram getNinePatch() {
        return nine_patch;
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
                    nine_patch = new ShaderProgram(manager, "nine_patch", VertexFormats.POSITION_TEXTURE_COLOR);

                  //  draw_buffer = new ShaderProgram(manager, "draw_buffer", VertexFormats.POSITION_TEXTURE_COLOR);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

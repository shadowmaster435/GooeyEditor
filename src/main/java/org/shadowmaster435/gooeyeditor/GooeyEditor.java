package org.shadowmaster435.gooeyeditor;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.screen.HandledGuiScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class GooeyEditor implements ModInitializer {
    public static final String id = "gooeyeditor";
    public static final Logger logger = Logger.getLogger("Gooey Editor");
    private static final HashMap<String, Class<? extends HandledGuiScreen>> loadableScreens = new HashMap<>();
    private static HashMap<String, Class<?>> classMap = new HashMap<>();
    private static HashMap<Identifier, Supplier<? extends GuiElement>> registeredElements = new HashMap<>();
    private static HashMap<Identifier, String> elementDisplayNames = new HashMap<>();

    /**
     * Registers an element for editor use.
     * @return Provided id
     */
    public static Identifier registerElement(Identifier id, String displayName, Supplier<? extends GuiElement> creationFunction) {
        registeredElements.put(id, creationFunction);
        elementDisplayNames.put(id, displayName);
        return id;
    }

    public static String getElementDisplayName(Identifier identifier) {
        return elementDisplayNames.get(identifier);
    }

    public static HashMap<Identifier, Supplier<? extends GuiElement>> getRegisteredElements() {
        return new HashMap<>(registeredElements);
    }

    public static void warn(int warning, Object... extra_data) {
        switch (warning) {
            case 0 -> GooeyEditor.logger.warning("Tried to export a " + extra_data[0].getClass().getSimpleName() + " with a blank name. Skipped export of element.");
            case 1 -> GooeyEditor.logger.warning("Found element of type " + extra_data[0].getClass().getSimpleName() + " with a duplicate name of '" + extra_data[1] + "' exporting with name '" + extra_data[2] + "' instead.");
        }
    }

    public static Class<? extends HandledGuiScreen> getClassForDisplayName(String display_name) {
        return loadableScreens.getOrDefault(display_name, null);
    }

    public static void registerScreenForEditor(String display_name, Class<? extends HandledGuiScreen> screen) {
        GooeyEditor.loadableScreens.put(display_name, screen);
    }

    public static Class<?> getClassFromString(String s) {
        System.out.println("You still need to do element classMap registering boi");
        return classMap.getOrDefault(s, null);
    }

    @Override
    public void onInitialize() {

    }
}

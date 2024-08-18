package org.shadowmaster435.gooeyeditor;

import net.fabricmc.api.ModInitializer;
import org.shadowmaster435.gooeyeditor.screen.GuiScreen;

import java.util.HashMap;
import java.util.logging.Logger;

public class GooeyEditor implements ModInitializer {
    public static final String id = "gooeyeditor";
    public static final Logger logger = Logger.getLogger("Gooey Editor");
    private static final HashMap<String, Class<? extends GuiScreen>> loadableScreens = new HashMap<>();
    @Override
    public void onInitialize() {

    }
    public static void warn(int warning, Object... extra_data) {
        switch (warning) {
            case 0 -> GooeyEditor.logger.warning("Tried to export a " + extra_data[0].getClass().getSimpleName() + " with a blank name. Skipped export of element.");
            case 1 -> GooeyEditor.logger.warning("Found element of type " + extra_data[0].getClass().getSimpleName() + " with a duplicate name of '" + extra_data[1] + "' exporting with name '" + extra_data[2] + "' instead.");
        }
    }

    public static Class<? extends GuiScreen> getClassForDisplayName(String display_name) {
        return loadableScreens.getOrDefault(display_name, null);
    }

    public static void registerScreenForEditor(String display_name, Class<? extends GuiScreen> screen) {
        GooeyEditor.loadableScreens.put(display_name, screen);
    }
}

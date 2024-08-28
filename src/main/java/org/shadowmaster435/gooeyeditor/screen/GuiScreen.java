package org.shadowmaster435.gooeyeditor.screen;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GuiScreen extends Screen {

    private final HashMap<String, GuiElement> elements = new HashMap<>();

    protected GuiScreen() {
        super(Text.of(""));
        initElements();
    }


    public abstract void initElements();

    public abstract GuiElement[] getElements();



    public final void refreshElements() {
        elements.clear();
        for (Element element : children()) {
            if (element instanceof GuiElement guiElement) {
                elements.put(guiElement.name, guiElement);
            }
        }
    }

    public final ArrayList<GuiElement> getEditableElements() {
        refreshElements();
        ArrayList<GuiElement> result = new ArrayList<>();
        for (String key : elements.keySet()) {
            var guiElement = elements.get(key);
            guiElement.setEditMode(true);
            result.add(guiElement);
        }
        return result;
    }

}

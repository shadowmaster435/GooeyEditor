package org.shadowmaster435.gooeyeditor.screen.elements.action.editor;

import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;

import java.util.HashMap;

public class EditorPropertyAction<E extends SealedGuiElement> implements EditorAction {

    private final E element;
    private final HashMap<?, SealedGuiElement.Property> properties;

    public EditorPropertyAction(E element, HashMap<?, SealedGuiElement.Property> properties) {
        this.element = element;
        this.properties = properties;
    }


    @Override
    public void act(boolean redo) {
        for (Object e : properties.keySet()) {
            var prop = properties.get(e);
            System.out.println(e);
            prop.set(element, e);
        }
    }
}

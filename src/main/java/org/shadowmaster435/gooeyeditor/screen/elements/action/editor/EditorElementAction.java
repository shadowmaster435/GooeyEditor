package org.shadowmaster435.gooeyeditor.screen.elements.action.editor;

import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;

import java.util.HashMap;

public class EditorElementAction<E extends SealedGuiElement> implements EditorAction {
    
    private final E element;

    public EditorElementAction(E element) {
        this.element = element;
    }
    
    @Override
    public void act(boolean redo) {
        var editor = GooeyEditorClient.currentEditor;
        if (editor != null) {
            if (redo) {
                if (element.parent != null) {
                    ((GuiElement) element.parent).addElement(element);
                } else {
                    editor.toAdd.add(element);
                }
            } else {
                if (element.parent != null) {
                    ((GuiElement) element.parent).removeElement(element);
                } else {
                    editor.toRemove.add(element);
                }
            }
        }
    }
}

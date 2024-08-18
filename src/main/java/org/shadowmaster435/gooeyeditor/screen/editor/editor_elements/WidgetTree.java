package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.text.Text;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.CollapsableContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.DropdownContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ScrollableListContainer;
import org.shadowmaster435.gooeyeditor.util.SimpleTreeMap;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class WidgetTree extends ScrollableListContainer {

    public GuiEditorScreen screen;
    private SimpleTreeMap<String, Pair<GuiElement, Integer>> tree = new SimpleTreeMap<>();

    public WidgetTree(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, new ScrollbarWidget(x + (w - 12), y, 12, h, false), 2, editMode);
    }

    public void createTreeForElement(ParentableWidgetBase rootElement) {
        tree.clear();
        HashMap<GuiElement, GuiElement> parentMap = new HashMap<>();
        for (GuiElement element : rootElement) {
            if (element instanceof ParentableWidgetBase parentableWidgetBase) {
                parentableWidgetBase.forEachInBranch((element1, parent, depth) -> {
                    tree.put(new Pair<>(element1, depth), rootElement.name, getElementPath(element1).toArray(new String[]{}));
                    parentMap.put(element1, Objects.requireNonNullElse(element1.parent, this));
                }, 1);
            } else {
                tree.put(new Pair<>(element, 1), rootElement.name, element.name);
                parentMap.put(element, this);

            }
        }
        HashMap<GuiElement, DropdownContainer> containerMap = new HashMap<>();
        parentMap.forEach((element, parent) -> {
            var button = new TextButtonWidget(0, 0, element.name, false);
            DropdownContainer container = new DropdownContainer(0,0, 0,0, false);
            if (containerMap.getOrDefault(parent, null) != null) {
                container = containerMap.get(parent);
            }
            if (parent != this) {
                containerMap.putIfAbsent(parent, container);
                containerMap.get(parent).addElement(button);
                container.addElement(element);
                button.setDataPressFunction(this::tryExpandElement, element, container);

            } else {
                button.setDataPressFunction(this::tryExpandElement, element, this);

            }
        });

    }

    public void tryExpandElement(GuiButton textButtonWidget, Object[] data) {
        if (data[0] instanceof GuiElement element) {
            screen.selectElement(element);
        }
        if (data[1] instanceof DropdownContainer container) {
            if (container.isOpen) {
                container.close();
            } else {
                container.open();
            }
        }
    }

    private ArrayList<String> getElementPath(GuiElement element) {
        ArrayList<String> path = new ArrayList<>();
        element.forEachParent((parent) -> path.add(parent.name));
        return path;
    }


    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}

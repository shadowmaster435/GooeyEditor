package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiButton;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.NinePatchTexture;

import java.util.ArrayList;
import java.util.HashMap;

public class TabContainer extends BaseContainer {

    private HashMap<Tab, ToggleContainer> tabMap = new HashMap<>();
    private HashMap<Tab, ToggleContainer> toAdd = new HashMap<>();
    private ArrayList<Tab> toRemove = new ArrayList<>();
    private ListContainer listContainer;

    public int tabCount = 0;

    public TabContainer(int x, int y, int w, int h, boolean editMode) {
        super(0, 0, w, h, editMode);
        this.listContainer = new ListContainer(x, y, w, 32, 0, editMode);
        listContainer.selectable = false;
        listContainer.needsExport = false;
        listContainer.name = "tabs";
    }

    public TabContainer(int x, int y, boolean editMode) {
        super(0, 0, 32, 32, editMode);
        this.listContainer = new ListContainer(x, y, 32, 32, 0, editMode);
        listContainer.selectable = false;
        listContainer.needsExport = false;
        listContainer.name = "tabs";
    }

    private void initList() {
        this.listContainer = new ListContainer(0, 0, 32, 32, 0, this.isEditMode());
        listContainer.selectable = false;
        listContainer.needsExport = false;
        listContainer.name = "tabs";

    }

    @Override
    public void arrange() {

    }

    @Override
    public void addElement(GuiElement... element) {
        for (GuiElement elem : element) {
            if (elem instanceof Tab) {
                listContainer.addElement(element);
            } else {
                addElement(elem);
            }
        }
    }

    @Override
    public void addElement(GuiElement element) {
        if (element instanceof Tab) {
            listContainer.addElement(element);
        } else {
            super.addElement(element);
        }
    }

    @Override
    public void addElements(GuiElement... element) {
        for (GuiElement elem : element) {
            if (elem instanceof Tab) {
                listContainer.addElement(element);
            } else {
                addElement(elem);
            }
        }
    }

    public void changeTab(Tab tab) {
        var container = tabMap.getOrDefault(tab, null);
        tabMap.forEach((k, v) -> {
            if (v == container) {
                v.open();
            } else {
                k.pressed = false;
                v.close();
            }
        });
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (listContainer == null) {
            initList();
        }
        tabCount = Math.max(tabCount, 0);
        toAdd.forEach((k, v) -> listContainer.addElement(v));
        toRemove.forEach(k -> tabMap.remove(k));
        listContainer.setSize(getWidth(), 32);
        listContainer.setY(2);
        if (tabCount != tabMap.size()) {
            if (tabCount < tabMap.size()) {
                for (Tab tab : tabMap.keySet()) {
                    var container = tabMap.get(tab);
                    container.orphanize();
                    toRemove.add(tab);
                }
            }
            if (tabCount > tabMap.size()) {
                for (int i = 0; i < tabCount; ++i) {
                    if (i > tabMap.size()) {
                        var newTab = new Tab(0, 0, 0, 32, this, isEditMode());
                        var newContainer = new ToggleContainer(0, 0, 0, 32, isEditMode());
                        toAdd.put(newTab, newContainer);
                    }
                }
            }
        }
        var rect = getGlobalRect();
        rect.setRect(getGlobalX(), getGlobalY() - 32, getWidth(), getHeight() - 32);
        drawNinePatchTexture(context, rect, NinePatchTexture.PANEL);
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var tabCount = new Property("Tab Count", "", "", Integer.class);
        return mergeProperties(super.getProperties(), tabCount);
    }

    public static class Tab extends GuiButton {

        public boolean leftConnected = true;
        public boolean rightConnected = true;

        public Tab(int x, int y, int w, int h, TabContainer container, boolean editMode) {
            super(x, y, w, h, editMode);
            setPressFunction((a) -> container.changeTab((Tab) a));
        }

        private final Identifier tabConnectors =  Identifier.of(GooeyEditor.id, "textures/gui/tab_connectors.png");
        @Override
        public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
            drawNinePatchTexture(context, (selected) ? NinePatchTexture.TAB_SELECTED : NinePatchTexture.TAB);
            if (pressed) {
                if (leftConnected) {
                    context.drawTexture(tabConnectors, getGlobalX(), getGlobalY() - (getHeight() - 2), 3, 2, 0, 0, 3, 2, 6, 2);
                }
                if (rightConnected) {
                    context.drawTexture(tabConnectors, getGlobalX() + (getWidth() - 3), getGlobalY() - (getHeight() - 2), 3, 2, 3, 0, 3, 2, 6, 2);
                }
            }
            super.preTransform(context, mouseX, mouseY, delta);
        }

        @Override
        public Property[] getProperties() {
            var leftConnected = new Property("Left Connected", "leftConnected", "leftConnected", Boolean.class);
            var rightConnected = new Property("Right Connected", "rightConnected", "rightConnected", Boolean.class);

            return mergeProperties(super.getProperties(), leftConnected, rightConnected);
        }
    }
}

package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiButton;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.NinePatchTexture;
import org.shadowmaster435.gooeyeditor.screen.elements.ParentableWidgetBase;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;

import java.util.ArrayList;
import java.util.HashMap;

public class TabContainer extends BaseContainer {

    private HashMap<Tab, ToggleContainer> tabMap = new HashMap<>();
    private HashMap<Tab, ToggleContainer> toAdd = new HashMap<>();
    private ArrayList<Tab> toRemove = new ArrayList<>();
    private ListContainer listContainer;
    private Tab selectedTab = null;
    private boolean initialized = false;

    public int tabCount = 0;

    public TabContainer(int x, int y, int w, int h, boolean editMode) {
        super(0, 0, w, h, editMode);


    }

    public TabContainer(int x, int y, boolean editMode) {
        super(0, 0, 32, 32, editMode);

    }

    private void initList() {
        this.listContainer = new ListContainer(0, 0, 32, 32, 0, this.isEditMode());
        listContainer.selectable = false;
        listContainer.needsExport = false;
        listContainer.name = "tabs";
        listContainer.vertical = false;
        listContainer.repositionElements = true;
        addElement(listContainer);

    }

    @Override
    public void arrange() {

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

                listContainer.addElements(element);
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

    public Tab addTab(String tabName) {
        var newTab = new Tab(0, 0, 16, 16, this, isEditMode());
        var newContainer = new ToggleContainer(0, 0, 0, 16, isEditMode());
        newTab.name = tabName;
        toAdd.put(newTab, newContainer);
        return newTab;
    }

    public Tab addAsTab(String tabName, GuiElement... elements) {
        var newTab = new Tab(0, 0, 16, 16, this, isEditMode());
        var newContainer = new ToggleContainer(0, 0, 0, 16, isEditMode());
        newTab.name = tabName;
        newTab.addElements(elements);
        toAdd.put(newTab, newContainer);
        tabCount += 1;
        return newTab;
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!initialized) {
            if (imported) {
                listContainer = (ListContainer) findChildInBranch("tabs");
            } else {
                initList();
            }
            initialized = true;
        }


        tabCount = Math.max(tabCount, 0);
        toAdd.forEach((k, v) -> {
            listContainer.addElement(k);

            addElement(v);
            tabMap.put(k, v);
        });
        toRemove.forEach(k -> tabMap.remove(k));
        toRemove.clear();
        toAdd.clear();
        listContainer.setSize(getWidth(), 16);
        listContainer.setY(19);
        listContainer.setX(4);
        if (tabCount != tabMap.size()) {
            if (tabCount < tabMap.size()) {
                for (int i = 0; i < tabMap.size(); ++i) {
                    var entry = new ArrayList<>(tabMap.entrySet()).get((tabMap.size() - 1) - i);
                    var tab = entry.getKey();
                    if (tabMap.size() - toRemove.size() > tabCount) {
                        var container = tabMap.get(tab);
                        container.orphanize();
                        tab.orphanize();
                        toRemove.add(tab);
                    }

                }
            } else {

                if (tabCount != Integer.MAX_VALUE) {
                    for (int i = 0; i < tabCount; ++i) {

                        if (i + 1 > tabMap.size()) {
                            var newTab = new Tab(16 * i, 0, 16, 16, this, isEditMode());
                            var newContainer = new ToggleContainer(0, 0, 0, 16, isEditMode());
                            newTab.name = "tab" + toAdd.size();
                            toAdd.put(newTab, newContainer);
                        }
                    }
                }
            }
        }
        var rect = getGlobalRect();
        rect.setRect(getGlobalX(), getGlobalY() + 16, getWidth(), getHeight() - 16);

        drawNinePatchTexture(context, rect, NinePatchTexture.PANEL);
        super.preTransform(context, mouseX, mouseY, delta);

    }

    @Override
    public Property[] getProperties() {
        var tabCount = new Property("Tab Count", "tabCount", "tabCount", Integer.class);
        return mergeProperties(super.getProperties(), tabCount);
    }

    public static class Tab extends GuiButton {

        public boolean leftConnected = true;
        public boolean rightConnected = true;
        private final TabContainer container;

        public Tab(int x, int y, int w, int h, TabContainer container, boolean editMode) {
            super(x, y, w, h, editMode);
            setPressFunction((a) -> container.changeTab((Tab) a));
            this.container = container;
        }

        private final Identifier tabConnectors =  Identifier.of(GooeyEditor.id, "textures/gui/tab_connecters.png");
        @Override
        public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
            var rect = new Rect2(getGlobalRect());
            var connected = container.tabMap.get(this);
            var editBool = container.selectedTab == this;
            rect.height = (editBool) ? getHeight() : getHeight() - 2;
            drawNinePatchTexture(context, rect, (editBool) ? NinePatchTexture.TAB_SELECTED : NinePatchTexture.TAB);
            if (!connected.isOpen && editBool) {
                connected.open();
            }
            if (connected.isOpen && !editBool) {
                connected.close();
            }
            if (editBool) {
                if (leftConnected) {
                    context.drawTexture(tabConnectors, getGlobalX(), getGlobalY() + (getHeight() - 2), 3, 2, 0, 0, 3, 2, 6, 2);
                }
                if (rightConnected) {
                    context.drawTexture(tabConnectors, getGlobalX() + (getWidth() - 3), getGlobalY() + (getHeight() - 2), 3, 2, 3, 0, 3, 2, 6, 2);
                }
            }
            super.preTransform(context, mouseX, mouseY, delta);
        }

        @Override
        public void addElement(GuiElement element) {
            container.tabMap.get(this).addElement(element);
            if (GooeyEditorClient.currentEditor != null) {
                GooeyEditorClient.currentEditor.tree.regenTree();;
            }
        }

        public TabContainer getContainer() {
            return container;
        }

        @Override
        public void removeElement(int index) {
            container.tabMap.get(this).removeElement(index);
            if (GooeyEditorClient.currentEditor != null) {
                GooeyEditorClient.currentEditor.tree.regenTree();;
            }
        }

        @Override
        public void removeElement(GuiElement element) {
            container.tabMap.get(this).removeElement(element);
            if (GooeyEditorClient.currentEditor != null) {
                GooeyEditorClient.currentEditor.tree.regenTree();;
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            var editBool = container.selectedTab != this || GooeyEditorClient.currentEditor.getSelectedElement() == this;
            if ((isMouseOver(mouseX, mouseY) && isActive() && !pressed) || (editBool && isMouseOver(mouseX,mouseY))) {

                pressed = true;
                container.selectedTab = this;
                if (dataPressFunction != null) {
                    dataPressFunction.accept(this, pressFunctionData);
                } else {
                    pressFunction.accept(this);

                }
            }
            return false;
        }


        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return false;
        }

        @Override
        public Property[] getProperties() {
            var leftConnected = new Property("Left Connected", "leftConnected", "leftConnected", Boolean.class);
            var rightConnected = new Property("Right Connected", "rightConnected", "rightConnected", Boolean.class);

            return mergeProperties(super.getProperties(), leftConnected, rightConnected);
        }
    }
}

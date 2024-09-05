package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.BaseContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.DropdownContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.GenericContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ScrollableListContainer;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;
import org.shadowmaster435.gooeyeditor.util.InputHelper;
import org.shadowmaster435.gooeyeditor.util.SimpleTreeMap;
import oshi.util.tuples.Pair;

import javax.xml.stream.events.DTD;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WidgetTree extends ScrollableListContainer {

    public GuiEditorScreen screen;
    private final SimpleTreeMap<String, Pair<GuiElement, Integer>> tree = new SimpleTreeMap<>();
    public Rect2 editorRect = new Rect2();
    private ParentableWidgetBase current = null;
    public DraggableElementReferenceButton hoveredButton = null;
    private DraggableElementReferenceButton clickedButton = null;


    public WidgetTree(int x, int y, int w, int h, boolean editMode) {
        super(0, y, w, h, new ScrollbarWidget((w - 12), 0, 12, h, false), 2, editMode);
        editorRect = new Rect2(x - 3, y, w, h);
        addElement(this.getScrollbar());
        this.getScrollbar().layer = 515;
        updateChildren = true;
        renderChildren = true;
    }

    public void regenTree() {
        if (current != null) {
            createTreeForElement(current);
        }
    }

    public void createTreeForElement(ParentableWidgetBase rootElement) {
        if (!screen.isPropertyEditorOpen()) {
            return;
        }
        current = rootElement;
        tree.clear();
        clearChildren();
        addElement(this.getScrollbar());
        var genericContainer = new GenericContainer(0,0,0,0,false);
        //HashMap<GuiElement, ArrayList<GuiElement>> parentMap = new HashMap<>();
        AtomicInteger y = new AtomicInteger(0);
        rootElement.forEachInBranch(((element, p, d) -> {
            if (element.parent != null && element != rootElement && element.parent != element && element.parent != rootElement.parent && element instanceof ParentableWidgetBase e) {
                var button = new DraggableElementReferenceButton(0,0, element.name, e, this, screen, false);
                var currY = y.getAndAdd(1);
                button.setY((currY * 10));
                button.setX((d * 8) - getGlobalX());
                genericContainer.addElement(button);
                button.layer = 516;
            }
        }), 0);
        addElement(genericContainer);
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
        element.forEachParent((parent) -> {
            path.add(parent.name);
        });
        return path;
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        scissor(true);
        getScrollbar().setActive(screen.isPropertyEditorOpen());
        getScrollbar().setVisible(screen.isPropertyEditorOpen());
        if (screen.isPropertyEditorOpen()) {
            context.fill(getGlobalX() - 12, getGlobalY(), getGlobalX() + getWidth(), getGlobalY() + getHeight(), 513, ColorHelper.Argb.getArgb(100, 100, 100));
        }
        super.preTransform(context, mouseX, mouseY, delta);

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (GuiElement element1 : getElements()) {
            if (element1 instanceof GenericContainer g) {
                var found = false;
                for (GuiElement element : g) {
                    if (element instanceof DraggableElementReferenceButton b && b.getGlobalRect().contains(mouseX, mouseY)) {
                        if (hoveredButton != null) {
                            hoveredButton.hovering = false;
                        }
                        hoveredButton = b;
                        hoveredButton.hovering = true;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    hoveredButton = null;
                }
                break;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private double mouseClickX = 0;
    private double mouseClickY = 0;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseClickX = Math.floor(mouseX);
        mouseClickY = Math.floor(mouseY);
        for (GuiElement element1 : getElements()) {

            if (element1 instanceof GenericContainer g) {
                hoveredButton = null;
                clickedButton = null;
                for (GuiElement element : g) {
                    if (clickedButton == null && element instanceof DraggableElementReferenceButton a && a.isMouseOver(mouseX, mouseY)) {
                        clickedButton = a;
                        break;
                    }
                }
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (Math.floor(mouseX) == mouseClickX && Math.floor(mouseY) == mouseClickY && clickedButton != null) {
            screen.selectElement(clickedButton.referenced, false);
        } else {
            for (GuiElement element1 : getElements()) {
                if (element1 instanceof GenericContainer g) {
                    for (GuiElement element : g) {
                        if (element instanceof DraggableElementReferenceButton) {

                            if (clickedButton != null && hoveredButton != null) {
                                if (!hoveredButton.referenced.isChildOfElementBranch(clickedButton.referenced) && clickedButton != hoveredButton) {
                                    hoveredButton.transfer(clickedButton);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        clickedButton = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}

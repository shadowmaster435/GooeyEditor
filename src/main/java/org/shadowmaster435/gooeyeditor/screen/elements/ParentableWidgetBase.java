package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;
import org.shadowmaster435.gooeyeditor.util.ClassCodeStringBuilder;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public abstract non-sealed class ParentableWidgetBase extends GuiElement implements Iterable<GuiElement> {

    private final ArrayList<GuiElement> widgets = new ArrayList<>();
    public boolean updateChildren = true;
    public boolean renderChildren = true;

    public ParentableWidgetBase(int x, int y, boolean editMode) {
        super(x, y, editMode);
        updateChildren = true;
        renderChildren = true;
    }

    public ParentableWidgetBase(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
        updateChildren = true;
        renderChildren = true;
    }

    public void addElements(GuiElement... element) {
        for (GuiElement elem : element) {
            addElement(elem);
        }
    }

    public ArrayList<GuiElement> getElements() {
        return new ArrayList<>(widgets);
    }

    public void addElement(GuiElement element) {
        if (element == this) { // if this don't
            return;
        }
        widgets.add(element);
        element.parent = this;
    }
    public void removeElement(int index) {
        widgets.remove(index);
    }
    public void removeElement(GuiElement element) {
        widgets.remove(element);
    }
    public GuiElement getElement(int index) {
        GuiElement element = null;
        try {element = widgets.get(index);} catch (Exception ignored) {}
        return element;
    }
    public void clearChildren() {
        widgets.clear();
    }

    public Vector2i getCollectiveChildSize(int spacing_x, int spacing_y) {
        AtomicInteger width = new AtomicInteger();
        AtomicInteger height = new AtomicInteger();
        forEachInBranch((elem, par, d) -> {
            width.addAndGet(elem.getWidth() + spacing_x);
            height.addAndGet(elem.getHeight() + spacing_y);
        }, 0);

        width.addAndGet(-spacing_x);
        height.addAndGet(-spacing_y);

        return new Vector2i(Math.max(width.get(), 0), Math.max(height.get(), 0));
    }

    /**
     * Removes element from its parent if possible.
     */
    public void orphanize() {
        if (this.parent instanceof ParentableWidgetBase p) {
            p.removeElement(this);
            this.parent = null;
        }
    }

    public void reparent(ParentableWidgetBase element) {
        orphanize();
        element.addElement(this);
        parent = element;
    }

    public int getCollectiveChildHeight(int spacing) {
        return getCollectiveChildSize(0, spacing).y;
    }

    public int getCollectiveChildWidth(int spacing) {
        return getCollectiveChildSize(spacing, 0).x;
    }

    public boolean isChildOfElementBranch(ParentableWidgetBase root) { // needs some touchups but is functional enough for now. expect bug reports.
        var result = false;
        ParentableWidgetBase current = this;
        boolean firstChecked = false;
        int failSafeBreakCounter = 0;
        ArrayList<ParentableWidgetBase> checkedElements = new ArrayList<>();
        while (current.parent instanceof ParentableWidgetBase widget) {
            if (failSafeBreakCounter > 50) {
                return true;
            }
            failSafeBreakCounter += 1;
            if (current == root || (firstChecked && current == this)) {
                result = true;
                break;
            } else {
                firstChecked = true;
                if (checkedElements.contains(current)) {
                    return true;
                } else {
                    checkedElements.add(current);
                }
                current = widget;
            }
        }
        return result;
    }




    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {

        if (renderChildren) {
            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                var px = (parent != null) ? getGlobalX() - getX() - parent_offset_x: 0;
                var py = (parent != null) ? getGlobalY() - getY() - parent_offset_y : 0;
                var px2 = (parent != null) ? parent_offset_x : 0;
                var py2 = (parent != null) ? parent_offset_y : 0;
//                if (parent != null) {
//                    px = px - parent.getX();
//                    py = py - parent.parent_offset_y;
//
//                }


                element.parent_offset_x = getGlobalX() - px;
                element.parent_offset_y = getGlobalY() - py;
                element.render(context, mouseX, mouseY, delta);
            }
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    /**
     * Iterates through all child elements and children of those elements. Provides the depth of the current element being iterated as well as the element itself.
     * @param consumer Supplies the current element being iterate, the parent of that element, and the depth of that element in the widget tree branch.
     * @param startDepth Depth you want to start with. Useful for positioning.
     */
    public void forEachInBranch(TriConsumer<GuiElement, GuiElement, Integer> consumer, int startDepth) {
        widgets.forEach((widget -> {
            if (widget != this) {
                consumer.accept(widget, widget.parent, startDepth + 1);
            }
            if (widget instanceof ParentableWidgetBase widgetBase) {
                widgetBase.forEachInBranch(consumer, startDepth + 1);
            }
        }));
    }






    /**
     * @return Bounding rectangle for all children including children of children and so on.
     */
    public Rect2 getChildBounds() {
        AtomicInteger min_x = new AtomicInteger(1000000);
        AtomicInteger min_y = new AtomicInteger(1000000);
        AtomicInteger max_x = new AtomicInteger(-1000000);
        AtomicInteger max_y = new AtomicInteger(-1000000);


        forEachInBranch(((element, parent, integer) -> {
            min_x.set(Math.min(element.getX(), min_x.get()));
            min_y.set(Math.min(element.getY(), min_y.get()));
            max_x.set(Math.max(element.getX() + element.getSize().x, min_x.get()));
            max_y.set(Math.max(element.getY() + element.getSize().y, min_y.get()));

        }), 0);
        return Rect2.fromPoints(new Vector2i(min_x.get(), min_y.get()), new Vector2i(max_x.get(), max_y.get()));
    }

    @Override
    public boolean changed() {
        if (updateChildren) {
            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                if (element.changed()) {
                    return true;
                }
            }
        }
        return super.changed();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                element.mouseMoved(mouseX, mouseY);
            }
        }
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                element.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                element.mouseReleased(mouseX, mouseY, button);
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                element.keyPressed(keyCode, scanCode, modifiers);

            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                if (element == this) {
                    continue;
                }
                element.keyReleased(keyCode, scanCode, modifiers);
            }
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (updateChildren) {

        for (GuiElement element : widgets) {
            if (element == this) {
                continue;
            }
            element.charTyped(chr, modifiers);
        }
        }
        return super.charTyped(chr, modifiers);
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {

        return super.getNavigationPath(navigation);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        for (GuiElement element : widgets) {
            if (element == this) {
                continue;
            }
            element.isMouseOver(mouseX, mouseY);
        }
        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        for (GuiElement element : widgets) {
            if (element == this) {
                continue;
            }
            element.setFocused(focused);
        }

        super.setFocused(focused);
    }



    public GuiElement getHoveredChild(int mouseX, int mouseY) {
        for (GuiElement element : this) {
            if (element == this) {
                continue;
            }
            if (element.isMouseOver(mouseX, mouseY)) {
                return element;
            }
        }
        return null;
    }



    public void removeChildAt(int index) {
        widgets.remove(index);
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }

    @NotNull
    @Override
    public Iterator<GuiElement> iterator() {
        return widgets.iterator();
    }




    @Override
    public void createAssignerSetterString(ClassCodeStringBuilder.MethodStringBuilder methodStringBuilder, String className, HashMap<String, Integer> usedNames, boolean root) {
        ArrayList<Property> props = new ArrayList<>();
        props.addAll(Arrays.stream(getProperties()).toList());
        props.addAll(Arrays.stream(getDefaultProperties()).toList());
        for (Property property : props) {
            if (Objects.equals(property.display_name(), "Position")) {
                continue;
            }
            methodStringBuilder.line(getPropertyText(property, className));
        }
        if (root) {
            methodStringBuilder.line("addDrawableChild(" + className + ");");
            forEachInBranch((element, par, d) -> createChildrenStrings(methodStringBuilder, element, par, usedNames), 0);
        }
    }

    private void createChildrenStrings(ClassCodeStringBuilder.MethodStringBuilder methodStringBuilder, GuiElement element, GuiElement par, HashMap<String, Integer> usedNames) {
        if (par instanceof SlotGridWidget && element instanceof SlotWidget) {
            return;
        }
        element.createChildInitString(methodStringBuilder, element.getClass(), element.name, element, par, usedNames, GuiEditorScreen.getSafeName(this, usedNames, false));
    }
}

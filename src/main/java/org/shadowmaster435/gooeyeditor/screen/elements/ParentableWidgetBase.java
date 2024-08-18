package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ParentableWidgetBase extends GuiElement implements Iterable<GuiElement> {

    private final ArrayList<GuiElement> widgets = new ArrayList<>();
    public boolean updateChildren = true;
    public boolean renderChildren = true;

    public ParentableWidgetBase(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }

    public ParentableWidgetBase(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }

    public void addElements(GuiElement... element) {
        widgets.addAll(Arrays.stream(element).toList());
    }

    public void addElement(GuiElement element) {
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
        return widgets.get(index);
    }
    public void orphanizeChildren() {
        widgets.clear();
    }

    public boolean isChildOfElementBranch(ParentableWidgetBase rootElement) {
        var currentParent = parent;
        while (currentParent instanceof ParentableWidgetBase widgetBase) {
            if (widgetBase.parent == rootElement) {
                return true;
            } else {
                currentParent = widgetBase.parent;
            }
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (renderChildren) {
            for (GuiElement element : widgets) {
                element.render(context, mouseX, mouseY, delta);
            }
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    /**
     * Iterates through all child elements and children of those elements. Provides the depth of the current element being iterated as well as the element itself.
     * @param consumer Supplies the current element being iterate, the parent of that element, and the depth of that element in the widget tree branch.
     * @param depth Depth you want to start with. Useful for positioning.
     */
    public void forEachInBranch(TriConsumer<GuiElement, GuiElement, Integer> consumer, int depth) {
        widgets.forEach((widget -> {
            consumer.accept(widget, widget.parent, depth + 1);
            if (widget instanceof ParentableWidgetBase widgetBase) {
                widgetBase.forEachInBranch(consumer, depth + 1);
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
                element.mouseMoved(mouseX, mouseY);
            }
        }
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                element.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                element.mouseReleased(mouseX, mouseY, button);
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                element.keyPressed(keyCode, scanCode, modifiers);

            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (updateChildren) {

            for (GuiElement element : widgets) {
                element.keyReleased(keyCode, scanCode, modifiers);
            }
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (updateChildren) {

        for (GuiElement element : widgets) {
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
            element.isMouseOver(mouseX, mouseY);
        }
        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        for (GuiElement element : widgets) {
            element.setFocused(focused);
        }

        super.setFocused(focused);
    }



    public GuiElement getHoveredChild(int mouseX, int mouseY) {
        for (GuiElement element : this) {
            if (element.isMouseOver(mouseX, mouseY)) {
                return element;
            }
        }
        return null;
    }



    public void removeChildAt(int index) {
        widgets.remove(index);
    }

    public void removeChild(GuiElement element) {
        widgets.remove(element);
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }

    @NotNull
    @Override
    public Iterator<GuiElement> iterator() {
        return widgets.iterator();
    }
}

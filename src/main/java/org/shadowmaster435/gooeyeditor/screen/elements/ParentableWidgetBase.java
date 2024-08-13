package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public abstract class ParentableWidgetBase extends GuiElement implements Iterable<GuiElement> {

    public final ArrayList<GuiElement> widgets = new ArrayList<>();


    public ParentableWidgetBase(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }

    public ParentableWidgetBase(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        for (GuiElement element : widgets) {
            element.render(context, mouseX, mouseY, delta);
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }


    @Override
    public boolean changed() {
        for (GuiElement element : widgets) {
            if (element.changed()) {
                return true;
            }
        }
        return super.changed();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (GuiElement element : widgets) {
            element.mouseMoved(mouseX, mouseY);
        }
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiElement element : widgets) {
            element.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiElement element : widgets) {
            element.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (GuiElement element : widgets) {
            element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (GuiElement element : widgets) {
            element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (GuiElement element : widgets) {
            element.keyPressed(keyCode, scanCode, modifiers);

        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (GuiElement element : widgets) {
            element.keyReleased(keyCode, scanCode, modifiers);
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (GuiElement element : widgets) {
            element.charTyped(chr, modifiers);
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

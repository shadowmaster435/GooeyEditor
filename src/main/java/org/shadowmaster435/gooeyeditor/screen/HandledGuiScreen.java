package org.shadowmaster435.gooeyeditor.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.util.InputHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class HandledGuiScreen<T extends ScreenHandler> extends HandledScreen<T> {

    private final HashMap<String, GuiElement> elements = new HashMap<>();
    private boolean initialized = false;

    protected HandledGuiScreen(T handler, PlayerInventory inventory) {
        super(handler, inventory, Text.of(""));
        init();

    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        if (client != null && !initialized) {
            refreshElements();
            initElements();
            clinit();
            initialized = true;
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    private HashMap<Slot,SlotWidget> touchSlots = new HashMap<>();
    private int touchHoveredSlotCount = 0;


    private int pressed_button = -1;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        for (SlotWidget widget : touchSlots.values()) {
            if (cursorDragging) {
                if (widget != null) {
                    if (InputHelper.isLeftMouseHeld && pressed_button == 0) {
                        addMulti(widget);
                    }
                    if (InputHelper.isRightMouseHeld && pressed_button == 1) {
                        addPlusOne(widget);
                    }
                    if (InputHelper.isMiddleMouseHeld && pressed_button == 2) {
                        addClone(widget);
                    }
                }
            }
        }
    }

    private void addClone(SlotWidget widget) {
        var stack = handler.getCursorStack();
        widget.touchDraggedStack = stack.copyWithCount(stack.getCount());
    }

    private void addPlusOne(SlotWidget widget) {
        var cstack = handler.getCursorStack();
        var stack = widget.displayedSlot.getStack();
        int amount = (stack.isEmpty()) ? 1 : Math.min(stack.getCount() + 1, stack.getMaxCount());
        widget.touchDraggedStack = cstack.copyWithCount(amount);
    }

    private void addMulti(SlotWidget widget) {
        var stack = handler.getCursorStack();
        int amount = stack.getCount() / touchSlots.size();
        var sstack = widget.displayedSlot.getStack();
        int extra = (stack.isEmpty()) ? 0 : sstack.getCount();
        widget.touchDraggedStack = stack.copyWithCount(amount + extra);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (getHoveredSlot(mouseX, mouseY) instanceof SlotWidget widget && widget.displayedSlot != null && widget.displayedSlot.canInsert(this.handler.getCursorStack()) && !(touchSlots.containsValue(widget))) {
            touchSlots.put(widget.displayedSlot, widget);
        }
        if (pressed_button == -1) {
            pressed_button = button;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (SlotWidget widget : touchSlots.values()) {
            widget.touchDraggedStack = null;
        }
        pressed_button = -1;
        touchSlots.clear();

        return super.mouseReleased(mouseX, mouseY, button);
    }

    public abstract void clinit();

    public abstract void initElements();

    public GuiElement[] getElements() {
        return elements.values().toArray(new GuiElement[]{});
    }

    public GuiElement getElement(String key) {
        return elements.getOrDefault(key, null);
    }

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

    public GuiElement getHoveredElement(double mouseX, double mouseY) {
        for (GuiElement element : elements.values()) {
            if (element.isMouseOver(mouseX, mouseY)) {
                return element;
            }
        }
        return null;
    }

    public SlotWidget getHoveredSlot(double mouseX, double mouseY) {
        for (GuiElement element : elements.values()) {
            if (element.isMouseOver(mouseX, mouseY) && element instanceof SlotGridWidget slotWidget) {
                var child = slotWidget.getHoveredChild((int) mouseX, (int) mouseY);
                if (child instanceof SlotWidget e) {
                    return e;
                }
            }
            if (element instanceof PlayerInventoryWidget playerInventoryWidget) {
                return playerInventoryWidget.getHoveredSlot((int) mouseX, (int) mouseY);
            }
            if (element.isMouseOver(mouseX, mouseY) && element instanceof SlotWidget slotWidget) {
                return slotWidget;
            }
        }
        return null;
    }

    public final JsonObject toJson() {
        var result = new JsonObject();
        for (String key : elements.keySet()) {
            var element = elements.get(key);
            element.writeJson(result);

        }
        return result;
    }

    public void addElement(GuiElement element) {
        addDrawableChild(element);
    }

    @Override
    public <E extends Element & Drawable & Selectable> E addDrawableChild(E drawableElement) {
        if (drawableElement instanceof GuiElement e) {
            elements.put(e.name, e);
        }
        return super.addDrawableChild(drawableElement);
    }

    public final void fromJson(JsonObject object) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            var element = GuiElement.fromJson(entry.getValue().getAsJsonObject(), entry.getKey(), false);
            addDrawableChild(element);
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public abstract boolean isClickOutsideBounds(double mouseX, double mouseY, int button);

}

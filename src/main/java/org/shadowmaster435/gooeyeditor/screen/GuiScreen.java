package org.shadowmaster435.gooeyeditor.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class GuiScreen<T extends ScreenHandler> extends HandledScreen<T> {

    private final HashMap<String, GuiElement> elements = new HashMap<>();
    private boolean initialized = false;

    protected GuiScreen(T handler, PlayerInventory inventory) {
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
    private void a(AtomicReference<SlotWidget> result, AtomicBoolean stop,GuiElement e, double mouseX, double mouseY) {
        System.out.println(e);
        if (e instanceof SlotWidget widget && widget.isMouseOver(mouseX, mouseY)) {
            result.set(widget);

            stop.set(true);
        }
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
